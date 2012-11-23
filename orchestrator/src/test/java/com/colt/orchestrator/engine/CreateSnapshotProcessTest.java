package com.colt.orchestrator.engine;

import java.util.Map;

import org.junit.Test;

import com.colt.cloudstack.message.domain.CloudstackDomain;
import com.colt.cloudstack.message.domain.CloudstackListDomainsResponse;
import com.colt.cloudstack.message.snapshot.CloudstackCreateSnapshotAsycnResponse;
import com.colt.cloudstack.message.snapshot.CloudstackJobResultSnapshot;
import com.colt.cloudstack.message.snapshot.CloudstackSnapshot;
import com.colt.common.logger.Logger;
import com.colt.orchestrator.message.GenericServiceRequest;

/**
 * Test cases:
 * <ol>
 * <li>Success</li>
 * <li>Snapshot failure in cloudstack</li>
 * <li>Unable to ascertain domain in orchestrator</li> </li>
 * 
 * @author richardevans
 * 
 */
public class CreateSnapshotProcessTest extends StubbedProcessTest {
	private static final Logger LOG = Logger.getLogger(CreateSnapshotProcessTest.class);
	private static final String TIME_PROPERTY = "time";
	private static final String DOMAIN_ID = "1";
	private static final String SNAPSHOT_ID = "123";
	private static final String VOLUME_ID = "volume-id";
	private static final String VOLUME_ID_PARAM = "volumeId";
	private static final String DOMAIN_PARAM = "domainName";
	private static final String ACCOUNT_PARAM = "account";
	private static final String ACCOUNT = "account";
	private static final String DOMAIN_NAME = "Customer A";

	@Test
	public void testSnapshotSuccess() throws Exception {

		CloudstackListDomainsResponse listDomainsResponse = mockListDomainsResponse();
		pushResponse(listDomainsResponse);

		CloudstackCreateSnapshotAsycnResponse createSnapshotAsycResponse = mockCreateSnapshotAsynchResponse();
		pushResponse(createSnapshotAsycResponse);

		GenericServiceRequest message = createServiceRequest();
		runProcess(message);
		LOG.message("---------- testSnapshotSuccess completed ----------");
		// TODO: Listen on response queue and check output. Can't do until
		// Response Q done.
	}

	@Test
	public void testSnapshotFailure() throws Exception {

		CloudstackListDomainsResponse listDomainsResponse = mockListDomainsResponse();
		pushResponse(listDomainsResponse);

		CloudstackCreateSnapshotAsycnResponse createSnapshotAsycResponse = mockCreateSnapshotAsynchResponseFailure();
		pushResponse(createSnapshotAsycResponse);

		GenericServiceRequest message = createServiceRequest();
		runProcess(message);
		LOG.message("---------- testSnapshotFailure completed ----------");

		// TODO: Listen on response queue and check output. Can't do until
		// Response Q done.
	}

	@Test
	public void testSnapshotNoDomain() throws Exception {

		CloudstackListDomainsResponse listDomainsResponse = mockListDomainsResponse();
		listDomainsResponse.getEntities().clear();
		listDomainsResponse.setCount(0);
		pushResponse(listDomainsResponse);

		GenericServiceRequest message = createServiceRequest();
		runProcess(message);
		LOG.message("---------- testSnapshotNoDomain completed ----------");

		// TODO: Listen on response queue and check output. Can't do until
		// Response Q done.
	}

	// /////////////// HELPERS ///////////////////////

	private GenericServiceRequest createServiceRequest() {
		GenericServiceRequest message = new GenericServiceRequest();
		Map<String, String> params = message.getParameters();
		message.setCommand("CreateSnapshot");
		params.put(TIME_PROPERTY, getDate());
		params.put(DOMAIN_PARAM, DOMAIN_NAME);
		params.put(ACCOUNT_PARAM, ACCOUNT);
		params.put(VOLUME_ID_PARAM, VOLUME_ID);
		return message;
	}

	private CloudstackCreateSnapshotAsycnResponse mockCreateSnapshotAsynchResponse() {
		CloudstackCreateSnapshotAsycnResponse createSnapshotAsycResponse = new CloudstackCreateSnapshotAsycnResponse();
		CloudstackJobResultSnapshot cloudstackJobResultSnapshot = new CloudstackJobResultSnapshot();
		CloudstackSnapshot cloudstackSnaphshot = new CloudstackSnapshot();
		cloudstackSnaphshot.setId(SNAPSHOT_ID);
		cloudstackJobResultSnapshot.setEntity(cloudstackSnaphshot);
		createSnapshotAsycResponse.setJobresult(cloudstackJobResultSnapshot);
		return createSnapshotAsycResponse;
	}

	private CloudstackCreateSnapshotAsycnResponse mockCreateSnapshotAsynchResponseFailure() {
		CloudstackCreateSnapshotAsycnResponse createSnapshotAsycResponse = new CloudstackCreateSnapshotAsycnResponse();
		createSnapshotAsycResponse.setErrorcode(111l);
		createSnapshotAsycResponse.setErrortext("Failed to create snapshot to help your test");
		return createSnapshotAsycResponse;
	}

	private CloudstackListDomainsResponse mockListDomainsResponse() {
		// Mock ListDomains response
		CloudstackListDomainsResponse listDomainsResponse = new CloudstackListDomainsResponse();
		CloudstackDomain cloudstackDomain = new CloudstackDomain();
		cloudstackDomain.setName(DOMAIN_NAME);
		cloudstackDomain.setId(DOMAIN_ID);
		listDomainsResponse.getEntities().add(cloudstackDomain);
		listDomainsResponse.setCount(1);
		return listDomainsResponse;
	}
}
