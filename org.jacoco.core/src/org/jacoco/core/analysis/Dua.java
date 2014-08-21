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

public class Dua implements IDua {

	private final int def;

	private final int use;

	private final int target;

	private final int var;

	public Dua(final int def, final int use, final int var) {
		this.def = def;
		this.use = use;
		this.var = var;
		target = -1;
	}

	public Dua(final int def, final int use, final int target, final int var) {
		this.def = def;
		this.use = use;
		this.target = target;
		this.var = var;
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
	public int getDef() {
		return def;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getUse()
	 */
	public int getUse() {
		return use;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getTarget()
	 */
	public int getTarget() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jacoco.core.analysis.IDua#getVar()
	 */
	public int getVar() {
		return var;
	}

}
