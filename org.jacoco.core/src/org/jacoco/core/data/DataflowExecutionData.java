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
package org.jacoco.core.data;

import static java.lang.String.format;

import java.util.Arrays;

import br.usp.each.saeg.commons.BitSetIterator;
import br.usp.each.saeg.commons.BitSetUtils;

public class DataflowExecutionData implements IExecutionData {

	private final long id;

	private final String name;

	private final long[] probes;

	public DataflowExecutionData(final long id, final String name,
			final long[] probes) {
		System.out.println("ExecutionData.ExecutionData(id,name,probes)");
		this.id = id;
		this.name = name;
		this.probes = probes;
	}

	public DataflowExecutionData(final long id, final String name,
			final int probeCount) {
		System.out.println("ExecutionData.ExecutionData(id,name,probeCount)");
		this.id = id;
		this.name = name;
		this.probes = new long[probeCount];
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean[] getProbes() {
		final boolean[] booleanProbes = new boolean[probes.length * 64];
		final BitSetIterator it = new BitSetIterator(
				BitSetUtils.valueOf(probes));
		while (it.hasNext()) {
			booleanProbes[it.next()] = true;
		}

		int cont = 0;
		System.out.println("\n" + name);
		System.out.println("long");
		for (int x = 0; x < probes.length; x++) {
			final StringBuffer buffer = new StringBuffer(
					Long.toBinaryString(probes[x])).reverse();
			System.out.println(buffer + " probes[" + x + "]" + " = "
					+ probes[x]);
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

		return booleanProbes;
	}

	public long[] getLongProbes() {
		return probes;
	}

	public void reset() {
		// TODO Auto-generated method stub
		// Arrays.fill(probes, false); se fosse boolean
		Arrays.fill(probes, 0);
	}

	public void merge(final IExecutionData other) {
		// TODO Auto-generated method stub
		merge(other, true);
	}

	public void merge(final IExecutionData other, final boolean flag) {
		// AINDA NAO SEI O QUE ISSO FAZ
		// assertCompatibility(other.getId(), other.getName(),
		// other.getProbes().length);
		// final boolean[] otherData = other.getProbes();
		// for (int i = 0; i < probes.length; i++) {
		// if (otherData[i]) {
		// probes[i] = flag;
		// }
		// }
	}

	public void assertCompatibility(final long id, final String name,
			final int probecount) throws IllegalStateException {
		if (this.id != id) {
			throw new IllegalStateException(format(
					"Different ids (%016x and %016x).", Long.valueOf(this.id),
					Long.valueOf(id)));
		}
		if (!this.name.equals(name)) {
			throw new IllegalStateException(format(
					"Different class names %s and %s for id %016x.", this.name,
					name, Long.valueOf(id)));
		}
		if (this.probes.length != probecount) {
			throw new IllegalStateException(format(
					"Incompatible execution data for class %s with id %016x.",
					name, Long.valueOf(id)));
		}

	}

	@Override
	public String toString() {
		return String.format("ExecutionData[name=%s, id=%016x]", name,
				Long.valueOf(id));
	}

}
