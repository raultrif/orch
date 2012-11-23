package com.colt.orchestrator.engine;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.colt.common.beans.BeanHelper;
import com.colt.common.logger.Logger;
import com.colt.orchestrator.message.GenericServiceRequest;
import com.colt.orchestrator.message.ServiceRequest;
import com.colt.orchestrator.message.ServiceRequestResolver;
import com.colt.orchestrator.message.ServiceResponse;

/**
 * A Camel consumer that fields ServiceRequests, looks up the corresponding jpbm
 * process and launches with request parameters.
 * 
 * @author richardevans
 * 
 */
@Service
public class ProcessRunner {
	private static final Logger LOG = Logger.getLogger(ProcessRunner.class);

	@Autowired
	private StatefulKnowledgeSession kSession;

	public ProcessRunner() {
		super();
	}

	@Consume(uri = "jms:servicerequest")
	public void onRequest(GenericServiceRequest request) {
		String command = request.getCommand();

		try {
			ServiceRequest serviceRequest = ServiceRequestResolver.getRequestInstance(command);
			ServiceResponse serviceResponse = ServiceRequestResolver.getResponseInstance(command);
			BeanHelper.mapToBean(request.getParameters(), serviceRequest);
			serviceRequest.setCommand(command);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("request", serviceRequest);
			params.put("response", serviceResponse);
			params.put("status", new HashMap<String, Object>());
			kSession.startProcess(command, params);

		} catch (Exception e) {
			LOG.message(e, "orchestrator.processrunner.taskrejected", command, e.getMessage());
		}
	}

	public static void main(String agrs[]) throws InterruptedException {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "META-INF/spring/application-context.xml" });
		appContext.start();
		appContext.registerShutdownHook();
		LOG.message("orchestrator.processrunner.running");
		synchronized (ProcessRunner.class) {
			ProcessRunner.class.wait();
		}
		LOG.message("orchestrator.processrunner.stopping");
	}
}
