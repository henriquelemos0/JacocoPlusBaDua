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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;

import org.jacoco.agent.rt.IAgent;
import org.jacoco.agent.rt.internal.output.FileOutput;
import org.jacoco.agent.rt.internal.output.IAgentOutput;
import org.jacoco.agent.rt.internal.output.NoneOutput;
import org.jacoco.agent.rt.internal.output.TcpClientOutput;
import org.jacoco.agent.rt.internal.output.TcpServerOutput;
import org.jacoco.core.JaCoCo;
import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.AbstractRuntime;
import org.jacoco.core.runtime.AbstractRuntimeData;
import org.jacoco.core.runtime.AgentOptions;
import org.jacoco.core.runtime.AgentOptions.OutputMode;
import org.jacoco.core.runtime.DataFlowRuntimeData;
import org.jacoco.core.runtime.RuntimeData;

/**
 * The agent manages the life cycle of JaCoCo runtime.
 */
public class Agent implements IAgent {

	private static Agent singleton;

	/**
	 * Returns a global instance which is already started. If the method is
	 * called the first time the instance is created with the given options.
	 * 
	 * @param options
	 *            options to configure the instance
	 * @return global instance
	 */
	public static synchronized Agent getInstance(final AgentOptions options) {
		System.out.println("Agent.getInstance(opt)");
		if (singleton == null) {
			final Agent agent = new Agent(options, IExceptionLogger.SYSTEM_ERR);// cria
																				// um
																				// agente
			agent.startup();// inicia o agente
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					agent.shutdown();// finaliza o agent
				}
			});
			singleton = agent;
		}
		return singleton;
	}

	/**
	 * Returns a global instance which is already started. If a agent has not
	 * been initialized before this method will fail.
	 * 
	 * @return global instance
	 * @throws IllegalStateException
	 *             if no Agent has been started yet
	 */
	public static synchronized Agent getInstance() throws IllegalStateException {
		if (singleton == null) {
			throw new IllegalStateException("JaCoCo agent not started.");
		}
		return singleton;
	}

	private final AgentOptions options;

	private final IExceptionLogger logger;

	private final AbstractRuntimeData data;

	private IAgentOutput output;

	private Callable<Void> jmxRegistration;

	/**
	 * Creates a new agent with the given agent options.
	 * 
	 * @param options
	 *            agent options
	 * @param logger
	 *            logger used by this agent
	 */
	Agent(final AgentOptions options, final IExceptionLogger logger) {
		this.options = options;
		this.logger = logger;

		if (options.isDataflow()) {
			this.data = new DataFlowRuntimeData();
		} else {
			this.data = new RuntimeData();
		}
	}

	/**
	 * Returns the runtime data object created by this agent
	 * 
	 * @return runtime data for this agent instance
	 */
	public AbstractRuntimeData getData() {
		return data;
	}

	/**
	 * Initializes this agent.
	 * 
	 */
	public void startup() {
		System.out.println("Agent.startup()");
		try {
			String sessionId = options.getSessionId();
			if (sessionId == null) {
				sessionId = createSessionId();
			}
			data.setSessionId(sessionId);
			output = createAgentOutput(); // instancia o output
			output.startup(options, data);
			if (options.getJmx()) {
				jmxRegistration = new JmxRegistration(this);
			}
		} catch (final Exception e) {
			logger.logExeption(e);
		}
	}

	/**
	 * Shutdown the agent again.
	 */
	public void shutdown() {
		System.out.println("Agent.shutdown()");
		try {
			if (options.getDumpOnExit()) {
				System.out
						.println("Agent.shutdown -> output.writeExecutionData(false)");
				output.writeExecutionData(false);
			}
			output.shutdown();// shutdown no output, caso for tcp, fecha os
								// sockets
			if (jmxRegistration != null) {
				jmxRegistration.call();
			}
		} catch (final Exception e) {
			logger.logExeption(e);
		}
	}

	/**
	 * Create output implementation as given by the agent options.
	 * 
	 * @return configured controller implementation
	 */
	IAgentOutput createAgentOutput() {
		System.out.println("Agent.createAgentOutput()");
		final OutputMode controllerType = options.getOutput();
		System.out.println("Tipo do output: " + controllerType);
		switch (controllerType) {
		case file:
			return new FileOutput();
		case tcpserver:
			return new TcpServerOutput(logger);
		case tcpclient:
			return new TcpClientOutput(logger);
		case none:
			return new NoneOutput();
		default:
			throw new AssertionError(controllerType);
		}
	}

	private String createSessionId() {
		String host;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (final Exception e) {
			// Also catch platform specific exceptions (like on Android) to
			// avoid bailing out here
			host = "unknownhost";
		}
		return host + "-" + AbstractRuntime.createRandomId();
	}

	// === IAgent Implementation ===

	public String getVersion() {
		return JaCoCo.VERSION;
	}

	public String getSessionId() {
		return data.getSessionId();
	}

	public void setSessionId(final String id) {
		data.setSessionId(id);
	}

	public void reset() {
		data.reset();
	}

	public byte[] getExecutionData(final boolean reset) {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			final ExecutionDataWriter writer = new ExecutionDataWriter(buffer);
			data.collect(writer, writer, reset);
		} catch (final IOException e) {
			// Must not happen with ByteArrayOutputStream
			throw new AssertionError(e);
		}
		return buffer.toByteArray();
	}

	public void dump(final boolean reset) throws IOException {
		output.writeExecutionData(reset);
	}

}
