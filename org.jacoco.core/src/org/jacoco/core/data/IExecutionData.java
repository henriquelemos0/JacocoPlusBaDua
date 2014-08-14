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

public interface IExecutionData {

	public long getId();

	public String getName();

	public boolean[] getProbes();

	public void reset();

	public void merge(final IExecutionData other);

	public void merge(final IExecutionData other, final boolean flag);

	public void assertCompatibility(final long id, final String name,
			final int probecount) throws IllegalStateException;

}
