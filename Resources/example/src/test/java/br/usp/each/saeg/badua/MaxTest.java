package br.usp.each.saeg.badua;

import static org.junit.Assert.*;
import org.junit.Test;

public class MaxTest {

	@Test
	public void test(){
		assertEquals(Max.max(new int[]{0,1,10}, 3),10);	
	}
	@Test
	public void test2(){
		assertEquals(Max.max(new int[]{0,1}, 1),1);
	}
	
	@Test
	public void test3(){
		assertEquals(Max.max(new int[]{4,2,3}, 3),3);
	}
}
