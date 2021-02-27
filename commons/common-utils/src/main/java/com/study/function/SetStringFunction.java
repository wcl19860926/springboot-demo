package com.study.function;

@FunctionalInterface
public interface SetStringFunction<T> {
	void setStringValue(T target, String value);
}
