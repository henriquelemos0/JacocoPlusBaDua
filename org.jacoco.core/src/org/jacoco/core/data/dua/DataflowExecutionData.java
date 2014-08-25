/*******************************************************************************
 * Copyright (c) 2009, 2014 Mountainminds GmbH & Co. KG and Contributors

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.core.data.dua;

import org.jacoco.core.data.ExecutionData;

import br.usp.each.saeg.commons.BitSetIterator;
import br.usp.each.saeg.commons.BitSetUtils;

public class DataflowExecutionData extends ExecutionData {

	private final long[] longProbes;

	public DataflowExecutionData(final long id, final String name,
			final long[] probes) {
		super(id, name, new boolean[]{});
		System.out.println("DataflowExecutionData.ExecutionData(id,name,probes)");
		this.longProbes = probes;
	}

	public DataflowExecutionData(final long id, final String name,
			final int probeCount) {
		super(id, name, new boolean[]{});
		System.out.println("DataflowExecutionData.ExecutionData(id,name,probeCount)");
		this.longProbes = new long[probeCount];
	}

	@Override
	public boolean[] getProbes() {
		System.out.println("DataflowExecutionData.getProbes()");
		final boolean[] booleanProbes = new boolean[longProbes.length * 64];
		final BitSetIterator it = new BitSetIterator(
				BitSetUtils.valueOf(longProbes));
		while (it.hasNext()) {
			booleanProbes[it.next()] = true;
		}

		int cont = 0;
		System.out.println("long");
		for (int x = 0; x < longProbes.length; x++) {
			final StringBuffer buffer = new StringBuffer(
					Long.toBinaryString(longProbes[x])).reverse();
			System.out.println(buffer + " probes[" + x + "]" + " = "
					+ longProbes[x]);
		}

		System.out.println("boolean");
		for (final boolean probes : booleanProbes) {
			if (probes == true) {
				System.out.print("1");
			} else {
				System.out.print("0");
			}
			cont++;
			if (cont % 64 == 0) {
				System.out.println();
			}
		}

		System.out.println();

		this.probes = booleanProbes;
		return booleanProbes;
	}

	public long[] getLongProbes() {
		return longProbes;
	}

}
