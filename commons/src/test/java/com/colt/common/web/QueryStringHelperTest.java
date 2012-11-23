package com.colt.common.web;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.colt.common.logger.Logger;

public class QueryStringHelperTest {
	Logger LOG = Logger.getLogger(QueryStringHelperTest.class);

	@Before
	public void setup() {
	}

	@Test
	public void testMapToQueryString() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("name", "surname");
		properties.put("value", null);
		properties.put("command", "someCommand");

		String queryString = QueryStringHelper.mapToQueryString(properties);
		assertEquals("command=someCommand&name=surname", queryString);
	}

	@Test
	public void testAppendToQueryString() {

		String queryString;
		queryString = QueryStringHelper.appendToQueryString(null, "name", "value");
		assertEquals("name=value", queryString);
		queryString = QueryStringHelper.appendToQueryString("", "name", "value");
		assertEquals("name=value", queryString);
		queryString = QueryStringHelper.appendToQueryString("x=y", "name", "value");
		assertEquals("x=y&name=value", queryString);
	}

	@Test
	public void testEncode() {

		String queryString;
		queryString = QueryStringHelper.encode("abc");
		assertEquals("abc", queryString);
		queryString = QueryStringHelper.encode("a bc");
		assertEquals("a%20bc", queryString);
		queryString = QueryStringHelper.encode("a=b&c");
		assertEquals("a%3Db%26c", queryString);
	}

}