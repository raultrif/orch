package com.colt.common.context;

public class Constants {

	// ORCH headers

	public static final String ORCH_HDR_PREFIX = "ORCH_";
	public static final String PLATFORM_HDR = ORCH_HDR_PREFIX + "PLATFORM";
	public static final String CONTEXT_HDR = ORCH_HDR_PREFIX + "CONTEXT";

	public static final String RESPONSE_CLASS_HDR = ORCH_HDR_PREFIX + "RESPONSE_CLASS";
	public static final String JBPM_ERROR_CODE = "ErrorCode";
	public static final String JBPM_ERROR_MESSAGE = "ErrorMessage";
	public static final String JBPM_SUCCESS = "SUCCESS";
	public static final String JBPM_PLATFORM = "Platform";
	public static final String JBPM_HANDLE_ERRORS = "HandleErrors";
	public static final String JBPM_TASK_NAME = "TaskName";

	// platforms
	public static final String HANDLER_PLATFORM_ORCHESTRATOR = ORCH_HDR_PREFIX + "ORCHESTRATOR";
	public static final String HANDLER_PLATFORM_CLOUDSTACK = ORCH_HDR_PREFIX + "CLOUDSTACK";

	public static final String ERROR_EVENT = "ORCH_ERROR";
	public static final String AMQ_SCHEDULED_DELAY_HDR = "AMQ_SCHEDULED_DELAY";

}
