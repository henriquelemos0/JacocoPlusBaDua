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
package br.usp.each.saeg.badua.core.internal.instr;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

public class LabelFrameNode extends LabelNode {

	private final Set<FrameNode> frames;

	public LabelFrameNode() {
		frames = new HashSet<FrameNode>();
	}

	public Set<FrameNode> getFrameNodes() {
		return Collections.unmodifiableSet(frames);
	}

	public boolean addFrameNode(final FrameNode frame) {
		return frames.add(frame);
	}

	public static void insertBefore(final AbstractInsnNode location,
			final InsnList insns, final InsnList insert) {

		AbstractInsnNode insn = location.getPrevious();
		while (true) {
			switch (insn.getType()) {
			case AbstractInsnNode.LABEL:
				final LabelFrameNode lfn = (LabelFrameNode) insn;
				final LabelFrameNode label = create(lfn);
				if (label != null) {
					insert.add(label);
				}
				break;
			case AbstractInsnNode.FRAME:
			case AbstractInsnNode.LINE:
				insn = insn.getPrevious();
				continue;
			default:
				break;
			}
			insns.insertBefore(location, insert);
			return;
		}
	}

	private static LabelFrameNode create(final LabelFrameNode lfn) {
		final LabelFrameNode label = new LabelFrameNode();

		boolean changed = false;
		for (final FrameNode frame : lfn.frames) {
			for (int i = 0; i < frame.local.size(); i++) {
				if (frame.local.get(i) == lfn) {
					frame.local.set(i, label);
					changed = true;
				}
			}
			for (int i = 0; i < frame.stack.size(); i++) {
				if (frame.stack.get(i) == lfn) {
					frame.stack.set(i, label);
					changed = true;
				}
			}
		}

		if (!changed) {
			return null;
		}

		return label;
	}

}
