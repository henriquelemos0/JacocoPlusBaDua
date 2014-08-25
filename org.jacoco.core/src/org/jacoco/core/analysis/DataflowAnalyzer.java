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

import org.jacoco.core.analysis.dua.IDuaCoverageVisitor;
import org.jacoco.core.data.AbstractExecutionDataStore;
import org.jacoco.core.data.IExecutionData;
import org.jacoco.core.internal.analysis.StringPool;
import org.jacoco.core.internal.analysis.dua.DuaClassAnalyzer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

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

	private final AbstractExecutionDataStore executionDataStore;

	private final IDuaCoverageVisitor coverageVisitor;
	
	private final StringPool stringPool;

	public DataflowAnalyzer(final AbstractExecutionDataStore executionData, final IDuaCoverageVisitor coverageVisitor) {
		this.executionDataStore = executionData;
		this.coverageVisitor = coverageVisitor;
		this.stringPool = new StringPool();
	}

	/**
	 * Analyzes the class given as a ASM reader.
	 * 
	 * @param reader
	 *            reader with class definitions
	 */
	@Override
	public void analyzeClass(final ClassReader reader) {
		final ClassNode cn = new ClassNode(Opcodes.ASM5);
		reader.accept(cn, ClassReader.EXPAND_FRAMES);
		
		// do not analyze interfaces
		if ((cn.access & Opcodes.ACC_INTERFACE) != 0) {
			return;
		}
		
		final boolean[] probes = getProbes(cn.name);
		if (probes == null) {
			System.out.println("Classe: "+cn.name+" Não-Tem DUAs");
			return;
		}
		
		final DuaClassAnalyzer analyzer = new DuaClassAnalyzer(cn, probes, stringPool);
		analyzer.analyze();
		coverageVisitor.visitCoverage(analyzer.getCoverage());
	}

	private boolean[] getProbes(String className) {
		final IExecutionData executionData = executionDataStore.get(className.hashCode());
		if (executionData != null) {
			System.out.println("Classe: "+executionData.getName()+" Tem DUAs");
			return executionData.getProbes();
		}
		return null;
	}

}
