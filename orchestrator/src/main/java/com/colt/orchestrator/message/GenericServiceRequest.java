package com.colt.orchestrator.message;

import java.util.HashMap;
import java.util.Map;


public class GenericServiceRequest extends ServiceRequest {
	private static final long serialVersionUID = 4770562009585073456L;
	private Map<String, String> parameters = new HashMap<String, String>();

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
}
