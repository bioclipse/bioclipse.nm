package net.bioclipse.nm.domain;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.nm.business.NmManager;

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.Nanomaterial;
import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.ErrorlessMeasurementValue;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.data.measurement.MeasurementRange;
import org.bitbucket.nanojava.data.measurement.MeasurementValue;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import com.github.jqudt.Unit;
import com.github.jqudt.onto.units.CountingUnit;
import com.github.jqudt.onto.units.EnergyUnit;
import com.github.jqudt.onto.units.LengthUnit;

public class Demo {

	public static void main(String[] args) throws BioclipseException {
		NmManager nmManager = new NmManager();
		Material material = nmManager.newMaterial("metal oxide");
		Nanomaterial nm = material.getInternalModel();
		
		// the material type
		nm.setType(MaterialType.METALOXIDE);

		// size: 5 +/- 2 nm, TEM cannot really be set right now
		IMeasurement temSize = new MeasurementValue(EndPoints.DIAMETER_TEM, 5, 2, LengthUnit.NM);
		nm.addCharacterization(temSize);

		// size: 34-47 nm, DLS
		IMeasurement dlsSize = new MeasurementRange(EndPoints.DIAMETER_DLS, 34, 47, LengthUnit.NM);
		nm.addCharacterization(dlsSize);
		
		// just a single thing now, no core, no coating
		nm.setChemicalComposition(
			MolecularFormulaManipulator.getMajorIsotopeMolecularFormula(
				"Fe2O3",
				DefaultChemObjectBuilder.getInstance()
			)
		);
		
		// no purity yet
		IMeasurement purity = new ErrorlessMeasurementValue(EndPoints.PURITY, 99.5, CountingUnit.PERCENT);
		nm.addCharacterization(purity);

		// zeta potential
		IMeasurement zetaPot = new ErrorlessMeasurementValue(EndPoints.ZETA_POTENTIAL, -24.5, EnergyUnit.EV);
		// OK, the next does not work yet
		List<IMeasurement> conditions = new ArrayList<IMeasurement>();
		IMeasurement pH = new ErrorlessMeasurementValue(EndPoints.PH, 7.0, (Unit)null);
		conditions.add(pH);
		zetaPot.setConditions(conditions);
		nm.addCharacterization(zetaPot);

	}
	
}
