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
package org.jacoco.core.runtime.dua;

import org.jacoco.core.data.dua.DataflowExecutionData;
import org.jacoco.core.data.dua.DataflowExecutionDataStore;
import org.jacoco.core.runtime.AbstractRuntimeData;

public class DataFlowRuntimeData extends AbstractRuntimeData {

	public DataFlowRuntimeData() {
		store = new DataflowExecutionDataStore();
	}

	@Override
	public DataflowExecutionData getExecutionData(final Long id,
			final String name, final int probecount) {
		synchronized (store) {
			System.out.println("RuntimeData.getExecutionData - call get()");
			return (DataflowExecutionData) store.get(id, name, probecount);
		}
	}

}
