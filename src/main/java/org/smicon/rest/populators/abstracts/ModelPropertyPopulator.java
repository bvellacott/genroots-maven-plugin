package org.smicon.rest.populators.abstracts;

import java.beans.PropertyDescriptor;

public abstract class ModelPropertyPopulator
extends
TemplatePopulator
{

	protected PropertyDescriptor propertyDescriptor;

	public ModelPropertyPopulator(PropertyDescriptor aPropertyDescriptor)
	{
		this.propertyDescriptor = aPropertyDescriptor;
	}

	public String getType()
	{
		return this.propertyDescriptor.getPropertyType().getSimpleName();
	}

	public String getParameterName()
	{
		return this.propertyDescriptor.getName();
	}

}
