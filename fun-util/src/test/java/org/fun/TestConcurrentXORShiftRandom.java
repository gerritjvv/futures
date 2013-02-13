package org.fun;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fun.util.rand.ConcurrentXORShiftRandom;
import org.junit.Test;

public class TestConcurrentXORShiftRandom {

	private static final ConcurrentXORShiftRandom rand = new ConcurrentXORShiftRandom();

	@Test
	public void testRand() throws Throwable {

		final int len = 100;
		final CountDownLatch latch = new CountDownLatch(len);
		final ConcurrentLinkedQueue<Boolean> okqueue = new ConcurrentLinkedQueue<Boolean>();

		ExecutorService service = Executors.newCachedThreadPool();

		for (int i = 0; i < len; i++) {
			service.submit(new Runnable() {
				public void run() {
					Set<Integer> set = new HashSet<Integer>();
					try {
						int len = 100;
						for (int i = 0; i < len; i++)
							set.add(rand.nextInt());
					} finally {
						System.out.println(Arrays.toString(set.toArray()));
						okqueue.add(len == set.size());
						latch.countDown();
					}
				}
			});
		}
		
		latch.await();
		for(Boolean ok : okqueue)
			assertTrue(ok);

	}

}
