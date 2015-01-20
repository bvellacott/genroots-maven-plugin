package org.smicon.rest.populators.modelwrapper;

import java.beans.PropertyDescriptor;

import org.smicon.rest.populators.abstracts.ModelPropertyPopulator;

public class ModelWrapperSimplePropertyPopulator 
extends
ModelPropertyPopulator
{
	
	public ModelWrapperSimplePropertyPopulator(PropertyDescriptor aPropertyDescriptor) {
		super(aPropertyDescriptor);
	}
	
	public String getGetterName() {
		return this.propertyDescriptor.getReadMethod().getName();
	}
	
	public String getSetterName() {
		return this.propertyDescriptor.getWriteMethod().getName();
	}
	
}
