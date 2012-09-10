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

import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.nm.domain.Material;

import org.apache.log4j.Logger;

public class NmManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(NmManager.class);

    public Material newMaterial() {
    	logger.debug("Creating a new domain object implementation...");
    	return new Material();
    }

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "nm";
    }
}
