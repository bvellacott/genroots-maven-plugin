package org.smicon.rest;

import java.beans.PropertyDescriptor;

public class EntityIdMeta {
	
	private Class<?> fieldType;
	private GetCompositeSetEntityPair readWritePair;
	private PropertyDescriptor pd;
	
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
	
	public PropertyDescriptor getPd() {
		return pd;
	}
	
	public void setPropertyDescriptor(PropertyDescriptor pd) {
		this.pd = pd;
	}
	
}
