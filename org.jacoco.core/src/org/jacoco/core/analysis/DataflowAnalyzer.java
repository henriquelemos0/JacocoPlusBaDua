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
package org.jacoco.core.analysis;

import org.jacoco.core.data.AbstractExecutionDataStore;
import org.jacoco.core.data.IExecutionData;
import org.jacoco.core.internal.analysis.ClassAnalyzer;
import org.jacoco.core.internal.flow.ClassProbesAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

/**
 * An {@link DataflowAnalyzer} instance processes a set of Java class files and
 * calculates coverage data for them. For each class file the result is reported
 * to a given {@link ICoverageVisitor} instance. In addition the
 * {@link DataflowAnalyzer} requires a {@link AbstractExecutionDataStore}
 * instance that holds the execution data for the classes to analyze. The
 * {@link DataflowAnalyzer} offers several methods to analyze classes from a
 * variety of sources.
 */
public class DataflowAnalyzer extends AbstractAnalyzer {

	@SuppressWarnings("javadoc")
	public DataflowAnalyzer(final AbstractExecutionDataStore executionData,
			final ICoverageVisitor coverageVisitor) {
		super(executionData, coverageVisitor);
	}

	@Override
	protected ClassVisitor createAnalyzingVisitor(final long classid,
			final String className) {
		// ************************************************************************************
		System.out
				.println("Analyzer.createAnalyzingVisitor(classid,className) -> "
						+ className);
		System.out.println(className.hashCode());
		final IExecutionData dataTest = executionData.get(className.hashCode());
		if (dataTest != null) {
			final boolean[] booleanProbes = dataTest.getProbes();
			int cont = 0;
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
		} else {
			System.out.println("dataTest = null -> " + className);
		}
		// ***********************************************************************************
		final IExecutionData data = executionData.get(classid);
		final boolean[] probes;
		final boolean noMatch;
		if (data == null) {
			probes = null;
			noMatch = executionData.contains(className);
		} else {
			probes = data.getProbes();
			noMatch = false;
		}
		final ClassAnalyzer analyzer = new ClassAnalyzer(classid, noMatch,
				probes, stringPool) {
			@Override
			public void visitEnd() {
				super.visitEnd();
				coverageVisitor.visitCoverage(getCoverage());
			}
		};
		return new ClassProbesAdapter(analyzer, false);
	}

	/**
	 * Analyzes the class given as a ASM reader.
	 * 
	 * @param reader
	 *            reader with class definitions
	 */
	@Override
	public void analyzeClass(final ClassReader reader) {
		final ClassVisitor visitor = createAnalyzingVisitor(reader
				.getClassName().hashCode(), reader.getClassName());
		reader.accept(visitor, 0);
	}
}
