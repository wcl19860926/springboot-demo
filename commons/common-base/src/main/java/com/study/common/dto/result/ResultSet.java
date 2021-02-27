package com.study.comcom.dto.result;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.Data;
@Data
public class ResultSet<T> {
	private List<T> data;
	private Pagination pagination;
	/**
	 * Common instance for {@code empty()}.
	 */
	private static final ResultSet<?> EMPTY = new ResultSet<>();

	/**
	 */
	private ResultSet() {
		this.data = new ArrayList<>();
		this.pagination = new Pagination();
	}

	/**
	 */
	private ResultSet(List<T> data) {
		this.data = data != null ? data : new ArrayList<>();
		this.pagination = new Pagination();
	}

	/**
	 *
	 * @param data
	 * @param pagination
	 */
	private ResultSet(List<T> data, Pagination pagination) {
		this.data = data != null ? data : new ArrayList<>();
		this.pagination = pagination == null ? new Pagination() : pagination;
	}

	public static <T> ResultSet<T> of(List<T> data, Pagination pagination) {
		return new ResultSet<>(data, pagination);
	}

	public static <T> ResultSet<T> of(List<T> data) {
		return new ResultSet<>(data);
	}

	public static <T> ResultSet<T> empty() {
		@SuppressWarnings("unchecked")
		ResultSet<T> t = (ResultSet<T>) EMPTY;
		return t;
	}

	public static <T> ResultSet<T> ofNullable(List<T> value) {
		return value == null ? empty() : of(value);
	}

	public List<T> get() {
		if (data == null) {
			throw new NoSuchElementException("No value present");
		}
		return data;
	}

	public boolean isPresent() {
		return data != null && this.data.size() > 0;
	}

	public void ifPresent(Consumer<? super List<T>> consumer) {
		if (data != null) {
			consumer.accept(data);
		}
	}

	public ResultSet<T> filter(Predicate<? super List<T>> predicate) {
		Objects.requireNonNull(predicate);
		if (!isPresent()) {
			return this;
		} else {
			return predicate.test(data) ? this : empty();
		}
	}

	public <U> ResultSet<U> flatMap(Function<? super List<T>, ResultSet<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent()) {
			return empty();
		} else {
			return Objects.requireNonNull(mapper.apply(data));
		}
	}

	public List<T> orElse(List<T> other) {
		return data != null ? data : other;
	}

	public List<T> orElseGet(Supplier<? extends List<T>> other) {
		return data != null ? data : other.get();
	}

	public <X extends Throwable> List<T> orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (data != null) {
			return data;
		} else {
			throw exceptionSupplier.get();
		}
	}



	@Override
	public String toString() {
		return data != null ? String.format("ResultSet[%s]", data) : "ResultSet.empty";
	}
}
