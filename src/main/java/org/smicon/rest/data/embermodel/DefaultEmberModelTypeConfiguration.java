package org.smicon.rest.data.embermodel;

import java.util.Set;

public class DefaultEmberModelTypeConfiguration
implements
EmberModelTypeConfigurationI
{

	private static final DefaultEmberModelTypeConfiguration instance = new DefaultEmberModelTypeConfiguration();
	
	private DefaultEmberModelTypeConfiguration()
	{}
	
	public static DefaultEmberModelTypeConfiguration getInstance()
	{
		return instance;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberNumbers()
	{
		return (Set<Class>) default_java_types_representing_ember_numbers;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberBooleans()
	{
		return (Set<Class>) default_java_types_representing_ember_booleans;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberDates()
	{
		return (Set<Class>) default_java_types_representing_ember_dates;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberStrings()
	{
		return (Set<Class>) default_java_types_representing_ember_strings;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberArrays()
	{
		return (Set<Class>) default_java_types_representing_ember_arrays;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberObjects()
	{
		return (Set<Class>) default_java_types_representing_ember_objects;
	}

}
