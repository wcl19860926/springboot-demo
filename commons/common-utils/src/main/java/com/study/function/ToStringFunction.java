package com.study.function;

@FunctionalInterface
public interface ToStringFunction<T> {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param value the function argument
	 * @return the function result
	 */
	String applyAsString(T value);
}
