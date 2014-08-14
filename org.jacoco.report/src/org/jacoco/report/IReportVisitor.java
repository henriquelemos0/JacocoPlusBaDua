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
package org.jacoco.report;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.IExecutionData;
import org.jacoco.core.data.SessionInfo;

/**
 * Interface for all implementations to retrieve structured report data. Unlike
 * nested {@link IReportGroupVisitor} instances the root visitor accepts exactly
 * one bundle or group.
 */
public interface IReportVisitor extends IReportGroupVisitor {

	/**
	 * Initializes the report with global information. This method has to be
	 * called before any other method can be called.
	 * 
	 * @param sessionInfos
	 *            list of chronological ordered {@link SessionInfo} objects
	 *            where execution data has been collected for this report.
	 * @param executionData
	 *            collection of all {@link ExecutionData} objects that are
	 *            considered for this report
	 * @throws IOException
	 *             in case of IO problems with the report writer
	 */
	public void visitInfo(List<SessionInfo> sessionInfos,
			Collection<IExecutionData> executionData) throws IOException;

	/**
	 * Has to be called after all report data has been emitted.
	 * 
	 * @throws IOException
	 *             in case of IO problems with the report writer
	 */
	public void visitEnd() throws IOException;

}
