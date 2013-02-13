package org.fun.util.rand;

/**
 * 
 * Implements the algorithm from source:
 * http://www.javamex.com/tutorials/random_numbers/xorshift.shtml#.URwXvFKxv3w
 * This algorithm was developed by: http://en.wikipedia.org/wiki/Xorshift
 * 
 * Is thread safe and uses a ThreadLocal to keep the x values.
 */
public class ConcurrentXORShiftRandom {

	final long seed;
	final ThreadLocal<Long> x = new ThreadLocal<Long>();

	public ConcurrentXORShiftRandom() {
		seed = genSeed();
	}

	public ConcurrentXORShiftRandom(long seed) {
		this.seed = (seed == 0) ? genSeed() : seed;
	}

	private static final long genSeed(){
		long x2 = System.nanoTime();
		x2 ^= (x2 << 21);
		x2 ^= (x2 >>> 35);
		x2 ^= (x2 << 4);
		
		return x2;
	}
	
	/**
	 * Returns a random number between 0 and n-1 
	 * @param n
	 * @return
	 */
	public int nextInt(int n){
		return nextInt()%n;
	}
	
	public final int nextInt(){
		return (int)nextLong();
	}

	/**
	 * Returns a random number between 0 and l-1
	 * @param l
	 * @return
	 */
	public long nextLong(long l) {
		return nextLong()%l;
	}
	
	public final long nextLong() {
		final Long xObj = x.get();
		long x2;
		if(xObj == null){
			x.set(seed);
			x2 = seed;
		}else
			x2 = xObj.longValue();
		
		x2 ^= (x2 << 21);
		x2 ^= (x2 >>> 35);
		x2 ^= (x2 << 4);
		
		x.set(x2);
		
		return x2;
	}
	
}
