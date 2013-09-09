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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.bioclipse.core.ResourcePathTransformer;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMaterial;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.nm.domain.Material;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import org.apache.log4j.Logger;
import org.bitbucket.nanojava.data.Nanomaterial;
import org.bitbucket.nanojava.io.Deserializer;
import org.bitbucket.nanojava.io.Serializer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;

public class NmManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(NmManager.class);

    public Material newMaterial() {
    	logger.debug("Creating a new domain object implementation...");
    	return new Material();
    }

    public Material load(IFile file, IProgressMonitor monitor)
    		throws IOException, BioclipseException, CoreException {
    	if (monitor == null) monitor = new NullProgressMonitor();

    	Document nmxDoc;
		try {
			nmxDoc = new CMLBuilder().buildEnsureCML(file.getContents());
		} catch (ParsingException exception) {
			throw new BioclipseException("Error while reading the NMX document: " + exception.getMessage(), exception);
		}
    	Element rootElem = nmxDoc.getRootElement();
    	if (rootElem instanceof CMLMolecule) { // requirement
    		CMLMolecule cmlMaterial = (CMLMolecule)rootElem;
    		Nanomaterial nmaterial = Deserializer.fromCML(cmlMaterial);
    		Material material = new Material(nmaterial);
    		return material;
    	}
    	
    	throw new BioclipseException("Document is not in the NMX format.");
    }

    public Material fromString(String nmxFile, IProgressMonitor monitor)
    		throws IOException, BioclipseException, CoreException {
    	if (monitor == null) monitor = new NullProgressMonitor();

    	Document nmxDoc;
		try {
			nmxDoc = new CMLBuilder().buildEnsureCML(
				new ByteArrayInputStream(nmxFile.getBytes())
			);
		} catch (ParsingException exception) {
			throw new BioclipseException("Error while reading the NMX document: " + exception.getMessage(), exception);
		}
    	Element rootElem = nmxDoc.getRootElement();
		System.out.println("rootElement: " + rootElem.getLocalName());
    	if (rootElem instanceof CMLMolecule) { // requirement
    		CMLMolecule cmlMaterial = (CMLMolecule)rootElem;
    		Nanomaterial nmaterial = Deserializer.fromCML(cmlMaterial);
    		Material material = new Material(nmaterial);
    		return material;
    	} else if (rootElem instanceof CMLList) { // requirement; output from AMBIT
    		System.out.println("x");
    		CMLList list = (CMLList)rootElem;
    		List<CMLElement> childElements = list.getChildCMLElements();
    		System.out.println("childs: " + childElements.size());
    		if (childElements.size() == 1) {
        		System.out.println("xx");
    			CMLElement element = childElements.get(0);
    			System.out.println("child: " + element.getLocalName());
    			if (element instanceof CMLMolecule) {
    	    		System.out.println("xxx");
    				CMLMolecule cmlMaterial = (CMLMolecule)element;
    				Nanomaterial nmaterial = Deserializer.fromCML(cmlMaterial);
    				Material material = new Material(nmaterial);
    				return material;
    			}
    		}
    	}
    	
    	throw new BioclipseException("Document is not in the NMX format.");
    }
    
    public void save(IMaterial material, String filename)
    throws UnsupportedEncodingException, CoreException, BioclipseException {
    	save(material, ResourcePathTransformer.getInstance().transform(filename), null);
    }

    public void save(IMaterial imaterial, IFile target, IProgressMonitor monitor)
    throws UnsupportedEncodingException, CoreException, BioclipseException {
    	if (monitor == null) monitor = new NullProgressMonitor();
    	if (!(imaterial instanceof Material))
    		throw new BioclipseException("Currently I can only save Material implementations of IMaterial.");

    	Material material = (Material)imaterial;
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
