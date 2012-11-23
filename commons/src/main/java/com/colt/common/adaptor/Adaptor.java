package com.colt.common.adaptor;

import static com.colt.common.context.Constants.ORCH_HDR_PREFIX;

import java.util.HashMap;
import java.util.Map;

public class Adaptor {
	/**
	 * Copies all headers starting with ORCH_HDR_PREFIX from one map to another
	 * 
	 * @param oldHeaders
	 * @param newHeaders
	 */

	protected Map<String, Object> getOrchHeaders(Map<String, Object> oldHeaders) {
		Map<String, Object> newHeaders = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : oldHeaders.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(ORCH_HDR_PREFIX)) {
				newHeaders.put(key, entry.getValue());
			}
		}
		return newHeaders;
	}

}
