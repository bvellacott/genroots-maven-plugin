package org.smicon.rest.data.mustachetemplate.controller.modelwrapper;

import java.beans.PropertyDescriptor;

import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.data.populators.abstracts.ModelPropertyPopulator;

public class ModelWrapperSimplePropertyPopulator 
extends
ModelPropertyPopulator
{
	
	public ModelWrapperSimplePropertyPopulator(PropertyDescriptor aPropertyDescriptor, ModelMetaRegistryI aRegistry) {
		super(aPropertyDescriptor, aRegistry);
	}
	
	public String getGetterName() {
		return this.propertyDescriptor.getReadMethod().getName();
	}
	
	public String getSetterName() {
		return this.propertyDescriptor.getWriteMethod().getName();
	}
	
}
