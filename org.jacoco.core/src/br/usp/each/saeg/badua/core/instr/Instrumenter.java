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
package br.usp.each.saeg.badua.core.instr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jacoco.core.internal.ContentTypeDetector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import br.usp.each.saeg.badua.core.internal.instr.ClassInstrumenter;
import br.usp.each.saeg.commons.io.Files;

public class Instrumenter {

	private static final int DEFAULT = 0;

	public byte[] instrument(final ClassReader reader) {
		final ClassWriter writer = new ClassWriter(reader, DEFAULT);
		final ClassVisitor ci = new ClassInstrumenter(writer);
		reader.accept(ci, ClassReader.EXPAND_FRAMES);
		return writer.toByteArray();
	}

	public byte[] instrument(final byte[] buffer, final String name)
			throws IOException {
		try {
			// no final instrument java class <= 1.5
			int value = 0;
			for (int i = 6; i < 8; i++) {
				value = (value << 8) | buffer[i];
			}
			if (value <= Opcodes.V1_5) {
				//System.out.println("class " + name + " should be compiled with java 6 or newer");
				return null;
			}
			System.out.println("Class: " + name);
			return instrument(new ClassReader(buffer));
		} catch (final RuntimeException e) {
			throw instrumentError(name, e);
		}
	}

	public void instrument(final InputStream input, final OutputStream output,
			final String name) throws IOException {
		try {
			output.write(instrument(new ClassReader(input)));
		} catch (final RuntimeException e) {
			throw instrumentError(name, e);
		}
	}

	private IOException instrumentError(final String name,
			final RuntimeException cause) {
		final String message = String.format(
				"Error while instrumenting class %s.", name);
		final IOException ex = new IOException(message);
		ex.initCause(cause);
		return ex;
	}

	public int instrumentAll(final InputStream input,
			final OutputStream output, final String name) throws IOException {
		final ContentTypeDetector detector = new ContentTypeDetector(input);
		switch (detector.getType()) {
		case ContentTypeDetector.CLASSFILE:
			instrument(detector.getInputStream(), output, name);
			return 1;
		default:
			Files.copy(detector.getInputStream(), output);
			return 0;
		}
	}

}
