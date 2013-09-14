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

import com.github.jqudt.Quantity;
import com.github.jqudt.Unit;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="Manager to convert units."
)
public interface IQudtManager extends IBioclipseManager {

	@Recorded
	@PublishedMethod(
		methodSummary="Create a quantity. Returns a Quantity object.",
		params="double value, Unit unit"
	)
	public Quantity createQuantity(double value, Unit unit);

	@Recorded
	@PublishedMethod(
		methodSummary="Create a unit from the QUDT unit URI. Returns a Unit object.",
		params="String unitURI"
	)
	public Unit createUnit(String unitURI);

	@Recorded
	@PublishedMethod(
		methodSummary="Find matching units from the unit symbol. Returns a list of Unit objects.",
		params="String unitSymbol"
	)
	public List<Unit> findUnits(String unitSymbol);

}
