package org.fun.util.rand;

/**
 * 
 * Implements the algorithm from source:
 * http://www.javamex.com/tutorials/random_numbers/xorshift.shtml#.URwXvFKxv3w
 * This algorithm was developed by: http://en.wikipedia.org/wiki/Xorshift
 * 
 */
public class XORShiftRandom {

	long x;

	public XORShiftRandom() {
		x = System.nanoTime();
	}

	public XORShiftRandom(long seed) {
		x = (seed == 0) ? System.nanoTime() : seed;
	}

	/**
	 * Returns a random number between 0 and n-1 
	 * @param n
	 * @return
	 */
	public int nextInt(int n){
		return nextInt()%n;
	}
	
	public int nextInt(){
		return (int)nextLong();
	}

	/**
	 * Returns a random number between 0 and l
	 * @param l
	 * @return
	 */
	public long nextLong(long l) {
		return nextLong()%l;
	}
	
	public long nextLong() {
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		return x;
	}
	
}
