package com.colt.common.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMessage {
	public Class<?> responseClass();
	public Class<?> asyncResponseClass() default java.lang.Void.class;
}
