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
package org.jacoco.core.internal.analysis.dua;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.analysis.dua.CoverageDuaImpl;
import org.jacoco.core.analysis.dua.IBundleDuaCoverage;
import org.jacoco.core.analysis.dua.IDuaClassCoverage;
import org.jacoco.core.analysis.dua.IDuaPackageCoverage;
import org.jacoco.core.analysis.dua.IDuaSourceFileCoverage;

/**
 * Implementation of {@link IBundleCoverage}.
 */
public class BundleDuaCoverageImpl extends CoverageDuaImpl implements
		IBundleDuaCoverage {

	private final Collection<IDuaPackageCoverage> packages;

	/**
	 * Creates a new instance of a bundle with the given name.
	 * 
	 * @param name
	 *            name of this bundle
	 * @param packages
	 *            collection of all packages contained in this bundle
	 */
	public BundleDuaCoverageImpl(final String name,
			final Collection<IDuaPackageCoverage> packages) {
		super(ElementType.BUNDLE, name);
		this.packages = packages;
		increment(packages);
	}

	/**
	 * Creates a new instance of a bundle with the given name. The packages are
	 * calculated from the given classes and source files.
	 * 
	 * @param name
	 *            name of this bundle
	 * @param collection
	 *            all classes in this bundle
	 * @param collection2
	 *            all source files in this bundle
	 */
	public BundleDuaCoverageImpl(final String name,
			final Collection<IDuaClassCoverage> collection,
			final Collection<IDuaSourceFileCoverage> collection2) {
		this(name, groupByPackage(collection, collection2));
	}

	private static Collection<IDuaPackageCoverage> groupByPackage(
			final Collection<IDuaClassCoverage> collection,
			final Collection<IDuaSourceFileCoverage> collection2) {
		final Map<String, Collection<IDuaClassCoverage>> classesByPackage = new HashMap<String, Collection<IDuaClassCoverage>>();
		for (final IDuaClassCoverage c : collection) {
			addByName(classesByPackage, c.getPackageName(), c);
		}

		final Map<String, Collection<IDuaSourceFileCoverage>> sourceFilesByPackage = new HashMap<String, Collection<IDuaSourceFileCoverage>>();
		for (final IDuaSourceFileCoverage s : collection2) {
			addByName(sourceFilesByPackage, s.getPackageName(), s);
		}

		final Set<String> packageNames = new HashSet<String>();
		packageNames.addAll(classesByPackage.keySet());
		packageNames.addAll(sourceFilesByPackage.keySet());

		final Collection<IDuaPackageCoverage> result = new ArrayList<IDuaPackageCoverage>();
		for (final String name : packageNames) {
			Collection<IDuaClassCoverage> c = classesByPackage.get(name);
			if (c == null) {
				c = Collections.emptyList();
			}
			Collection<IDuaSourceFileCoverage> s = sourceFilesByPackage.get(name);
			if (s == null) {
				s = Collections.emptyList();
			}
			result.add(new PackageDuaCoverageImpl(name, c, s));
		}
		return result;
	}

	private static <T> void addByName(final Map<String, Collection<T>> map,
			final String name, final T value) {
		Collection<T> list = map.get(name);
		if (list == null) {
			list = new ArrayList<T>();
			map.put(name, list);
		}
		list.add(value);
	}

	// === IBundleCoverage implementation ===

	public Collection<IDuaPackageCoverage> getPackages() {
		return packages;
	}

}
