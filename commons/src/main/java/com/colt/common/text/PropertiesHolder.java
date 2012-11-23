package com.colt.common.text;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PropertiesHolder {
	private Properties properties = new Properties();
	
	public PropertiesHolder ( String name ) {
		 try {
			Enumeration<URL> urls = this.getClass().getClassLoader().getResources(name);
			while ( urls.hasMoreElements() ) {
				URL url = urls.nextElement();
				properties.load(url.openStream());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		String ret = System.getProperty(key);
		if ( ret == null ) {
			ret = properties.getProperty(key);
		}
		return ret;
	}
	
	public  String getProperty(String key, String defaultValue) {
		String ret = getProperty(key);

		if ( ret == null ) {
			ret = defaultValue;
		}
		return ret;
	}

	public  String getStringProperty(String key, String defaultValue) {
		return getProperty(key, defaultValue);
	}

	public  int getIntProperty(String key, int defaultValue) {
		String str = getProperty(key);
		return str == null ? defaultValue : Integer.parseInt(str);
	}

	public long getLongProperty(String key, long defaultValue) {
		String str = getProperty(key);
		return str == null ? defaultValue : Long.parseLong(str);
	}

	public  boolean getBooleanProperty(String key, boolean defaultValue) {
		String str = getProperty(key);
		return str == null ? defaultValue : Boolean.valueOf(str);
	}

	public List<Property<String>> getProperties(String prefix) {
		List<Property<String>> list = new LinkedList<Property<String>>();
		Enumeration<Object> keys = properties.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			if (key.startsWith(prefix)) {
				list.add(new Property<String>(key, (String) getProperty(key)));
			}
		}
		return list;
	}
}
