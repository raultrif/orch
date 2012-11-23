package com.colt.common.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonHelper {
	
	public static <T> T unmarshal ( String json, Class<T>clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.UNWRAP_ROOT_VALUE, true);
		return mapper.readValue(json, clazz);
	}
}
