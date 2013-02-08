package org.fun.futures;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;


public class TestFuture {

	
	static final ForkJoinPool pool = new ForkJoinPool(10000);
	
	@Test
	public void testAll(){
		final AtomicBoolean completed = new AtomicBoolean(false);
		
		final Future<String> f1 = Future.future(new Callable<String>(){
			public String call(){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return "a";
			}
		}, pool);
		
		final Future<String> f2 = Future.future(new Callable<String>(){
			public String call(){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return "b";
			}
		}, pool);
		final Future<String> f3 = Future.future(new Callable<String>(){
			public String call(){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return "c";
			}
		}, pool);
		
		
		Future<String> f = f1.flatMap(new Mapper<String, Future<String>>(){
			public Future<String> apply(final String a){
				return f2.flatMap(new Mapper<String, Future<String>>(){
					public Future<String> apply(final String b){
						return f3.map(new Mapper<String, String>(){
							public String apply(final String c){
								return a + ":" + b + ":" + c;
							}
						});
					}
				});
			}
		});
		
		Future fwait = f.onComplete(new OnComplete<String>() {
			
			@Override
			public void onComplete(Throwable t, String value) {
				completed.set(true);
			}
		});
		
		fwait.await();
		assertEquals(true, completed.get());
		System.out.println("Output: " + f.value());
		assertEquals("a:b:c", f.value());
		
	}
	
}
