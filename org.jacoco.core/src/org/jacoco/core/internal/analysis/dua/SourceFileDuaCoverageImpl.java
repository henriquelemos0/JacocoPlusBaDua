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

import org.jacoco.core.analysis.ISourceFileCoverage;
import org.jacoco.core.analysis.dua.IDuaSourceFileCoverage;

/**
 * Implementation of {@link ISourceFileCoverage}.
 */
public class SourceFileDuaCoverageImpl extends SourceDuaImpl implements
		IDuaSourceFileCoverage {

	private final String packagename;

	/**
	 * Creates a source file data object with the given parameters.
	 * 
	 * @param name
	 *            name of the source file
	 * @param packagename
	 *            vm name of the package the source file belongs to
	 */
	public SourceFileDuaCoverageImpl(final String name, final String packagename) {
		super(ElementType.SOURCEFILE, name);
		this.packagename = packagename;
	}

	// === ISourceFileCoverage implementation ===

	public String getPackageName() {
		return packagename;
	}

}
