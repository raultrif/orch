package com.colt.orchestrator.engine;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.colt.common.logger.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/test-context.xml" })
public class StubbedProcessTest extends ProcessTest {
	private static final Logger LOG = Logger.getLogger(StubbedProcessTest.class);
	protected Queue<Object> responses = new LinkedList<Object>();

	@EndpointInject(uri = "direct:platformresponse")
	protected ProducerTemplate platformResponseProducer;

	@Autowired
	protected CamelContext camelContext;
	private static CamelContext staticCamelContext;

	private Consumer consumer;

	/**
	 * Creates a consumer on the router endpoint that returns the next queued
	 * response. We may need to get fancier and hold a map of responses
	 * identified by a TEST_RESPONSE header
	 * 
	 * @throws Exception
	 */

	@Before
	public void setup() throws Exception {
		LOG.message("setup");
		Endpoint endpoint = camelContext.getEndpoint("direct:platformrequest");
		consumer = endpoint.createConsumer(new Processor() {
			public void process(Exchange exchange) throws Exception {
				Map<String, Object> headers = exchange.getIn().getHeaders();
				Object response;
				try {
					response = responses.remove();
				} catch (Exception e) {
					response = "TEST ERROR: Response queue is empty";
				}
				platformResponseProducer.sendBodyAndHeaders(response, headers);
			}
		});

		consumer.start();
		super.setup();
	}

	@After
	public void teardown() throws Exception {
		super.teardown();
		consumer.stop();
	}

	/**
	 * Adds a response to the FIFO response queue. The response is cloned so
	 * that the caller can alter and re-use the response
	 * 
	 * @param response
	 */
	protected void pushResponse(Object response) {
		try {
			responses.add(BeanUtils.cloneBean(response));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@AfterClass
	public static void teardownClass() throws Exception {
		staticCamelContext.stop();
	}
}
