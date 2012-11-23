package com.colt.common.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.colt.common.logger.Logger;

public class BeanHelper {
	private static final Logger LOG = Logger.getLogger(BeanHelper.class);

	/**
	 * Extracts all properties of a bean into a new map
	 * 
	 * @param bean
	 * @param map
	 * @return
	 */
	public static  Map<String, Object> beanToMap(Map<String, Object>map, Object bean) {
		beanToMap(bean, map);
		return map;
	}

	/**
	 * Extracts all properties of a bean into an existing map
	 * value
	 * 
	 * @param bean
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  Map<String, Object> beanToMap(Object bean, Map<String, Object >map) {
		try {
			map.putAll(PropertyUtils.describe(bean));
			map.remove("class");
		} catch (Exception e) {
			LOG.message(e, "error %s", e.getMessage());
		}
		return map;
	}
	
	public static  Map<String, Object> beanToMap(Object bean) {
		Map<String, Object>results = new HashMap<String, Object>();
		return beanToMap(bean, results);
	}

	/**
	 * Populates a bean from matching properties in a map
	 * 
	 * @param map
	 * @param bean
	 * @return
	 */
	public static Object mapToBean(@SuppressWarnings("rawtypes") Map map, Object bean) {
		try {
			BeanUtils.populate(bean, map);
		} catch (Exception e) {
			LOG.message(e, "error %s", e.getMessage());
		}
		return bean;
	}
}
