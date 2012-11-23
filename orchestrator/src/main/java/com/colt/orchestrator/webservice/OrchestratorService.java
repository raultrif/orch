package com.colt.orchestrator.webservice;

import com.colt.orchestrator.message.GenericServiceRequest;

public interface OrchestratorService {
	public void onRequest(GenericServiceRequest request);
}
