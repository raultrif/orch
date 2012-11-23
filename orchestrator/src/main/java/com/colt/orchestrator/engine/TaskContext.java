package com.colt.orchestrator.engine;

import static com.colt.common.context.Constants.HANDLER_PLATFORM_ORCHESTRATOR;
import static com.colt.common.context.Constants.JBPM_HANDLE_ERRORS;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.drools.runtime.process.WorkItem;
@XmlRootElement
public class TaskContext implements Serializable {
	private static final long serialVersionUID = -5989165615740784379L;
	public String platform ;
	public String handlerName;
	public String taskName;
	public long processInstanceId;
	public long workItemId;
	public boolean handleErrors = false;
	public Object request;

	public TaskContext() {
	}

	public TaskContext(WorkItem workItem, String handlerName, Object request) {
		this.handleErrors = Boolean.valueOf((String)workItem.getParameter(JBPM_HANDLE_ERRORS));
		this.workItemId = workItem.getId();
		this.processInstanceId = workItem.getProcessInstanceId();
		this.handlerName = handlerName;
		this.taskName = workItem.getName();
		this.platform = HANDLER_PLATFORM_ORCHESTRATOR;
		this.request = request;
	}

	@SuppressWarnings("unchecked")
	public <I> I getRequest( Class<I> clazz) {
		return (I)request;
	}

}
