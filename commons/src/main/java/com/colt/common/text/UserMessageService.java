package com.colt.common.text;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UserMessageService {

	private ResourceBundle bundle;

	public UserMessageService(String name) {
		bundle = PropertyResourceBundle.getBundle(name);
	}

	public String format(String key, String... args) {
		String format = bundle.getString(key);
		String message = String.format(format, (Object[])args);
		return message;
	}
}
