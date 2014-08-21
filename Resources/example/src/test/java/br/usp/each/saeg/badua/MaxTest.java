package br.usp.each.saeg.badua;

import static org.junit.Assert.*;
import org.junit.Test;

public class MaxTest {

	@Test
	public void test() {
		assertEquals(Max.max(new int[] { 0, 1, 10 }, 3), 10);
		assertEquals(Max.min2(new int[] { 0, 1 }, 2), 0);
	}

//	@Test
//	public void test2() {
//		assertEquals(Min.min(new int[] { 0, 1 }, 2), 0);
//	}
//
//	@Test
//	public void test3() {
//		assertEquals(Max.max(new int[] { 4, 2, 3 }, 3), 4);
//	}
}
