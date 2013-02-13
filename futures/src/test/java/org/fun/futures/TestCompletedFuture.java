package org.fun.futures;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestCompletedFuture {

	private static final ExecutorService pool = Executors.newCachedThreadPool();

	@Test
	public void test() throws Throwable {
		for (int i = 0; i < 10; i++) {
			runMapCascadeTest(1);
		}
	}

//	public void runMapOnFutureCascadeTest(int len) throws Throwable {
//
//		final CountDownLatch latch = new CountDownLatch(len);
//		final ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<Long>();
//
//		for (int i = 0; i < len; i++) {
//			final long start = System.currentTimeMillis();
//			final Future<String> f1 = Future.successful("a", pool);
//			final Future<String> f2 = Future.successful("b", pool);
//			final Future<String> f3 = Future.successful("c", pool);
//
//			Future<String> f = f1
//					.flatMapOnFuture(new Mapper<Future<String>, Future<String>>() {
//						public Future<String> apply(final Future<String> a) {
//							try {
//								Thread.sleep(500);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//
//							return f2
//									.flatMapOnFuture(new Mapper<Future<String>, Future<String>>() {
//										public Future<String> apply(
//												final Future<String> b) {
//											try {
//												Thread.sleep(500);
//											} catch (InterruptedException e) {
//												e.printStackTrace();
//											}
//
//											return f3
//													.mapOnFuture(new Mapper<Future<String>, String>() {
//														public String apply(
//																final Future<String> c) {
//															try {
//																Thread.sleep(500);
//															} catch (InterruptedException e) {
//																e.printStackTrace();
//															}
//															return a.value()
//																	+ ":"
//																	+ b.value()
//																	+ ":"
//																	+ c.value();
//														}
//													});
//										}
//									});
//						}
//					});
//
//			Future fwait = f.onComplete(new OnComplete<String>() {
//
//				@Override
//				public void onComplete(Throwable t, String value) {
//					queue.add(System.currentTimeMillis() - start);
//					latch.countDown();
//				}
//			});
//
//		}
//
//		latch.await();
//		long total = 0L;
//		for (Long val : queue)
//			total += val;
//
//		System.out.println("OnFuture Avg: " + (total / len));
//
//	}

	public void runMapCascadeTest(int len) throws Throwable {

		final CountDownLatch latch = new CountDownLatch(len);
		final ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<Long>();

		for (int i = 0; i < len; i++) {
			final long start = System.currentTimeMillis();
			final Future<String> f1 = Future.successful("a", pool);
			final Future<String> f2 = Future.successful("b", pool);
			final Future<String> f3 = Future.successful("c", pool);

			Future<String> f = f1.flatMap(new Mapper<String, Future<String>>() {
				public Future<String> apply(final String a) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					return f2.flatMap(new Mapper<String, Future<String>>() {
						public Future<String> apply(final String b) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							return f3.map(new Mapper<String, String>() {
								public String apply(final String c) {
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
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
					queue.add(System.currentTimeMillis() - start);
					latch.countDown();
				}
			});

		}

		latch.await();
		long total = 0L;
		for (Long val : queue)
			total += val;

		System.out.println("Avg: " + (total / len));

	}

}
