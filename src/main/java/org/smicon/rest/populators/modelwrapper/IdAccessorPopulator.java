package org.smicon.rest.populators.modelwrapper;

import java.beans.PropertyDescriptor;

public class IdAccessorPopulator
extends 
IdPropertyPopulator 
{
	protected PropertyDescriptor propertyDescriptor;

	public IdAccessorPopulator(PropertyDescriptor aPropertyDescriptor, int aIndex, String aUrlIdDelimiter) {
		super(aPropertyDescriptor.getPropertyType(), aPropertyDescriptor.getName(), aIndex, aUrlIdDelimiter);
		this.propertyDescriptor = aPropertyDescriptor;
	}
	
	public String getGetterName() {
		return this.propertyDescriptor.getReadMethod().getName();
	}

	public String getSetterName() {
		return this.propertyDescriptor.getWriteMethod().getName();
	}
	
}
