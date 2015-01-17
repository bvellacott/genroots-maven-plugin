package org.smicon.rest.populators.modelwrapper;

import java.beans.PropertyDescriptor;

import org.smicon.rest.populators.abstracts.TemplatePopulator;

public class ModelWrapperSimplePropertyPopulator 
extends
TemplatePopulator
{
	
	protected PropertyDescriptor propertyDescriptor;

	public ModelWrapperSimplePropertyPopulator(PropertyDescriptor aPropertyDescriptor) {
		this.propertyDescriptor = aPropertyDescriptor;
	}
	
	public String getType() {
		return this.propertyDescriptor.getPropertyType().getSimpleName();
	}
	
	public String getGetterName() {
		return this.propertyDescriptor.getReadMethod().getName();
	}
	
	public String getSetterName() {
		return this.propertyDescriptor.getWriteMethod().getName();
	}
	
	public String getParameterName() {
		return this.propertyDescriptor.getName();
	}
	
}
