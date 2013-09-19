package net.bioclipse.nm.ui.editors.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author ola
 *
 */
public class ViewModel {
	// The model to bind
	private List<Property> props = new LinkedList<Property>();
//	{
//		props.add(new Property("Steve", "Northover"));
//		props.add(new Property("Grant", "Gayed"));
//		props.add(new Property("Veronika", "Irvine"));
//		props.add(new Property("Mike", "Wilson"));
//		props.add(new Property("Christophe", "Cornu"));
//		props.add(new Property("Lynne", "Kues"));
//		props.add(new Property("Silenio", "Quarti"));
//	}

	public List getProps() {
		return props;
	}

	public void setProps(List props) {
		this.props = props;
	}

	public ViewModel(List<Property> props) {
		super();
		this.props = props;
	}

	
	
}