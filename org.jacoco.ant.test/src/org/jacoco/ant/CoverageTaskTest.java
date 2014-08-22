/*******************************************************************************
 * Copyright (c) 2009, 2014 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Brock Janiczak - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.ant;

import java.io.File;

import junit.framework.TestSuite;

import org.apache.ant.antunit.junit3.AntUnitSuite;
import org.apache.ant.antunit.junit4.AntUnitSuiteRunner;
import org.junit.runner.RunWith;

@RunWith(AntUnitSuiteRunner.class)
public class CoverageTaskTest {

	public static TestSuite suite() {
		System.setProperty("org.jacoco.ant.coverageTaskTest.classes.dir",
				TestTarget.getClassPath());
		final File file = new File("src/org/jacoco/ant/CoverageTaskTest.xml");
		return new AntUnitSuite(file, CoverageTaskTest.class);
	}

}
