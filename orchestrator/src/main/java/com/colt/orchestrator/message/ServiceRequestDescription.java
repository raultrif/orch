package com.colt.orchestrator.message;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceRequestDescription {
	String command();
}
