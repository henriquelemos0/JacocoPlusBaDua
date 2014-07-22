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

import static java.lang.String.format;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public final class InstrSupport {

	private InstrSupport() {
		// No instances
	}

	public static final String RUNTIME_OWNER = "org/jacoco/agent/rt/internal/DFRT";

	public static final String RUNTIME_NAME = "getData";

	public static final String RUNTIME_DESC = "(Ljava/lang/String;I)[J";

	// --- Data Field

	/**
	 * Name of the field that stores data-flow coverage of a class.
	 */
	public static final String DATAFIELD_NAME = "$data";

	/**
	 * Data type of the field that stores data-flow coverage for a class (
	 * <code>long[]</code>).
	 */
	public static final String DATAFIELD_DESC = "[J";

	/**
	 * Access modifiers of the field that stores data-flow coverage of a class.
	 */
	public static final int DATAFIELD_ACC = Opcodes.ACC_SYNTHETIC
			| Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_TRANSIENT;

	// --- Initialization Method

	/**
	 * Name of the initialization method.
	 */
	public static final String INITMETHOD_NAME = "$initData";

	/**
	 * Descriptor of the initialization method.
	 */
	public static final String INITMETHOD_DESC = "()[J";

	/**
	 * Access modifiers of the initialization method.
	 */
	public static final int INITMETHOD_ACC = Opcodes.ACC_SYNTHETIC
			| Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL;

	/**
	 * Ensures that the given member does not correspond to a internal member
	 * created by the instrumentation process. This would mean that the class is
	 * already instrumented.
	 * 
	 * @param member
	 *            name of the member to check
	 * @param owner
	 *            name of the class owning the member
	 * @throws IllegalStateException
	 *             thrown if the member has the same name than the
	 *             instrumentation member
	 */
	public static void assertNotInstrumented(final String member,
			final String owner) throws IllegalStateException {
		if (member.equals(DATAFIELD_NAME) || member.equals(INITMETHOD_NAME)) {
			throw new IllegalStateException(format(
					"Class %s is already instrumented.", owner));
		}
	}

	/**
	 * Generates the instruction to push the given int value on the stack.
	 * Implementation taken from
	 * {@link org.objectweb.asm.commons.GeneratorAdapter#push(int)}.
	 * 
	 * @param mv
	 *            visitor to emit the instruction
	 * @param value
	 *            the value to be pushed on the stack.
	 */
	public static void push(final MethodVisitor mv, final int value) {
		if (value >= -1 && value <= 5) {
			mv.visitInsn(Opcodes.ICONST_0 + value);
		} else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
			mv.visitIntInsn(Opcodes.BIPUSH, value);
		} else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
			mv.visitIntInsn(Opcodes.SIPUSH, value);
		} else {
			mv.visitLdcInsn(Integer.valueOf(value));
		}
	}

	/**
	 * Generates the instruction to push the given int value on the stack.
	 * 
	 * @param value
	 *            the value to be pushed on the stack.
	 * @return the AbstractInsnNode that push the given int value on the stack.
	 */
	public static AbstractInsnNode push(final int value) {
		final AbstractInsnNode insn;
		if (value >= -1 && value <= 5) {
			insn = new InsnNode(Opcodes.ICONST_0 + value);
		} else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
			insn = new IntInsnNode(Opcodes.BIPUSH, value);
		} else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
			insn = new IntInsnNode(Opcodes.SIPUSH, value);
		} else {
			insn = new LdcInsnNode(value);
		}
		return insn;
	}

	public static void swap(final MethodVisitor mv, final Type stackTop,
			final Type belowTop) {
		if (stackTop.getSize() == 1) {
			if (belowTop.getSize() == 1) {
				// Top = 1, below = 1
				mv.visitInsn(Opcodes.SWAP);
			} else {
				// Top = 1, below = 2
				mv.visitInsn(Opcodes.DUP_X2);
				mv.visitInsn(Opcodes.POP);
			}
		} else {
			if (belowTop.getSize() == 1) {
				// Top = 2, below = 1
				mv.visitInsn(Opcodes.DUP2_X1);
			} else {
				// Top = 2, below = 2
				mv.visitInsn(Opcodes.DUP2_X2);
			}
			mv.visitInsn(Opcodes.POP2);
		}
	}

}
