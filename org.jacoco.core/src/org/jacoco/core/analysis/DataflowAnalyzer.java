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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jacoco.core.data.AbstractExecutionDataStore;
import org.jacoco.core.data.IExecutionData;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import br.usp.each.saeg.asm.defuse.DefUseAnalyzer;
import br.usp.each.saeg.asm.defuse.DefUseChain;
import br.usp.each.saeg.asm.defuse.DefUseFrame;
import br.usp.each.saeg.asm.defuse.DepthFirstDefUseChainSearch;
import br.usp.each.saeg.asm.defuse.Field;
import br.usp.each.saeg.asm.defuse.Local;
import br.usp.each.saeg.asm.defuse.Variable;
import br.usp.each.saeg.badua.core.internal.instr.IdGenerator;

/**
 * An {@link DataflowAnalyzer} instance processes a set of Java class files and
 * calculates coverage data for them. For each class file the result is reported
 * to a given {@link ICoverageVisitor} instance. In addition the
 * {@link DataflowAnalyzer} requires a {@link AbstractExecutionDataStore}
 * instance that holds the execution data for the classes to analyze. The
 * {@link DataflowAnalyzer} offers several methods to analyze classes from a
 * variety of sources.
 */
public class DataflowAnalyzer extends AbstractAnalyzer implements IdGenerator {

	private final AbstractExecutionDataStore executionDataStore;

	private final IDuaCoverageVisitor coverageVisitor;

	private Variable[] vars;
	int[][] blocks;

	private static MethodVisitor NOP = new MethodVisitor(Opcodes.ASM5) {
		// Use default implementation
	};

	private int methodProbeCount;

	private int classProbeCount;

	public DataflowAnalyzer(final AbstractExecutionDataStore executionData, final IDuaCoverageVisitor coverageVisitor) {
		this.executionDataStore = executionData;
		this.coverageVisitor = coverageVisitor;
	}

	protected ClassVisitor createAnalyzingVisitor(final long classid, final String className) {
		// TODO
		return null;
	}

	/**
	 * Analyzes the class given as a ASM reader.
	 * 
	 * @param reader
	 *            reader with class definitions
	 */

	String className;

	@Override
	public void analyzeClass(final ClassReader reader) {
		final ClassNode cn = new ClassNode(Opcodes.ASM5);
		reader.accept(cn, ClassReader.EXPAND_FRAMES);

		// do not analyze interfaces
		if ((cn.access & Opcodes.ACC_INTERFACE) != 0) {
			return;
		}
		className = cn.name;
		classProbeCount = 0;
		System.out.println("ClassName: " + className);
		// iteration order is important!
		methodProbeCount = 0; // reset method probe counter
		for (final MethodNode mn : cn.methods) {
			methodProbeCount += analyzeMethods(mn);
		}

		// final ClassVisitor visitor = createAnalyzingVisitor(reader
		// .getClassName().hashCode(), reader.getClassName());
		// reader.accept(visitor, 0);
	}

