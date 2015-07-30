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
package org.jacoco.core.internal.analysis.dua;

import org.jacoco.core.analysis.ISourceNode;
import org.jacoco.core.analysis.dua.CoverageDuaImpl;
import org.jacoco.core.analysis.dua.ISourceDua;
import org.jacoco.core.internal.analysis.LineImpl;

/**
 * Implementation of {@link ISourceNode}.
 */
public class SourceDuaImpl extends CoverageDuaImpl implements ISourceDua {

	private LineImpl[] lines;

	/** first line number in {@link #lines} */
	private int offset;

	/**
	 * Create a new source node implementation instance.
	 * 
	 * @param elementType
	 *            element type
	 * @param name
	 *            name of the element
	 */
	public SourceDuaImpl(final ElementType elementType, final String name) {
		super(elementType, name);
		lines = null;
		offset = UNKNOWN_LINE;
	}

	/**
	 * Make sure that the internal buffer can keep lines from first to last.
	 * While the buffer is also incremented automatically, this method allows
	 * optimization in case the total range in known in advance.
	 * 
	 * @param first
	 *            first line number or {@link ISourceNode#UNKNOWN_LINE}
	 * @param last
	 *            last line number or {@link ISourceNode#UNKNOWN_LINE}
	 */
	public void ensureCapacity(final int first, final int last) {
		if (first == UNKNOWN_LINE || last == UNKNOWN_LINE) {
			return;
		}
		if (lines == null) {
			offset = first;
			lines = new LineImpl[last - first + 1];
		} else {
			final int newFirst = Math.min(getFirstLine(), first);
			final int newLast = Math.max(getLastLine(), last);
			final int newLength = newLast - newFirst + 1;
			if (newLength > lines.length) {
				final LineImpl[] newLines = new LineImpl[newLength];
				System.arraycopy(lines, 0, newLines, offset - newFirst,
						lines.length);
				offset = newFirst;
				lines = newLines;
			}
		}
	}

	/**
	 * Increments all counters by the values of the given child. When
	 * incrementing the line counter it is assumed that the child refers to the
	 * same source file.
	 * 
	 * @param child
	 *            child node to add
	 */
	public void increment(final ISourceDua child) {
		duaCounter = duaCounter.increment(child
				.getDuaCounter());
		methodCounter = methodCounter.increment(child.getMethodCounter());
		classCounter = classCounter.increment(child.getClassCounter());
		final int firstLine = child.getFirstLine();
		if (firstLine != UNKNOWN_LINE) {
			final int lastLine = child.getLastLine();
			ensureCapacity(firstLine, lastLine);
		}
	}

	// === ISourceNode implementation ===

	public int getFirstLine() {
		return offset;
	}

	public int getLastLine() {
		return lines == null ? UNKNOWN_LINE : (offset + lines.length - 1);
	}

	public LineImpl getLine(final int nr) {
		if (lines == null || nr < getFirstLine() || nr > getLastLine()) {
			return LineImpl.EMPTY;
		}
		final LineImpl line = lines[nr - offset];
		return line == null ? LineImpl.EMPTY : line;
	}

}
