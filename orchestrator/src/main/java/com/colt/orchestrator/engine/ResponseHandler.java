package com.colt.orchestrator.engine;


public interface ResponseHandler<O> {
	public void handleResponse(TaskContext ctx, O response);
}
