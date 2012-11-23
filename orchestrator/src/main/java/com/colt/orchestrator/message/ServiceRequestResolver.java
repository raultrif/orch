package com.colt.orchestrator.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.colt.orchestrator.engine.NotFoundException;

@SuppressWarnings("unchecked")
public class ServiceRequestResolver {
	private static Map<String, Class<? extends ServiceRequest>> requestClasses = new HashMap<String, Class<? extends ServiceRequest>>();
	private static Map<String, Class<? extends ServiceResponse>> responseClasses = new HashMap<String, Class<? extends ServiceResponse>>();

	static {
		Reflections reflections = new Reflections(ServiceRequestResolver.class.getPackage().getName());

		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ServiceRequestDescription.class);

		for (Class<?> clazz : classes) {
			ServiceRequestDescription annotation = clazz.getAnnotation(ServiceRequestDescription.class);
			requestClasses.put(annotation.command(), (Class<? extends ServiceRequest>) clazz);
		}
		classes = reflections.getTypesAnnotatedWith(ServiceResponseDescription.class);

		for (Class<?> clazz : classes) {
			ServiceResponseDescription annotation = clazz.getAnnotation(ServiceResponseDescription.class);
			responseClasses.put(annotation.command(), (Class<? extends ServiceResponse>) clazz);
		}
	}

	public static Class<? extends ServiceRequest> getRequestClass(String command) {
		return requestClasses.get(command);
	}

	public static ServiceRequest getRequestInstance(String command) throws NotFoundException, InstantiationException,
			IllegalAccessException {
		Class<? extends ServiceRequest> clazz = getRequestClass(command);
		if (clazz == null) {
			throw new NotFoundException("Request not found for " + command);
		}
		return clazz.newInstance();
	}

	public static Class<? extends ServiceResponse> getResponseClass(String command) {
		return responseClasses.get(command);
	}

	public static ServiceResponse getResponseInstance(String command) throws NotFoundException, InstantiationException,
			IllegalAccessException {
		Class<? extends ServiceResponse> clazz = getResponseClass(command);
		if (clazz == null) {
			throw new NotFoundException("Response not found for " + command);
		}
		return clazz.newInstance();
	}

}