	private int analyzeMethods(final MethodNode mn) {
		// do not analyze abstract methods
		if ((mn.access & Opcodes.ACC_ABSTRACT) != 0) {
			return 0;
		}
		// do not analyze static class initialization
		else if (mn.name.equals("<clinit>")) {
			return 0;
		}
		final DefUseChain[] chains = transform(mn);
		System.out.println("Method Name: " + mn.name);
		final boolean[] probes = getProbes();
		if (probes.length == 0) {
			return 0;
		}

		final int[] lines = new int[mn.instructions.size()];
		for (int i = 0; i < lines.length; i++) {
			if (mn.instructions.get(i) instanceof LineNumberNode) {
				final LineNumberNode insn = (LineNumberNode) mn.instructions.get(i);
				lines[mn.instructions.indexOf(insn.start)] = insn.line;
			}
		}
		int line = 1;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] == 0) {
				lines[i] = line;
			} else {
				line = lines[i];
			}
		}

		int i = 0;
		System.out.print("linhas: ");
		for (final int l : lines) {
			System.out.print(l + " ");
		}
		System.out.println();

		for (final DefUseChain dua : chains) {
			final int[] isntDef = blocks[dua.def];
			final int[] isntUse = blocks[dua.use];
			int[] instTarget = null;
			if (dua.target != -1) {
				instTarget = blocks[dua.target];
				// frames[index[index.length-1]].predicate
			}
			System.out.print("Def[] = ");
			for (final int def : isntDef) {
				System.out.print(def + " ");
			}
			System.out.print("Use[] = ");
			for (final int use : isntUse) {
				System.out.print(use + " ");
			}
			if (instTarget != null) {
				System.out.print("Target[] = ");
				for (final int target : instTarget) {
					System.out.print(target + " ");
				}
			}

			final Set<Integer> defs = new TreeSet<Integer>();
			final Set<Integer> uses = new TreeSet<Integer>();
			final Set<Integer> targets = new TreeSet<Integer>();

			for (final int instrucao : blocks[dua.def]) {
				defs.add(Integer.valueOf(lines[instrucao]));
			}

			for (final int instrucao : blocks[dua.use]) {
				uses.add(Integer.valueOf(lines[instrucao]));
			}

			if (dua.target != -1) {
				for (final int instrucao : blocks[dua.target]) {
					targets.add(Integer.valueOf(lines[instrucao]));
				}
			}

			final Variable var = vars[dua.var];
			String name;
			if (var instanceof Field) {
				name = ((Field) var).name;
			} else {
				try {
					name = varName(dua.def, ((Local) var).var, mn);
				} catch (final Exception e) {
					name = var.toString();
				}
			}
			System.out.println("DUA Index: Def:" + dua.def + " Use:" + dua.use + " Target:" + dua.target + " Var"
					+ dua.var);
			System.out.print("DUA Linha: Def:" + defs + " Use:" + uses + " Targets: " + targets + " Var name:" + name
					+ "  ");
			System.out.println(probes[methodProbeCount + i]);
			i++;
		}
		return ((chains.length + 63) / 64) * 64;
	}

	private boolean[] getProbes() {
		final IExecutionData executionData = executionDataStore.get(className.hashCode());
		if (executionData != null) {
			return executionDataStore.get(className.hashCode()).getProbes();
		}
		return new boolean[] {};
	}

	private String varName(final int insn, final int index, final MethodNode mn) {
		for (final LocalVariableNode local : mn.localVariables) {
			if (local.index == index) {
				final int start = mn.instructions.indexOf(local.start);
				final int end = mn.instructions.indexOf(local.end);
				if (insn + 1 >= start && insn + 1 <= end) {
					return local.name;
				}
			}
		}
		throw new RuntimeException("Variable not found");
	}

	private DefUseChain[] transform(final MethodNode methodNode) {
		final DefUseAnalyzer analyzer = new DefUseAnalyzer();
		try {
			analyzer.analyze(className, methodNode);
		} catch (final AnalyzerException e) {
			throw new RuntimeException(e);
		}

		final DefUseFrame[] frames = analyzer.getDefUseFrames();
		final Variable[] variables = analyzer.getVariables();
		// frames[0].getDefinitions().contains(variables[0]); // instrucao 0
		// teve deficao da variavel 0, se retornar true
		vars = variables;
		final int[][] successors = analyzer.getSuccessors();
		final int[][] predecessors = analyzer.getPredecessors();
		final int[][] basicBlocks = analyzer.getBasicBlocks();
		final int[] leaders = analyzer.getLeaders();

		blocks = analyzer.getBasicBlocks();

		final DefUseChain[] chains = DefUseChain.toBasicBlock(
				new DepthFirstDefUseChainSearch().search(frames, variables, successors, predecessors), leaders,
				basicBlocks);

		return chains;
	}

	private String[] toArray(final List<String> l) {
		final String[] array = new String[l.size()];
		int i = 0;
		for (final String string : l) {
			array[i++] = string;
		}
		return array;
	}

	public int nextId() {
		methodProbeCount++;
		return classProbeCount++;
	}

}
