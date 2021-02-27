package com.study.common.dto.result;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.study.common.error.code.ErrorCode;

import lombok.Data;

@Data

public final class Result<T> {
	/**
	 * If non-null, the value; if null, indicates no value is present
	 */
	private T value;
	/**
	 * for error return
	 */
	private ErrorCode errorCode;
	/**
	 * for error args
	 */
	private Object[] args;

	/**
	 * Common instance for {@code empty()}.
	 */
	private static final Result<?> EMPTY = new Result<>();

	private Result() {
		this.value = null;
	}

	private Result(T data) {
		this.value = data;
	}

	private Result(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	private Result(ErrorCode errorCode, Object... args) {
		this.errorCode = errorCode;
		this.args = args;
	}

	private Result(T value, ErrorCode errorCode, Object... args) {
		this.value = value;
		this.errorCode = errorCode;
		this.args = args;
	}

	/**
	 * 判断是否有异常返回
	 */
	public boolean isSuccess() {
		if (this.errorCode == null) {
			return Boolean.TRUE;
		}
		return this.errorCode.getCode() == 0;
	}

	public static <T> Result<T> ofError(ErrorCode errorCode, Object... args) {
		return new Result<>(errorCode, args);
	}

	public static <T> Result<T> ofError(ErrorCode errorCode) {
		return new Result<>(errorCode);
	}

	@SuppressWarnings("unchecked")
	public static <T> Result<T> empty() {
		return (Result<T>) EMPTY;
	}

	public static <T> Result<T> of(T value) {
		return new Result<>(value);
	}

	public static <T> Result<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}

	public T get() {
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}

	public boolean isPresent() {
		return value != null;
	}

	public void ifPresent(Consumer<? super T> consumer) {
		if (value != null) {
			consumer.accept(value);
		}
	}

	public Result<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (!isPresent()) {
			return this;
		} else {
			return predicate.test(value) ? this : empty();
		}
	}

	public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent()) {
			return empty();
		} else {
			return Result.ofNullable(mapper.apply(value));
		}
	}

	public <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent()) {
			return empty();
		} else {
			return Objects.requireNonNull(mapper.apply(value));
		}
	}

	public T orElse(T other) {
		return value != null ? value : other;
	}

	public T orElseGet(Supplier<? extends T> other) {
		return value != null ? value : other.get();
	}

	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (value != null) {
			return value;
		} else {
			throw exceptionSupplier.get();
		}
	}

	@Override
	public String toString() {
		return value != null ? String.format("Result[%s]", value) : "Result.empty";
	}

}