package org.smicon.rest.functionality.embermodel.staticfactory;

import org.smicon.rest.data.embermodel.DefaultEmberModelTypeConfiguration;
import org.smicon.rest.data.embermodel.MutableEmberModelTypeConfiguration;
import org.smicon.rest.data.embermodel.MutableEmberModelTypeConfigurationI;

public class EmberModels
{
	
	public static DefaultEmberModelTypeConfiguration getDefaultConfiguration()
	{
		return DefaultEmberModelTypeConfiguration.getInstance();
	}
	
	public static MutableEmberModelTypeConfigurationI newConfiguration()
	{
		return new MutableEmberModelTypeConfiguration();
	}
	
}
