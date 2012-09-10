/* Copyright (c) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 */
package net.bioclipse.nm.business;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.nm.domain.Material;

import org.apache.log4j.Logger;
import org.bitbucket.nanojava.io.Serializer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.xmlcml.cml.element.CMLMolecule;

public class NmManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(NmManager.class);

    public Material newMaterial() {
    	logger.debug("Creating a new domain object implementation...");
    	return new Material();
    }

    public void save(Material material, String filename)
    throws UnsupportedEncodingException, CoreException {
    	save(material, ResourcePathTransformer.getInstance().transform(filename), null);
    }

    public void save(Material material, IFile target, IProgressMonitor monitor)
    throws UnsupportedEncodingException, CoreException {
    	if (monitor == null) monitor = new NullProgressMonitor();

    	CMLMolecule cmlMaterial = Serializer.toCML(material.getInternalModel());
    	if (target.exists()) {
            target.setContents(
                    new ByteArrayInputStream(cmlMaterial.toXML()
                            .getBytes("US-ASCII")),
                            false,
                            true, // overwrite
                            monitor );
        } else {
            target.create(
                    new ByteArrayInputStream(cmlMaterial.toXML()
                            .getBytes("US-ASCII")),
                            false,
                            monitor );
        }
    	monitor.done();
    }

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "nm";
    }
}
