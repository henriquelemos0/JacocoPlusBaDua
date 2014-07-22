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

import org.objectweb.asm.tree.MethodNode;

public class MethodTransformer {

	protected MethodTransformer mt;

	public MethodTransformer() {
		this.mt = null;
	}

	public MethodTransformer(final MethodTransformer mt) {
		this.mt = mt;
	}

	public void transform(final MethodNode methodNode) {
		if (mt != null) {
			mt.transform(methodNode);
		}
	}

}
