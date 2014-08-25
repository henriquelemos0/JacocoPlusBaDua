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

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.dua.Dua;
import org.jacoco.core.analysis.dua.DuaMethodCoverage;
import org.jacoco.core.analysis.dua.IDua;
import org.jacoco.core.internal.flow.MethodProbesVisitor;
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

/**
 * A {@link MethodProbesVisitor} that analyzes which statements and branches of
 * a method has been executed based on given probe data.
 */
public class DuaMethodAnalyzer {

	private final DuaMethodCoverage coverage;
	private final MethodNode methodNode;

	private final String className;
	private final int methodProbeIndex;
	private final boolean[] probes;
	private Variable[] vars;
	private int[][] blocks;

	/**
	 * New Method analyzer for the given probe data.
	 * 
	 * @param methodId
	 * @param className
	 * 
	 * @param name
	 *            method name
	 * @param desc
	 *            description of the method
	 * @param signature
	 *            optional parameterized signature
	 * 
	 * @param probes
	 *            recorded probe date of the containing class or
	 *            <code>null</code> if the class is not executed at all
	 * @param methodProbeIndex
	 */
	public DuaMethodAnalyzer(final int methodId, final String className, final MethodNode methodNode,
			final boolean[] probes, final int methodProbeIndex) {
		super();
		this.className = className;
		this.methodNode = methodNode;
		this.probes = probes;
		this.methodProbeIndex = methodProbeIndex;
		this.coverage = new DuaMethodCoverage(methodId, methodNode.name, methodNode.desc, methodNode.signature);
	}

	/**
	 * Returns the coverage data for this method after this visitor has been
	 * processed.
	 * 
	 * @return coverage data for this method
	 */
	public DuaMethodCoverage getCoverage() {
		return coverage;
	}

	public void analyze() {
		
		List<LocalVariableNode> localVariables = methodNode.localVariables;

		//put in the hashmap the name of the source variables method
		HashMap<Integer,String> variables = new HashMap<Integer,String>();
		for(LocalVariableNode var: localVariables) variables.put(var.index,var.name);

		final int[] lines = getLines();
		final DefUseChain[] chains = transform(methodNode);
		for (int i = 0; i < chains.length; i++) {
			final Set<Integer> defLines = getDefs(lines, chains[i]);
			final Set<Integer> useLines = getUses(lines, chains[i]);
			final Set<Integer> targetLines = getTargets(lines, chains[i]);
			String varName = getName(chains[i]);
			varName = getVariableName(varName,variables);
			final int status = getStatus(i);
			final IDua dua = new Dua(defLines, useLines, targetLines, varName, status);
			coverage.addDua(dua);
		}
	}

	private int getStatus(final int i) {
		int status = ICounter.NOT_COVERED;
		if (probes[methodProbeIndex + i]) {
			status = ICounter.FULLY_COVERED;
		}
		return status;
	}

	private Set<Integer> getTargets(final int[] lines, final DefUseChain defUseChain) {
		final Set<Integer> targets = new TreeSet<Integer>();
		if (defUseChain.target != -1) {
			for (final int instrucao : blocks[defUseChain.target]) {
				targets.add(Integer.valueOf(lines[instrucao]));
			}
		}
		return targets;
	}

	private Set<Integer> getUses(final int[] lines, final DefUseChain defUseChain) {
		final Set<Integer> uses = new TreeSet<Integer>();
		for (final int instrucao : blocks[defUseChain.use]) {
			uses.add(Integer.valueOf(lines[instrucao]));
		}
		return uses;
	}

	private Set<Integer> getDefs(final int[] lines, final DefUseChain defUseChain) {
		final Set<Integer> defs = new TreeSet<Integer>();
		for (final int instrucao : blocks[defUseChain.def]) {
			defs.add(Integer.valueOf(lines[instrucao]));
		}
		return defs;
	}

	private int[] getLines() {
		final int[] lines = new int[methodNode.instructions.size()];
		for (int i = 0; i < lines.length; i++) {
			if (methodNode.instructions.get(i) instanceof LineNumberNode) {
				final LineNumberNode insn = (LineNumberNode) methodNode.instructions.get(i);
				lines[methodNode.instructions.indexOf(insn.start)] = insn.line;
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
		return lines;
	}

	private String getName(final DefUseChain dua) {
		final Variable var = vars[dua.var];
		String name;
		if (var instanceof Field) {
			name = ((Field) var).name;
		} else {
			try {
				name = varName(dua.def, ((Local) var).var, methodNode);
			} catch (final Exception e) {
				name = var.toString();
			}
		}
		return name;
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
		final int[][] successors = analyzer.getSuccessors();
		final int[][] predecessors = analyzer.getPredecessors();
		final int[][] basicBlocks = analyzer.getBasicBlocks();
		final int[] leaders = analyzer.getLeaders();

		blocks = analyzer.getBasicBlocks();
		vars = variables;

		final DefUseChain[] chains = DefUseChain.toBasicBlock(
				new DepthFirstDefUseChainSearch().search(frames, variables, successors, predecessors), leaders,
				basicBlocks);

		return chains;
	}
	
	
	private String getVariableName(String variable, HashMap<Integer, String> variables) {
		String type = variable.substring(0, 1);
		if(type.equals("L")){
			String split = variable.substring(2);
			try{
				Integer.parseInt(split);
				return variables.get(Integer.parseInt(split));
			}catch(NumberFormatException e){ 
				split = (String) split.subSequence(0, split.length()-2);
				return variables.get(Integer.parseInt(split))+"[]";
			}
		}
		return variable;
	}

}
