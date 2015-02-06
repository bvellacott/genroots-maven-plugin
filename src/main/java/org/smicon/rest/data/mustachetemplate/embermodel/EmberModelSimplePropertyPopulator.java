package org.smicon.rest.data.mustachetemplate.embermodel;

import java.beans.PropertyDescriptor;

import org.smicon.rest.data.embermodel.EmberModelTypeConfigurationI;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.data.populators.abstracts.ModelPropertyPopulator;
import org.smicon.rest.functionality.embermodel.EmberModelFunctions;

public class EmberModelSimplePropertyPopulator
extends
ModelPropertyPopulator
{
	EmberModelTypeConfigurationI configuration;

	public EmberModelSimplePropertyPopulator(PropertyDescriptor aPropertyDescriptor, ModelMetaRegistryI aRegistry, EmberModelTypeConfigurationI aConfiguration)
	{
		super(aPropertyDescriptor, aRegistry);
		this.configuration = aConfiguration;
	}

	public EmberModelTypeConfigurationI getConfiguration()
	{
		return this.configuration;
	}

	public void setConfiguration(EmberModelTypeConfigurationI configuration)
	{
		this.configuration = configuration;
	}

	public String getJsType()
	{
		return EmberModelFunctions.getEmberJsType(this.propertyDescriptor.getPropertyType(), this.getConfiguration());
	}

}