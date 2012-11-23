package com.colt.orchestrator.handlers.cloudstack;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.colt.cloudstack.message.domain.CloudstackDomain;
import com.colt.cloudstack.message.domain.CloudstackListDomainsRequest;
import com.colt.cloudstack.message.domain.CloudstackListDomainsResponse;
import com.colt.common.logger.Logger;
import com.colt.orchestrator.engine.TaskContext;
/**
 * Looks up a domain by name and returns its id.
 * Raises error if there is not exactly 1 match.
 * 
 * NB - The logic here is probably NOT accurate. Current version demo-ing some principals but not checked for accuracy
 * 
 * 
 * @author richardevans
 *
 */
public class AscertainDomainHandler extends
		CloudstackHandler<CloudstackListDomainsRequest, CloudstackListDomainsResponse> {
	private static final String ASCERTAIN_RESULT = "domainid";
	private final static Logger LOG = Logger.getLogger(AscertainDomainHandler.class);

	/**
	 * Populates request from like-named parameters and submits to cloudstack
	 */
	
	@Override
	public void onRequest(WorkItem workItem, WorkItemManager manager) throws Exception {
		CloudstackListDomainsRequest request = new CloudstackListDomainsRequest();
		//TODO: Check exact logic here. Should we specify name on input? If not, remove call to populateRequest
		populateRequest(request, workItem);
		LOG.message("orchestrator.ascertaindomain.request", request);
		sendRequest(request, workItem);
	}

	/**
	 * Iterates though all returned domains looking for the requested domain. 
	 * 
	 * Succeeds and sets results["domainid"] if exactly one match is returned.
	 * Fails if no match or more than 1 match. 
	 */
	
	@Override
	public void onSuccess(final TaskContext ctx, final CloudstackListDomainsResponse response,
			final Map<String, Object> results) {
		List<CloudstackDomain> domains = response.getEntities();
		
		//TODO: Check exact logic here. Do we need to prefix with ROOT?
		String domainName = ctx.getRequest(CloudstackListDomainsRequest.class).getName();

		Iterator<CloudstackDomain> iter = domains.iterator();
		while (iter.hasNext())
			if (!domainName.equals(iter.next().getName()))
				iter.remove();

		if (domains.size() == 0) {
			LOG.message("orchestrator.ascertaindomain.notfound", domainName);
			lookupErrorResults(results, ctx, "orchestrator.ascertaindomain.notfound", domainName);

		} else if (domains.size() > 1) {
			LOG.message("orchestrator.ascertaindomain.manyfound", domainName);
			lookupErrorResults(results, ctx, "orchestrator.ascertaindomain.manyfound", domainName);

		} else {
			String domainId = domains.get(0).getId();
			results.put(ASCERTAIN_RESULT, domainId);
			LOG.message("orchestrator.ascertaindomain.success", domainName, domainId);
		}
	}
}
