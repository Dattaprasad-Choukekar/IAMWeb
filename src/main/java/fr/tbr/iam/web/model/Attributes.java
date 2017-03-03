package fr.tbr.iam.web.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "attributes" )
public class Attributes {
	private List<Attribute> attributes;

	public List<Attribute> getAttributes() {
		return attributes;
	}

	@XmlElement( name = "attribute" )
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
}
