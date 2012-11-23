package com.colt.common.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QueryStringHelper {
	private static final String CHARACTER_ENCODING = "UTF-8";

	/**
	 * URL-encodes, using %20 for space rather than +
	 * 
	 * @param value
	 * @return
	 */

	public static String encode(Object value) {
		try {
			String s = (value == null) ? "" : value.toString();
			return URLEncoder.encode(s, CHARACTER_ENCODING).replaceAll("\\+",
					"%20");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * Adds properties to a map in alphabetical order. Shallow.
	 * 
	 * @param properties
	 * @return
	 */

	public static String mapToQueryString(Map<String, Object> properties) {
		StringBuilder resp = new StringBuilder();
		List<String> names = new LinkedList<String>(properties.keySet());

		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String left, String right) {
				return left.compareToIgnoreCase(right);
			}
		});

		String separator = "";
		for (String name : names) {
			Object value = properties.get(name);
			if (value != null) {
				resp.append(separator).append(encode(name)).append("=")
						.append(encode(value));
				separator = "&";
			}
		}
		return resp.toString();
	}

	/**
	 * Appends a name/value pair to a query string.
	 * 
	 * @param queryString
	 * @param name
	 * @param value
	 * @return
	 */

	public static String appendToQueryString(String queryString, String name,
			String value) {
		if (queryString == null || queryString.length() == 0) {
			queryString = "";
		} else {
			queryString += "&";
		}
		return queryString + encode(name) + "=" + encode(value);
	}
}
