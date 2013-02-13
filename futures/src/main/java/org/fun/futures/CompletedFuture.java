package org.fun.futures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * Contains the result of a completed calculation/function.
 * 
 * @param <T>
 */
public class CompletedFuture<T> extends Future<T> {

	final T value;

	public CompletedFuture(T value, ExecutorService pool) {
		super(null, pool);
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T unroll() {
		if (value instanceof Future) {
			return ((Future<T>) value).unroll();
		} else {
			return value;
		}
	}

	@Override
	public T value() {
		return unroll();
	}

	@Override
	public Future<T> onComplete(OnComplete<T> c) {
		c.onComplete(null, value);
		return new CompletedFuture<T>(null, pool);
	}

	@Override
	public void await() {
		// do nothing
	}

	public void await(long time, TimeUnit unit) throws TimeoutException {
		// do nothing
	}

}
