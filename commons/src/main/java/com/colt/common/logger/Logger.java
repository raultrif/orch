package com.colt.common.logger;

import org.apache.log4j.Level;

import com.colt.common.text.PropertiesHolder;

/**
 * Thin wrapper around the log4j wrapper that uses a ResourceBundle to determine messages and level 
 * and supports String format parameters.
 * 
 * Usage:
 * <pre>
 * import com.colt.common.loggger.Logger;
 * Logger logger = Logger.getInstance(Some.class.getName());
 * logger.message ("some.key", param1, param2);
 * 
 * W
 * </pre>
 *
 * @author richardevans
 *
 */
public class Logger {
	private static class Holder {
		static PropertiesHolder properties = new PropertiesHolder("log-messages.properties");
	}

	private org.apache.log4j.Logger log;

	public Logger(String category) {
		log = org.apache.log4j.Logger.getLogger(category);
	}

	public void message(String key, Object... args) {
		this.message(null, key, args);
	}

	public void message(Exception e, String key, Object... args) {
		String message, format, setting, levelName;
		try {
			setting = Holder.properties.getProperty(key);
			int separatorPos = setting.indexOf(":");
			format = setting.substring(separatorPos + 1);
			levelName = setting.substring(0, separatorPos);
		} catch (Exception ex) {
			format = key;
			levelName = "DEBUG";
		}
		Level level = Level.toLevel(levelName);
		message = String.format(format, args).replaceAll("[\r\n]+", " ");
		log.log(level, message, e);
	}
	
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class clazz) {
		return new Logger(clazz.getName());
	}

	public static Logger getLogger(String category) {
		return new Logger(category);
	}
}
