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

import br.usp.each.saeg.badua.core.runtime.RuntimeData;

public final class DFRT {

	private static RuntimeData DATA;
	static {
		DATA = DFAgent.getInstance().getData();
		System.out.println("DFRT!");
	}

	private DFRT() {
		// No instances
	}

	public static void init() {
	}

	public static long[] getData(final String className, final int size) {
		System.out.println("DFRT.getData(" + className + " , " + size + ")");
		return DATA.getExecutionData(className, size);
	}

}
