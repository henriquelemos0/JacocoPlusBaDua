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

import java.lang.instrument.Instrumentation;

import org.jacoco.core.runtime.AgentOptions;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.ModifiedSystemClassRuntime;

/**
 * The agent which is referred as the <code>Premain-Class</code>. The agent
 * configuration is provided with the agent parameters in the command line.
 */
public final class PreMain {

	private PreMain() {
		// no instances
	}

	/**
	 * This method is called by the JVM to initialize Java agents.
	 * 
	 * @param options
	 *            agent options
	 * @param inst
	 *            instrumentation callback provided by the JVM
	 * @throws Exception
	 *             in case initialization fails
	 */
	public static void premain(final String options, final Instrumentation inst)
			throws Exception {
		System.out.println("Premain.premain(opt,inst)");
		final AgentOptions agentOptions = new AgentOptions(options);// pega os
		System.out.println("dataFlow = " + agentOptions.getDataflow());
		// parametros
		// do agent
		IRuntime runtime = null;
		// if (agentOptions.getDataflow()) {
		// DFRT.init();
		// } else {
		final Agent agent = Agent.getInstance(agentOptions);
		runtime = createRuntime(inst);
		runtime.startup(agent.getData());
		System.out.println("runtime.getClass -> " + runtime.getClass());
		// }
		inst.addTransformer(new CoverageTransformer(runtime, agentOptions,
				IExceptionLogger.SYSTEM_ERR));
	}

	private static IRuntime createRuntime(final Instrumentation inst)
			throws Exception {
		return ModifiedSystemClassRuntime.createFor(inst, "java/util/UUID");
	}

}
