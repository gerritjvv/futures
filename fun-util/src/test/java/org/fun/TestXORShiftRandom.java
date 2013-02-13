package org.fun;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.fun.util.rand.XORShiftRandom;
import org.junit.Test;

public class TestXORShiftRandom {

	
	@Test
	public void testRand() throws Throwable{

		 XORShiftRandom rand = new XORShiftRandom();
		 Set<Integer> set = new HashSet<Integer>();
		 
		 int len = 100;
		 for(int i = 0; i < len; i++){
			 System.out.println(Math.abs(rand.nextInt()%3));
			 set.add(rand.nextInt());
		 }
	
		 
		 assertEquals(len, set.size());
		 
	}
	
	
}
