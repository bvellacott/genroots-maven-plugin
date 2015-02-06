package org.smicon.rest.data.metas;

import java.beans.PropertyDescriptor;

import org.smicon.rest.GetCompositeSetEntityPair;

public class EntityIdMeta {
	
	private Class<?> fieldType;
	private GetCompositeSetEntityPair readWritePair;
	private PropertyDescriptor pd;
	private boolean isGenerated;
	private String name;
	
	public EntityIdMeta(String aName) {
		this.name = aName;
	}
	
	public Class<?> getFieldType() {
		return fieldType;
	}
	
	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}
	
	public GetCompositeSetEntityPair getReadWritePair() {
		return readWritePair;
	}
	
	public void setReadWritePair(GetCompositeSetEntityPair readWritePair) {
		this.readWritePair = readWritePair;
	}
	
	public PropertyDescriptor getPropertyDescriptor() {
		return pd;
	}
	
	public void setPropertyDescriptor(PropertyDescriptor pd) {
		this.pd = pd;
	}

	public boolean isGenerated() {
		return isGenerated;
	}

	public void setGenerated(boolean isGenerated) {
		this.isGenerated = isGenerated;
	}

	public String getName() {
		return name;
	}
	
}
