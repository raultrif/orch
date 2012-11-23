package com.colt.orchestrator.handlers.cloudstack;

import static com.colt.common.beans.BeanHelper.beanToMap;
import static com.colt.common.context.Constants.JBPM_SUCCESS;

import java.util.Map;

import com.colt.cloudstack.message.CloudstackAsyncResponse;
import com.colt.cloudstack.message.CloudstackRequest;
import com.colt.cloudstack.message.CloudstackResponse;
import com.colt.orchestrator.engine.TaskContext;
import com.colt.orchestrator.handlers.BaseHandler;

/**
 * Superclass of all CS handlers providing only a little more than the
 * BaseHandler. It recognises asynchronous jobs and extracts the underlying
 * entity from the job wrapper; it maps cloudstack error details to orchestrator
 * 
 * @author richardevans
 * 
 */

public abstract class CloudstackHandler<I extends CloudstackRequest, O extends CloudstackResponse> extends
		BaseHandler<I, O> {

	/**
	 * Default Cloudstack response Handler suitable for simple responses (sync
	 * or async) where the response properties are mapped directly to results.
	 * For async responses, the embedded entity us unwrapped.
	 * 
	 * @param response
	 * @param results
	 */

	@Override
	protected void onSuccess(TaskContext ctx, O response, Map<String, Object> results) {
		if (response instanceof CloudstackAsyncResponse) {
			beanToMap(((CloudstackAsyncResponse<?, ?>) response).getJobresult().getEntity(), results);
		} else {
			beanToMap(response, results);
		}
	}

	/**
	 * Extract error from response
	 * 
	 * @param response
	 * @return errorCode
	 */

	@Override
	protected String getErrorCode(O response) {
		Long errorCode = response.getErrorcode();
		return errorCode == null || errorCode == 0 ? JBPM_SUCCESS : String.valueOf(errorCode);
	}

	@Override
	protected String getErrorText(O response) {
		return response.getErrortext();
	}
}
