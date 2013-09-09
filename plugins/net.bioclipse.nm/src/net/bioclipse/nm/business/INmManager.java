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

import java.io.UnsupportedEncodingException;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMaterial;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.nm.domain.Material;

import org.eclipse.core.runtime.CoreException;

@PublishedClass(
    value="Manager to handle (nano)materials."
)
public interface INmManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        methodSummary=
            "Creates a new material."
    )
    public Material newMaterial();

    @Recorded
    @PublishedMethod(params = "IMaterial material, String filename",
            methodSummary="Saves a material to file in the NMX format.")
    public void save(IMaterial material, String filename)
    	          throws BioclipseException, UnsupportedEncodingException, CoreException;
    
    @Recorded
    @PublishedMethod(params = "String filename",
            methodSummary="Loads a material from a NMX file.")
    public Material load(String filename)
	          throws BioclipseException, UnsupportedEncodingException, CoreException;

    @Recorded
    @PublishedMethod(params = "String nmxFile",
            methodSummary="Parses a material from a NMX encoded String.")
    public Material fromString(String nmxFile)
	          throws BioclipseException, UnsupportedEncodingException, CoreException;
}
