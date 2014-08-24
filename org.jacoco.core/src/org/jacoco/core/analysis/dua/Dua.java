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
		this(def, use, null, var, status);
	}

	public Dua(final Set<Integer> def, final Set<Integer> use, final Set<Integer> target, final String var,
			final int status) {
		this.def = def;
		this.use = use;
		this.target = target;
		this.var = var;
		this.status = status;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getVar()
	 */
	public int getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Dua [def=" + def + ", use=" + use + ", target=" + target + ", var=" + var + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((def == null) ? 0 : def.hashCode());
		result = prime * result + status;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((use == null) ? 0 : use.hashCode());
		result = prime * result + ((var == null) ? 0 : var.hashCode());
		return result;
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
		if (def == null) {
			if (other.def != null) {
				return false;
			}
		} else if (!def.equals(other.def)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (target == null) {
			if (other.target != null) {
				return false;
			}
		} else if (!target.equals(other.target)) {
			return false;
		}
		if (use == null) {
			if (other.use != null) {
				return false;
			}
		} else if (!use.equals(other.use)) {
			return false;
		}
		if (var == null) {
			if (other.var != null) {
				return false;
			}
		} else if (!var.equals(other.var)) {
			return false;
		}
		return true;
	}

}
