package org.fun.futures;

public abstract class OnComplete<T> {

	
	public abstract void onComplete(Throwable t, T value);
	
	
}
