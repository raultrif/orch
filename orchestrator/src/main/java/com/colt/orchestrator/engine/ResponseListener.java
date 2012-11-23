package com.colt.orchestrator.engine;

import java.io.IOException;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.XmlMappingException;

import com.colt.common.logger.Logger;

public class ResponseListener<O> {
	private static final Logger LOG = Logger.getLogger(ResponseListener.class);

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private TaskContextService taskContextService;

	@SuppressWarnings("unchecked")
	@Handler
	public void onResponse(@Headers Map<String, Object> headers, @Body O response) throws XmlMappingException,
			IOException {
		TaskContext ctx = taskContextService.fromHeaders(headers);
		LOG.message("orchestrator.responselistener.onresponse", ctx.handlerName, ctx.workItemId);
		ResponseHandler<O> handler = (ResponseHandler<O>) appContext.getBean(ctx.handlerName);
		handler.handleResponse(ctx, response);
	}
}
