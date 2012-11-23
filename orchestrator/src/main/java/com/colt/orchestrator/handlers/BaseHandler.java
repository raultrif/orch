package com.colt.orchestrator.handlers;

import static com.colt.common.beans.BeanHelper.beanToMap;
import static com.colt.common.beans.BeanHelper.mapToBean;
import static com.colt.common.context.Constants.ERROR_EVENT;
import static com.colt.common.context.Constants.JBPM_ERROR_CODE;
import static com.colt.common.context.Constants.JBPM_ERROR_MESSAGE;
import static com.colt.common.context.Constants.JBPM_PLATFORM;
import static com.colt.common.context.Constants.JBPM_SUCCESS;
import static com.colt.common.context.Constants.JBPM_TASK_NAME;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.XmlMappingException;

import com.colt.common.logger.Logger;
import com.colt.common.text.UserMessageService;
import com.colt.orchestrator.engine.ResponseHandler;
import com.colt.orchestrator.engine.TaskContext;
import com.colt.orchestrator.engine.TaskContextService;

/**
 * The base class of all handler, implementing jbpm executeWorkItem to initiate
 * a task and implementing handlerResponse to handle the corresponding response.
 * 
 * 
 * @author richardevans
 * 
 * @param <I>
 * @param <O>
 */
public abstract class BaseHandler<I, O> implements WorkItemHandler, ResponseHandler<O>, BeanNameAware {
	private final static Logger LOG = Logger.getLogger(BaseHandler.class);

	protected String handlerName;

	@Override
	public void setBeanName(String name) {
		this.handlerName = name;
	}

	@EndpointInject(uri = "direct:platformrequest")
	ProducerTemplate producer;

	@Autowired
	protected UserMessageService userMessageService;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private TaskContextService taskContextService;

	private volatile StatefulKnowledgeSession kSession;

	/**
	 * Lazily wire kSession. Cannot wire earlier as creates circular dependency
	 * since kSession contains a collection of handlers including this.
	 * 
	 * @return
	 */
	protected StatefulKnowledgeSession getKSession() {
		if (kSession == null) {
			synchronized (this) {
				kSession = appContext.getBean(StatefulKnowledgeSession.class);
			}
		}
		return kSession;
	}

	/**
	 * Handle JBPM requests. A thin wrapper to trap all exceptions.
	 */

