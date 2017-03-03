package fr.tbr.iam.web.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement( name = "attribute" )
public class Attribute {
	
	@XmlType(name="type")
	@XmlEnum
	public  enum AttributeType {
			TEXT, NUMBER, EMAIL, DATE, PASSWORD
		}
	 
	private int id;
	private String name;
	private boolean isMandatory;
	private AttributeType type;

	public Attribute() {
		super();
	}

	public Attribute(int id, String name, boolean isMandatory,
			AttributeType type) {
		super();
		this.id = id;
		this.name = name;
		this.isMandatory = isMandatory;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}
	
	@XmlElement( name = "name" )
	public void setName(String name) {
		this.name = name;
	}

	
	public boolean isMandatory() {
		return isMandatory;
	}
	
	public boolean getIsMandatory() {
		return isMandatory;
	}

	@XmlAttribute( name = "isMandatory" )
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public AttributeType getType() {
		return type;
	}

	@XmlAttribute( name = "type" )
	public void setType(AttributeType type) {
		this.type = type;
	}

/*	public String getType() {
		return type.toString();
	}

	
	@XmlAttribute( name = "type" )
	public void setType(String type) {
		if(type.equalsIgnoreCase(AttributeType.TEXT.toString())){
			this.type= AttributeType.TEXT;
		}
	}*/
}
