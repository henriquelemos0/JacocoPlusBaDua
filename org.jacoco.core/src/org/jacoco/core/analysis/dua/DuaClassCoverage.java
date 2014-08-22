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
package org.jacoco.core.analysis.dua;

import java.util.ArrayList;
import java.util.Collection;

public class DuaClassCoverage implements IDuaClassCoverage {

	private final long id;
	private final String name;
	private final String signature;
	private final String superName;
	private final String[] interfaces;
	private final Collection<IDuaMethodCoverage> methods;
	private String sourceFileName;

	public DuaClassCoverage(String name, long id, String signature,
			String superName, String[] interfaces) {
		super();
		this.id = id;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
		this.methods = new ArrayList<IDuaMethodCoverage>();
	}
	
	/**
	 * Add a method to this class.
	 * 
	 * @param method
	 *            method data to add
	 */
	public void addMethod(final IDuaMethodCoverage method) {
		this.methods.add(method);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public Collection<IDuaMethodCoverage> getMethods() {
		return methods;
	}

	public String getPackageName() {
		final int pos = getName().lastIndexOf('/');
		return pos == -1 ? "" : getName().substring(0, pos);
	}

	public String getSignature() {
		return signature;
	}

	public String getSuperName() {
		return superName;
	}

	public String[] getInterfaceNames() {
		return interfaces;
	}

}
