package com.colt.orchestrator.handlers.cloudstack;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.colt.cloudstack.message.snapshot.CloudstackCreateSnapshotAsycnResponse;
import com.colt.cloudstack.message.snapshot.CloudstackCreateSnapshotRequest;
import com.colt.common.logger.Logger;

/**
 * Creates a snapshot of a volume in Cloudstack and returns snaphot details
 * 
 * @author richardevans
 * 
 */
public class CreateSnapshotHandler extends
		CloudstackHandler<CloudstackCreateSnapshotRequest, CloudstackCreateSnapshotAsycnResponse> {
	private final static Logger LOG = Logger.getLogger(CreateSnapshotHandler.class);

	/**
	 * Populates request from like-named parameters and submits to cloudstack
	 */

	@Override
	public void onRequest(WorkItem workItem, WorkItemManager manager) throws Exception {
		CloudstackCreateSnapshotRequest request = new CloudstackCreateSnapshotRequest();
		populateRequest(request, workItem);
		LOG.message("orchestrator.createsnapshot.request", request);
		sendRequest(request, workItem);
	}

	/**
	 * Default onSuccess populates results from like-named parameters.
	 */

}
