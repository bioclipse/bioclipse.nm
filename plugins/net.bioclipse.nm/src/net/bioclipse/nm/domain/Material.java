/* Copyright (c) 2012  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package net.bioclipse.nm.domain;

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.Nanomaterial;

import net.bioclipse.core.domain.BioObject;
import net.bioclipse.core.domain.IMaterial;

public class Material extends BioObject implements IMaterial {

	private Nanomaterial internalModel;

	public Material() {
		this.internalModel = new Nanomaterial();
	}

	public Material(MaterialType type) {
		this.internalModel = new Nanomaterial(type);
	}

	public Material(Nanomaterial nmaterial) {
		this.internalModel = nmaterial;
	}

	public Nanomaterial getInternalModel() {
		return internalModel;
	}

}
