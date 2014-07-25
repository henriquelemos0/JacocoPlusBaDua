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

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassInstrumenter extends ClassVisitor implements IdGenerator {

	private String className;

	private boolean withFrames;

	private boolean interfaceType;

	private int methodProbeCount;

	private int classProbeCount;

	public ClassInstrumenter(final ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
	}

	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName,
			final String[] interfaces) {

		methodProbeCount = 0;
		classProbeCount = 0;
		className = name;
		withFrames = (version & 0xff) >= Opcodes.V1_6;
		interfaceType = (access & Opcodes.ACC_INTERFACE) != 0;

		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public FieldVisitor visitField(final int access, final String name,
			final String desc, final String signature, final Object value) {

		InstrSupport.assertNotInstrumented(name, className);

		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {

		InstrSupport.assertNotInstrumented(name, className);

		final MethodVisitor next = super.visitMethod(access, name, desc,
				signature, exceptions);

		// Does not instrument:
		// 1. Interfaces
		if (interfaceType) {
			return next;
		} else if ((access & Opcodes.ACC_ABSTRACT) != 0) {
			return next;
		} else if (name.equals("<clinit>")) {
			return next;
		}

		final CoverageMethodTransformer mt = new CoverageMethodTransformer(
				className, this);
		return new MethodInstrumenter(access, name, desc, signature,
				exceptions, next, mt) {

			@Override
			public void sizeOverflow() {
				classProbeCount = classProbeCount - methodProbeCount;
			}

			@Override
			public void visitEnd() {
				super.visitEnd();
				methodProbeCount = 0;
			}

		};
	}

	@Override
	public void visitEnd() {
		// not instrument interfaces or
		// classes with probe count equals to zero
		if (interfaceType || classProbeCount == 0) {
			super.visitEnd();
			return;
		}

		final FieldVisitor fv = cv.visitField(InstrSupport.DATAFIELD_ACC,
				InstrSupport.DATAFIELD_NAME, InstrSupport.DATAFIELD_DESC, null,
				null);
		fv.visitEnd();

		final MethodVisitor mv = cv.visitMethod(InstrSupport.INITMETHOD_ACC,
				InstrSupport.INITMETHOD_NAME, InstrSupport.INITMETHOD_DESC,
				null, null);
		mv.visitCode();

		// Load the value of the static data field:
		mv.visitFieldInsn(Opcodes.GETSTATIC, className,
				InstrSupport.DATAFIELD_NAME, InstrSupport.DATAFIELD_DESC);
		mv.visitInsn(Opcodes.DUP);

		// Stack[1]: [J
		// Stack[0]: [J

		// Skip initialization when we already have a data array:
		final Label alreadyInitialized = new Label();
		mv.visitJumpInsn(Opcodes.IFNONNULL, alreadyInitialized);

		// Stack[0]: [J

		mv.visitInsn(Opcodes.POP);
		mv.visitLdcInsn(className);
		InstrSupport.push(mv, classProbeCount);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, InstrSupport.RUNTIME_OWNER,
				InstrSupport.RUNTIME_NAME, InstrSupport.RUNTIME_DESC, false);

		// Stack[0]: [J

		mv.visitInsn(Opcodes.DUP);

		// Stack[1]: [J
		// Stack[0]: [J

		mv.visitFieldInsn(Opcodes.PUTSTATIC, className,
				InstrSupport.DATAFIELD_NAME, InstrSupport.DATAFIELD_DESC);

		// Stack[0]: [J

		if (withFrames) {
			mv.visitFrame(Opcodes.F_NEW, 0, new Object[] {}, 1,
					new Object[] { InstrSupport.DATAFIELD_DESC });
		}
		mv.visitLabel(alreadyInitialized);
		mv.visitInsn(Opcodes.ARETURN);

		mv.visitMaxs(2, 0);
		mv.visitEnd();

		super.visitEnd();
	}

	public int nextId() {
		methodProbeCount++;
		return classProbeCount++;
	}

}
