package org.smicon.rest.emberwiring.embermodel;

import org.smicon.rest.emberwiring.embermodel.EmberModelTypeConfigurationI;

public class EmberModels
{
	public static final EmberModelTypeConfigurationI default_instance = new DefaultEmberModelTypeConfiguration();
	
	public static EmberModelTypeConfigurationI getDefaultConfiguration()
	{
		return default_instance;
	}
	
	public static EmberModelTypeConfigurationI newConfiguration()
	{
		return new DefaultEmberModelTypeConfiguration();
	}
	
}
