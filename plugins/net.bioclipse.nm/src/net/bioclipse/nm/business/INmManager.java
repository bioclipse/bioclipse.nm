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

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.nm.domain.Material;

@PublishedClass(
    value="Manager to handle (nano)materials."
)
public interface INmManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        methodSummary=
            "Returns the security token for the service."
    )
    public Material newMaterial();

}
