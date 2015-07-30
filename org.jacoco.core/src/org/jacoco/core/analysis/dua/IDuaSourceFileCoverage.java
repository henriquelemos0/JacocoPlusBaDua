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

/**
 * Coverage data of a single source file. The name of this node is the local
 * name of the source file.
 */
public interface IDuaSourceFileCoverage extends ISourceDua {

	/**
	 * Returns the VM name of the package the source file belongs to.
	 * 
	 * @return package name
	 */
	public String getPackageName();

}