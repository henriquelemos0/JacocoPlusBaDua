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

import java.util.List;

import org.jacoco.core.analysis.dua.DuaClassCoverage;
import org.jacoco.core.analysis.dua.IDuaMethodCoverage;
import org.jacoco.core.internal.analysis.StringPool;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Analyzes the structure of a class.
 */
public class DuaClassAnalyzer {

	private final long classid;
	private final String className;
	private final boolean probes[];
	private final List<MethodNode> methods;
	private final StringPool stringPool;
	private int methodProbeIndex = 0;

	private final DuaClassCoverage coverage;

	/**
	 * Creates a new analyzer that builds coverage data for a class.
	 * 
	 * @param classid
	 *            id of the class
	 * @param noMatch
	 *            <code>true</code> if class id does not match with execution
	 *            data
	 * @param probes
	 *            execution data for this class or <code>null</code>
	 * @param stringPool
	 *            shared pool to minimize the number of {@link String} instances
	 */
	public DuaClassAnalyzer(final ClassNode cn, final boolean[] probes, final StringPool stringPool) {
		this.classid = cn.name.hashCode();
		this.methods = cn.methods;
		this.probes = probes;
		this.stringPool = stringPool;
		final String[] interfaces = cn.interfaces.toArray(new String[cn.interfaces.size()]);
		this.coverage = new DuaClassCoverage(stringPool.get(cn.name), classid, stringPool.get(cn.signature),
				stringPool.get(cn.superName), stringPool.get(interfaces));
		this.className=cn.name;

	}

	/**
	 * Returns the coverage data for this class after this visitor has been
	 * processed.
	 * 
	 * @return coverage data for this class
	 */
	public DuaClassCoverage getCoverage() {
		return coverage;
	}

	public void analyze() {
		int methodId = 0;
		for (final MethodNode method : methods) {
			// Does not instrument:
			// 1. Abstract methods
			if ((method.access & Opcodes.ACC_ABSTRACT) != 0) {
				continue;
			}
	        // 2. Interfaces
			if ((method.access & Opcodes.ACC_INTERFACE) != 0) {
				continue;
			}
	        // 3. Synthetic methods
			if ((method.access & Opcodes.ACC_SYNTHETIC) != 0) {
				continue;
			}
			// 4. Static class initialization
			if (method.name.equals("<clinit>")) {
				continue;
			}

			visitMethod(method, methodId++);
		}
	}

	public void visitSource(final String source, final String debug) {
		this.coverage.setSourceFileName(stringPool.get(source));
	}

	public void visitMethod(final MethodNode methodNode, final int methodId) {
		final DuaMethodAnalyzer methodAnalyzer = new DuaMethodAnalyzer(methodId, coverage.getName(), methodNode,
				probes, methodProbeIndex);
		methodAnalyzer.analyze();

		final IDuaMethodCoverage methodCoverage = methodAnalyzer.getCoverage();
		
		if (methodCoverage.getDuas().size() > 0) {
			// Only consider methods that actually contain code
			coverage.addMethod(methodCoverage);
		}

		methodProbeIndex += ((methodCoverage.getDuas().size() + 63) / 64) * 64;
	}

}
