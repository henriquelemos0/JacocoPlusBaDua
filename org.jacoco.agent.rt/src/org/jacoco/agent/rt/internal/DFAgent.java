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
package org.jacoco.agent.rt.internal;

import java.io.IOException;

import org.jacoco.agent.rt.internal.output.DFFileOutput;

import br.usp.each.saeg.badua.core.runtime.RuntimeData;

public final class DFAgent {

	// --- Static

	private static DFAgent singleton;

	public static synchronized DFAgent getInstance() {
		System.out.println("DFAgent.getInstance()");
		if (singleton == null) {
			final DFAgent agent = new DFAgent();
			ShutdownHook.register(agent);
			singleton = agent;
		}
		return singleton;
	}

	// --- Object

	private final RuntimeData data;

	private final DFFileOutput output;

	private DFAgent() {
		System.out.println("DFAgent()");
		data = new RuntimeData();
		output = new DFFileOutput();
	}

	private void shutdown() {
		System.out.println("DFAgent.shutdown()");
		try {
			output.writeExecutionData(data);
		} catch (final IOException e) {
			System.err.println("error writing execution data");
			e.printStackTrace();
		}
	}

	public RuntimeData getData() {
		System.out.println("DFAgent.getData()");
		return data;
	}

	private static final class ShutdownHook extends Thread {

		private final DFAgent agent;

		public ShutdownHook(final DFAgent agent) {
			this.agent = agent;
		}

		public ShutdownHook register() {
			Runtime.getRuntime().addShutdownHook(this);
			return this;
		}

		@Override
		public void run() {
			System.out.println("ShutdownHook.run");
			agent.shutdown();
		}

		public static ShutdownHook register(final DFAgent agent) {
			System.out.println("ShutdownHoot.register()");
			return new ShutdownHook(agent).register();
		}

	}

}
