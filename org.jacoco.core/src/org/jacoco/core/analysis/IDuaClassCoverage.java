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
package org.jacoco.core.analysis;

import java.util.Collection;

/**
 * Coverage data of a single class containing methods. The name of this node is
 * the fully qualified class name in VM notation (slash separated).
 * 
 * @see IDuaMethodCoverage
 */
public interface IDuaClassCoverage {

	/**
	 * Returns the identifier for this class which is the CRC64 signature of the
	 * class definition.
	 * 
	 * @return class identifier
	 */
	public long getId();

	/**
	 * Returns the VM name of the package this class belongs to.
	 * 
	 * @return VM name of the package
	 */
	public String getPackageName();

	/**
	 * Returns the optional name of the corresponding source file.
	 * 
	 * @return name of the corresponding source file
	 */
	public String getSourceFileName();

	/**
	 * Returns the methods included in this class.
	 * 
	 * @return methods of this class
	 */
	public Collection<IDuaMethodCoverage> getMethods();

}