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
package org.jacoco.agent.rt.internal.output;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import br.usp.each.saeg.badua.core.runtime.RuntimeData;

public class DFFileOutput {

	public void writeExecutionData(final RuntimeData data) throws IOException {
		System.out.println("DFFileOutput.writeExecutionData(data)");
		String filename = System.getProperty("output.file");
		if (filename == null) {
			filename = "coverage.ser";
		}

		final ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(filename));
		try {
			oos.writeObject(data.getData());
		} finally {
			oos.close();
		}
	}

}
