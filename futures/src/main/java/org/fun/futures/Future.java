package org.fun.futures;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * 
 * Represents a task that will be completed sometime in the future.
 * 
 * @param <T>
 */
public class Future<T> {

	private final ExecutorService pool;
	private final java.util.concurrent.Future<T> f;
	
	public Future(final Callable<T> c, ExecutorService pool){
		this.pool = pool;

		f = pool.submit(c);
	}
	
	@SuppressWarnings("unchecked")
	public T unroll(){
		T val;
		try {
			val = f.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return null;
		} catch (ExecutionException e) {
			RuntimeException rte = new RuntimeException(e.toString(), e);
			rte.setStackTrace(e.getStackTrace());
			throw rte;
		}
		
		if(val instanceof Future){
			return ((Future<T>)val).unroll();
		}else{
			return val;
		}
	}
	
	public T value(){
		return unroll();
	}
	
	public Future<T> onComplete(final OnComplete<T> c){
		return new Future<T>(new Callable<T>(){ public T call(){
			Throwable t = null;
			T value = null;
			try{
				value = unroll();
			}catch(Throwable te){
				t = te;
			}
			
			c.onComplete(t, value);
			return null;
			
		}}, pool);
	}
	
	public <A> Future<A> map(final Mapper<T, A> mapper){
		return new Future<A>(new Callable<A>(){ public A call(){ return mapper.apply(unroll()); }}, pool);
	}
	
	public <A> Future<A> flatMap(final Mapper<T, Future<A>> mapper){
		return new Future<A>(new Callable<A>(){ public A call(){ return mapper.apply(unroll()).unroll(); }}, pool);
	}
	
	public static <T> Future<T> future(Callable<T> f, ExecutorService pool){
	  return new Future<T>(f, pool);
	}

	public static <T> Future<T> successful(final T value, ExecutorService pool){
		return future(new Callable<T>(){public T call(){ return value; } }, pool);
	}

	public void await() {
		try {
			f.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		} catch (ExecutionException e) {
		    RuntimeException rte = new RuntimeException(e.toString(), e);
		    rte.setStackTrace(e.getStackTrace());
		    throw rte;
		}
	}
	
}
