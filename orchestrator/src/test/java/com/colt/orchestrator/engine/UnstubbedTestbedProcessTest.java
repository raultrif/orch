package com.colt.orchestrator.engine;

import java.util.Map;

import org.junit.Test;

import com.colt.common.logger.Logger;
import com.colt.orchestrator.message.GenericServiceRequest;

/**
 * This demonstrates unstubbed testing and 2 styles of error handling. The
 * CreateSnapshot handler is executed 3 times as follows:
 * 
 * <ol>
 * <li>Success</li>
 * <li>Fails with HandleErrors set to true and the error is handled explicitly</li>
 * <li>Fails with HandleErrors set to false and error is handled in by a signal</li>
 * </ol>
 * 
 * @author richardevans
 * 
 */

public class UnstubbedTestbedProcessTest extends ProcessTest {
	private static final Logger LOG = Logger.getLogger(UnstubbedTestbedProcessTest.class);
	private static final String TIME_PROPERTY = "time";
	private static final String VOLUME_ID = "volume-id";
	private static final String VOLUME_ID_PARAM = "volumeId";
	private static final String DOMAIN_PARAM = "domainName";
	private static final String ACCOUNT_PARAM = "account";
	private static final String ACCOUNT = "account";
	private static final String DOMAIN_NAME = "Customer A";

	@Test
	public void testDemo() throws Exception {

		GenericServiceRequest message = createServiceRequest();
		runProcess(message);
		LOG.message("---------- testDemo completed ----------");
		// TODO: Listen on response queue and check output. Can't do until
		// Response Q done.
	}

	// /////////////// HELPERS ///////////////////////

	private GenericServiceRequest createServiceRequest() {
		GenericServiceRequest message = new GenericServiceRequest();
		Map<String, String> params = message.getParameters();
		message.setCommand("Testbed");
		params.put(TIME_PROPERTY, getDate());
		params.put(DOMAIN_PARAM, DOMAIN_NAME);
		params.put(ACCOUNT_PARAM, ACCOUNT);
		params.put(VOLUME_ID_PARAM, VOLUME_ID);
		return message;
	}

}
