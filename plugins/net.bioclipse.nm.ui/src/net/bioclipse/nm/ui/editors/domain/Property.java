package net.bioclipse.nm.ui.editors.domain;
// The data model class. This is normally a persistent class of some sort.
public class Property extends AbstractModelObject {
	String key = "a";
	String value = "b";
	
	public Property(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		String oldValue = this.value;
		this.value = value;
		firePropertyChange("name", oldValue, value);
	}

}