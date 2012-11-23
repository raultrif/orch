package com.colt.common.text;

public class Property<T> {
	String name;
	T value;

	public Property(String name, T value) {
		this.name = name;
		this.value = value;
	}
}