	final public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		try {
			onRequest(workItem, manager);
		} catch (Exception e) {
			TaskContext ctx = new TaskContext(workItem, handlerName, "");
			handleException(ctx, e);
		}
	}

	/**
	 * The method that concrete handlers override to deal with requests.
	 * 
	 * @param workItem
	 * @param manager
	 */
	public abstract void onRequest(WorkItem workItem, WorkItemManager manager) throws Exception;

	/**
	 * The response handler invoked by the response listener. A thin wrapper to
	 * trap all exceptions.
	 */

	final public void handleResponse(TaskContext ctx, O response) {
		try {
			WorkItemManager manager = getKSession().getWorkItemManager();
			LOG.message("orchestrator.basehandler.response", response);
			onResponse(ctx, response, manager, ctx.workItemId);
		} catch (Exception e) {
			handleException(ctx, e);
		}
	}

	/**
	 * Generic response handler that invokes handler-specific processing and
	 * then either returns errors (if handlerError is true) or raises an error
	 * Signal (if HandlerError is not true).
	 * 
	 * Though this can be overridden by concrete handlers, they will normally
	 * choose to override onSuccess to benefit from the error handling in this
	 * method.
	 * 
	 * @param response
	 * @param manager
	 * @param workItemId
	 */

	public void onResponse(TaskContext ctx, O response, WorkItemManager manager, long workItemId) {
		Map<String, Object> results = new HashMap<String, Object>();
		String errorCode = getErrorCode(response);
		String errorText = getErrorText(response);

		results.put(JBPM_ERROR_CODE, errorCode);
		results.put(JBPM_ERROR_MESSAGE, errorText);

		if (JBPM_SUCCESS.equals(errorCode)) {
			onSuccess(ctx, response, results);
		} else {
			onFailure(response, results);
		}
		errorCode = (String) results.get(JBPM_ERROR_CODE);
		errorText = (String) results.get(JBPM_ERROR_MESSAGE);

		if (JBPM_SUCCESS.equals(errorCode) || ctx.handleErrors) {
			manager.completeWorkItem(workItemId, results);
		} else {
			signalError(ctx, errorCode, errorText);
		}
	}

	/**
	 * Default success handler maps bean properties to results (shallow). This
	 * may be adequate for simple handlers but will often be overridden for more
	 * substantial functionality.
	 * 
	 * @param response
	 * @param results
	 */
	protected void onSuccess(TaskContext ctx, O response, Map<String, Object> results) {
		beanToMap(response, results);
	}

	/**
	 * Null default failure handler.
	 * 
	 * @param response
	 * @param result
	 */
	protected void onFailure(O response, Map<String, Object> result) {

	}

	/**
	 * Defer extraction of errorcode from response to platform-specific handler
	 * 
	 * @param response
	 * @return error code
	 */
	protected abstract String getErrorCode(O response);

	/**
	 * Defer extraction of errorcode from response to platform-specific handler
	 * 
	 * @param response
	 * @return error text
	 */
	protected abstract String getErrorText(O response);

	/**
	 * Exception handler. A thin wrapper that traps any exceptions generated
	 * during handling.
	 * 
	 * @param workItemId
	 * @param processInstanceId
	 * @param taskName
	 * @param exception
	 */

	final private void handleException(TaskContext ctx, Exception exception) {
		try {
			WorkItemManager manager = getKSession().getWorkItemManager();
			onException(ctx, manager, exception);
		} catch (Exception e) {
			LOG.message(e, "orchestrator.basehandler.exception", e.getMessage());
		}
	}

	/**
	 * Generic exception handler that returns errors if handlerError is true or
	 * raises an error Signal otherwise.
	 * 
	 * Note that exceptions and errors share a single handler. When an exception
	 * is raised, ctx.platform will be set to ORCHESTRATOR
	 * 
	 * 
	 */

	public void onException(TaskContext ctx, WorkItemManager manager, Exception exception) {
		String code = exception.getClass().getName();
		String msg = exception.getMessage();
		if (ctx.handleErrors) {
			Map<String, Object> results = createErrorResults(ctx, code, msg);
			manager.completeWorkItem(ctx.workItemId, results);
		} else {
			signalError(ctx, code, msg);
		}
	}

	/**
	 * JBPM abort
	 */

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		LOG.message("orchestrator.basehandler.abort", workItem.getName());
	}

	// ============================================================================================
	// HELPERS
	// ============================================================================================

	/**
	 * Helper for concrete handlers to populate a request from jbpm input map
	 * 
	 * @param request
	 * @param parameters
	 * @param workItemId
	 */

	protected void populateRequest(I request, WorkItem workItem) {
		mapToBean(workItem.getParameters(), request);
	}

	/**
	 * Helper for concrete handlers to send a request. The context of the
	 * requests is saved in the request headers to be retrieved when the
	 * response is generated. The context includes details of the handler and
	 * workitem to allow the response to be routed back to the correct handler
	 * and process. It also includes the request for those handlers that need
	 * input parameters to process the response.
	 * 
	 * @param request
	 * @param workItemId
	 * @throws Exception
	 */

	protected void sendRequest(Object request, WorkItem workItem) throws Exception {
		LOG.message("orchestrator.basehandler.sendrequest", request);
		TaskContext ctx = new TaskContext(workItem, handlerName, request);
		producer.sendBodyAndHeaders(request, taskContextService.toHeaders(ctx));
	}

	/**
	 * Helper for concrete handlers to populate a request and send to the
	 * request route.
	 * 
	 * @param request
	 * @param workItem
	 * @throws Exception
	 */
	protected void populateAndSendRequest(I request, WorkItem workItem) throws Exception {
		populateRequest(request, workItem);
		sendRequest(request, workItem);
	}

	/**
	 * Signals a error on the process.
	 * 
	 * @param ctx
	 * @param code
	 * @param msg
	 */

	protected void signalError(TaskContext ctx, String code, String msg) {
		getKSession().signalEvent(ERROR_EVENT, createErrorResults(ctx, code, msg), ctx.processInstanceId);
	}

	/**
	 * Puts error details into a HashMap. Used for completing a work item or
	 * raising an event
	 * 
	 * @param ctx
	 * @param code
	 * @param msg
	 * @return
	 */

	protected Map<String, Object> createErrorResults(TaskContext ctx, String code, String message, String... args) {
		Map<String, Object> results = new HashMap<String, Object>();
		setErrorResults(results, ctx, code, message);
		return results;
	}

	/**
	 * Looks up message from the user messages resource bundle, formats and
	 * populates results
	 * 
	 * @param results
	 * @param ctx
	 * @param code
	 * @param args
	 */

	protected void lookupErrorResults(Map<String, Object> results, TaskContext ctx, String code, String... args) {
		setErrorResults(results, ctx, code, userMessageService.format(code, args));
	}

	/**
	 * Populates standard error results
	 * 
	 * @param results
	 * @param ctx
	 * @param code
	 * @param message
	 */

	protected void setErrorResults(Map<String, Object> results, TaskContext ctx, String code, String message) {
		results.put(JBPM_PLATFORM, ctx.platform);
		results.put(JBPM_ERROR_CODE, code);
		results.put(JBPM_ERROR_MESSAGE, message);
		results.put(JBPM_TASK_NAME, ctx.taskName);
	}
}
