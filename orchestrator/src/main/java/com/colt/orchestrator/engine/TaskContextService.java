package com.colt.orchestrator.engine;

import static com.colt.common.context.Constants.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import com.colt.common.xml.XmlMarshaller;

@Service
public class TaskContextService {

	@Autowired
	private XmlMarshaller xmlMarshaller;

	public TaskContext fromHeaders(Map<String, Object> headers) throws XmlMappingException, IOException {
		TaskContext ctx = xmlMarshaller.unmarshal((String) headers.get(CONTEXT_HDR), TaskContext.class);
		ctx.platform = (String) headers.get(PLATFORM_HDR);
		return ctx;
	}

	public Map<String, Object> toHeaders(TaskContext ctx) throws XmlMappingException, IOException {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(CONTEXT_HDR, xmlMarshaller.marshal(ctx));
		return headers;
	}
}
