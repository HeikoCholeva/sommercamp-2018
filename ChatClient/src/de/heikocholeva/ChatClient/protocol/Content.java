package de.heikocholeva.ChatClient.protocol;

public class Content {

	private String property;
	private Object value;
	
	public Content setProperty(String property) {
		this.property = property;
		return this;
	}
	
	public Content setValue(Object value) {
		this.value = value;
		return this;
	}
	
	public String getProperty() {
		return this.property;
	}
	
	public Object getValue() {
		return this.value;
	}

	public String toString() {
		if(getValue() instanceof String) return "\"" + getProperty() + "\":\"" + getValue() + "\"";
		return "\"" + getProperty() + "\":" + getValue();
	}
}
