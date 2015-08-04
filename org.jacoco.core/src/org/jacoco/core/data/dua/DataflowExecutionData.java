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
		this.longProbes = probes;
	}

	public DataflowExecutionData(final long id, final String name,
			final int probeCount) {
		super(id, name, new boolean[]{});
		this.longProbes = new long[probeCount];
	}

	@Override
	public boolean[] getProbes() {
		if (getName().endsWith("AnnotationMapper")){
			for (long l : longProbes) {
				System.err.println("l=" + l);
			}	
		}
		final boolean[] booleanProbes = new boolean[longProbes.length * 64];
		final BitSetIterator it = new BitSetIterator(BitSetUtils.valueOf(longProbes));
		while (it.hasNext()) {
			booleanProbes[it.next()] = true;
		}
		this.probes = booleanProbes;
		return booleanProbes;
	}

	public long[] getLongProbes() {
		return longProbes;
	}

}
