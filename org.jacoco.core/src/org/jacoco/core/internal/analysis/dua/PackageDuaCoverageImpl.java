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

import java.util.Collection;

import org.jacoco.core.analysis.IPackageCoverage;
import org.jacoco.core.analysis.dua.CoverageDuaImpl;
import org.jacoco.core.analysis.dua.IDuaClassCoverage;
import org.jacoco.core.analysis.dua.IDuaPackageCoverage;
import org.jacoco.core.analysis.dua.IDuaSourceFileCoverage;

/**
 * Implementation of {@link IPackageCoverage}.
 */
public class PackageDuaCoverageImpl extends CoverageDuaImpl implements
		IDuaPackageCoverage {

	private final Collection<IDuaClassCoverage> classes;

	private final Collection<IDuaSourceFileCoverage> sourceFiles;

	/**
	 * Creates package node instance for a package with the given name.
	 * 
	 * @param name
	 *            vm name of the package
	 * @param classes
	 *            collection of all classes in this package
	 * @param sourceFiles
	 *            collection of all source files in this package
	 */
	public PackageDuaCoverageImpl(final String name,
			final Collection<IDuaClassCoverage> classes,
			final Collection<IDuaSourceFileCoverage> sourceFiles) {
		super(ElementType.PACKAGE, name);
		this.classes = classes;
		this.sourceFiles = sourceFiles;
		increment(sourceFiles);
		for (final IDuaClassCoverage c : classes) {
			// We need to add only classes without a source file reference.
			// Classes associated with a source file are already included in the
			// SourceFileCoverage objects.
			if (c.getSourceFileName() == null) {
				increment(c);
			}
		}
	}

	// === IPackageCoverage implementation ===

	public Collection<IDuaClassCoverage> getClasses() {
		return classes;
	}

	public Collection<IDuaSourceFileCoverage> getSourceFiles() {
		return sourceFiles;
	}

}
