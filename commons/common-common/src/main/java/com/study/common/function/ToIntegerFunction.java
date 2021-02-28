package com.study.common.function;

@FunctionalInterface
public interface ToIntegerFunction<T> {


    Integer applyAsInteger(T t);

}
