package com.colt.common.beans;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.colt.common.logger.Logger;

public class BeanHelperTest {
	Logger LOG = Logger.getLogger(BeanHelperTest.class);
	private static final String NAME = "surname";
	private static final String VALUE = "de la fuente";
	private static final String COMMAND = "someCommand";

	@Test
	public void testBeanToMap() {
		Bean bean = new Bean(NAME, VALUE);
		Map<String, Object> map = new HashMap<String, Object>();
		BeanHelper.beanToMap(bean, map);
		assertEquals(NAME, map.get("name"));
		assertEquals(VALUE, map.get("value"));
		assertEquals(COMMAND, map.get("command"));
	}

	@Test
	public void testMapToBean() {
		Bean bean = new Bean();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", NAME);
		map.put("value", VALUE);

		BeanHelper.mapToBean(map, bean);
		assertEquals(NAME, bean.getName());
		assertEquals(VALUE, bean.getValue());
	}

	public static class Bean {
		private static String command = COMMAND;
		private String name;
		private String value;

		public Bean() {
		}

		public Bean(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCommand() {
			return command;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}