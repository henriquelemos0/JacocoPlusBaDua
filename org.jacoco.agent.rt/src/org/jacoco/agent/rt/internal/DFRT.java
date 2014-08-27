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
package org.jacoco.agent.rt.internal;

import org.jacoco.core.runtime.dua.DataFlowRuntimeData;

public final class DFRT {

	private static DataFlowRuntimeData DATA;

	static {
		DATA = (DataFlowRuntimeData) Agent.getInstance().getData();
		System.out.println("DFRT!");
	}

	private DFRT() {
		// No instances
	}

	public static void init() {
	}

	// metodo chamado pelo codigo instrumentado
	public static long[] getData(final String className, final int size) {
		// System.out.println("DFRT.getData(" + className + " , " + size + ")");
		// final long[] result = DATA.getExecutionData(className, size);
		// System.out.println("result: " + result[0]);

		// ID da jacoco é calculado com -> CRC64.checksum(reader.b);
		return DATA.getExecutionData((long) className.hashCode(), className,
				size).getLongProbes();
	}

}
