package com.colt.orchestrator.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;

import com.colt.orchestrator.message.GenericServiceRequest;

@WebService()
public class OrchestratorServiceImpl implements OrchestratorService {

	@EndpointInject()
	private ProducerTemplate serviceRequestQueue;

	private String uri;

	@WebMethod
	public void onRequest(GenericServiceRequest request) {
		serviceRequestQueue.sendBody(uri, request);
	}

	@WebMethod(exclude=true)  
	public String getUri() {
		return uri;
	}

	@WebMethod(exclude=true)  
	public void setUri(String uri) {
		this.uri = uri;
	}
}
