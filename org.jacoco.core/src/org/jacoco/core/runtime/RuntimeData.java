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
package org.jacoco.core.runtime;

import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;

public class RuntimeData extends AbstractRuntimeData {

	public RuntimeData() {
		store = new ExecutionDataStore();
	}

	@Override
	public ExecutionData getExecutionData(final Long id, final String name,
			final int probecount) {
		synchronized (store) {
			System.out.println("getExecutionData: "+name);
			return (ExecutionData) store.get(id, name, probecount);
		}
	}

}
