package br.usp.each.saeg.badua;

import static org.junit.Assert.*;
import org.junit.Test;

public class MaxTest {

//	@Test
//	public void test() {
//		assertEquals(0, Max.min2(new int[] { 0, 1 }, 2));
//	}
//	
//	@Test
//	public void test2() {
//		assertEquals(Min.min(new int[] { 0, 1 }, 2), 0);
//	}
//
//	@Test
//	public void test3() {
//		assertEquals(Max.max(new int[] { 0, 1 }, 2), 1);
//	}
	
	@Test
	public void test4() {
		assertEquals( 20, Max.max(new int[] { 20, 0, 1, 10 }, 4));
	}
//	
//	@Test
//	public void test5() {
//		assertEquals( 10, Max.max(new int[] { 2, 10, 1, }, 3));
//	}
//	
//	@Test
//	public void test6() {
//		assertEquals( 5, Max.max(new int[] { 5, 5, 5}, 3));
//	}
	@Test
	public void test7() {
		assertEquals( 3, Max.max(new int[] { 1, 2, 3}, 3));
	}
}
