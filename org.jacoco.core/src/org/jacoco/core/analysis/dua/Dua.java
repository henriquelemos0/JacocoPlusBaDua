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

import java.util.Set;


public class Dua implements IDua {

	private final Set<Integer> def;

	private final Set<Integer> use;

	private final Set<Integer> target;

	private final String var;
	
	private final int status;

	public Dua(final Set<Integer> def, final Set<Integer> use, final String var, final int status) {
		this(def,use,null,var,status);
	}

	public Dua(final Set<Integer> def, final Set<Integer> use, final Set<Integer> target, final String var, final int status) {
		this.def = def;
		this.use = use;
		this.target = target;
		this.var = var;
		this.status = status;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final Dua other = (Dua) obj;

		if (def != other.def || use != other.use || target != other.target
				|| var != other.var) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getDef()
	 */
	public Set<Integer> getDef() {
		return def;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getUse()
	 */
	public Set<Integer> getUse() {
		return use;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getTarget()
	 */
	public Set<Integer> getTarget() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getVar()
	 */
	public String getVar() {
		return var;
	}

	public int getStatus() {
		return status;
	}

}
