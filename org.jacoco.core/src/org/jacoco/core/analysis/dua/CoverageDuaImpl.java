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
package org.jacoco.core.analysis.dua;

import java.util.Collection;

import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.internal.analysis.CounterImpl;

/**
 * Base implementation for coverage data nodes.
 */
public class CoverageDuaImpl implements ICoverageDua {

	private final ElementType elementType;

	private final String name;

	/** Counter for duas. */
	protected CounterImpl duaCounter;

	/** Counter for methods. */
	protected CounterImpl methodCounter;

	/** Counter for classes. */
	protected CounterImpl classCounter;

	/**
	 * Creates a new coverage data node.
	 * 
	 * @param elementType
	 *            type of the element represented by this instance
	 * @param name
	 *            name of this node
	 */
	public CoverageDuaImpl(final ElementType elementType, final String name) {
		this.elementType = elementType;
		this.name = name;
		this.duaCounter = CounterImpl.COUNTER_0_0;
		this.methodCounter = CounterImpl.COUNTER_0_0;
		this.classCounter = CounterImpl.COUNTER_0_0;
	}

	/**
	 * Increments the counters by the values given by another element.
	 * 
	 * @param child
	 *            counters to add
	 */
	public void increment(final ICoverageDua child) {
		duaCounter = duaCounter.increment(child
				.getDuaCounter());
		methodCounter = methodCounter.increment(child.getMethodCounter());
		classCounter = classCounter.increment(child.getClassCounter());
	}

	/**
	 * Increments the counters by the values given by the collection of
	 * elements.
	 * 
	 * @param children
	 *            list of nodes, which counters will be added to this node
	 */
	public void increment(final Collection<? extends ICoverageDua> children) {
		for (final ICoverageDua child : children) {
			increment(child);
		}
	}

	// === ICoverageDataNode ===

	public ElementType getElementType() {
		return elementType;
	}

	public String getName() {
		return name;
	}

	public ICounter getDuaCounter() {
		return duaCounter;
	}

	public ICounter getMethodCounter() {
		return methodCounter;
	}

	public ICounter getClassCounter() {
		return classCounter;
	}

	public ICounter getCounter(final CounterEntity entity) {
		switch (entity) {
		case DUA:
			return getDuaCounter();
		case METHOD:
			return getMethodCounter();
		case CLASS:
			return getClassCounter();
		}
		throw new AssertionError(entity);
	}

	public ICoverageDua getPlainCopy() {
		final CoverageDuaImpl copy = new CoverageDuaImpl(elementType, name);
		copy.duaCounter = CounterImpl.getInstance(duaCounter);
		copy.methodCounter = CounterImpl.getInstance(methodCounter);
		copy.classCounter = CounterImpl.getInstance(classCounter);
		return copy;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(name).append(" [").append(elementType).append("]");
		return sb.toString();
	}

}
