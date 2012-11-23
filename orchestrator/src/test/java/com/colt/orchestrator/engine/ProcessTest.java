package com.colt.orchestrator.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.drools.event.DefaultProcessEventListener;
import org.drools.event.process.ProcessCompletedEvent;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.colt.common.logger.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/application-context.xml" })
public class ProcessTest {
	private static final Logger LOG = Logger.getLogger(ProcessTest.class);

	@Autowired
	protected StatefulKnowledgeSession kSession;

	@EndpointInject(uri = "jms:servicerequest")
	protected ProducerTemplate serviceRequestProducer;

	private volatile boolean processRunning = true;

	private DefaultProcessEventListener listener = new DefaultProcessEventListener() {
		@Override
		public void afterProcessCompleted(ProcessCompletedEvent event) {
			LOG.message("Process: " + event.getProcessInstance().getId() + " completed");
			processRunning = false;
		}
	};

	/**
	 * Creates a consumer on the router endpoint that returns the next queued
	 * response. We may need to get fancier and hold a map of responses
	 * identified by a TEST_RESPONSE header
	 * 
	 * @throws Exception
	 */

	@Before
	public void setup() throws Exception {
		kSession.addEventListener(listener);
	}

	@After
	public void teardown() throws Exception {
		kSession.removeEventListener(listener);
	}


	/**
	 * Blocks until the process is completed
	 */
	protected void runProcess(Object message) {
		serviceRequestProducer.sendBody(message);

		while (processRunning) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected String getDate() {
		SimpleDateFormat fmt = new SimpleDateFormat();
		return fmt.format(new Date());
	}
}
