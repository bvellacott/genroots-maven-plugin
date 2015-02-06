package org.smicon.rest.data.populators.abstracts;

import java.beans.PropertyDescriptor;

import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;

public abstract class ModelPropertyPopulator
extends
TemplatePopulator
{

	protected PropertyDescriptor propertyDescriptor;
	private ModelMetaRegistryI registry;

	public ModelPropertyPopulator(PropertyDescriptor aPropertyDescriptor, ModelMetaRegistryI aRegistry)
	{
		this.propertyDescriptor = aPropertyDescriptor;
		this.registry = aRegistry;
	}

	public String getType()
	{
		return this.propertyDescriptor.getPropertyType().getSimpleName();
	}

	public String getParameterName()
	{
		return this.propertyDescriptor.getName();
	}
	
	public ModelMetaRegistryI getRegistry()
	{
		return this.registry;
	}

}
