/*******************************************************************************
 * Copyright (c) 2013  Egon Willighagen <egon.willighagen@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.qudt.business;

import java.util.List;

import net.bioclipse.managers.business.IBioclipseManager;

import com.github.jqudt.Quantity;
import com.github.jqudt.Unit;
import com.github.jqudt.onto.UnitFactory;

public class QudtManager implements IBioclipseManager {

//    private static final Logger logger = Logger.getLogger(QudtManager.class);

	public Quantity createQuantity(double value, Unit unit) {
		return new Quantity(value, unit);
	}

	public Unit createUnit(String unitURI) {
		return UnitFactory.getInstance().getUnit(unitURI);
	}

	public List<Unit> findUnits(String unitSymbol) {
		return UnitFactory.getInstance().findUnits(unitSymbol);
	}

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "qudt";
    }
}
