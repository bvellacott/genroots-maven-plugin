package org.smicon.rest.populators.embermodel;

import java.beans.PropertyDescriptor;

import org.smicon.rest.metas.Metas;
import org.smicon.rest.populators.abstracts.ModelPropertyPopulator;

public class EmberModelSimplePropertyPopulator
extends
ModelPropertyPopulator
{

	public EmberModelSimplePropertyPopulator(PropertyDescriptor aPropertyDescriptor)
	{
		super(aPropertyDescriptor);
	}

	public String getJsType()
	{
		return Metas.getEmberJsType(this.propertyDescriptor.getPropertyType());
	}

}