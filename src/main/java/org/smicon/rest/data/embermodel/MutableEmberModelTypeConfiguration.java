package org.smicon.rest.data.embermodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MutableEmberModelTypeConfiguration
implements
MutableEmberModelTypeConfigurationI
{

	private Set<Class> javaTypesRepresentingEmberNumbers;
	private Set<Class> javaTypesRepresentingEmberBooleans;
	private Set<Class> javaTypesRepresentingEmberDates;
	private Set<Class> javaTypesRepresentingEmberStrings;
	private Set<Class> javaTypesRepresentingEmberArrays;
	private Set<Class> javaTypesRepresentingEmberObjects;
	
	public MutableEmberModelTypeConfiguration()
	{
		javaTypesRepresentingEmberNumbers = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_numbers);
		javaTypesRepresentingEmberBooleans = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_booleans);
		javaTypesRepresentingEmberDates = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_dates);
		javaTypesRepresentingEmberStrings = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_strings);
		javaTypesRepresentingEmberArrays = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_arrays);
		javaTypesRepresentingEmberObjects = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_objects);
	}
	
	@Override
	public Set<Class> getJavaTypesRepresentingEmberNumbers()
	{
		return this.javaTypesRepresentingEmberNumbers;
	}

	@Override
	public void setJavaTypesRepresentingEmberNumbers(Set<Class> javaTypesRepresentingEmberNumbers)
	{
		this.javaTypesRepresentingEmberNumbers = javaTypesRepresentingEmberNumbers;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberBooleans()
	{
		return this.javaTypesRepresentingEmberBooleans;
	}

	@Override
	public void setJavaTypesRepresentingEmberBooleans(Set<Class> javaTypesRepresentingEmberBooleans)
	{
		this.javaTypesRepresentingEmberBooleans = javaTypesRepresentingEmberBooleans;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberDates()
	{
		return this.javaTypesRepresentingEmberDates;
	}

	@Override
	public void setJavaTypesRepresentingEmberDates(Set<Class> javaTypesRepresentingEmberDates)
	{
		this.javaTypesRepresentingEmberDates = javaTypesRepresentingEmberDates;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberStrings()
	{
		return this.javaTypesRepresentingEmberStrings;
	}

	@Override
	public void setJavaTypesRepresentingEmberStrings(Set<Class> javaTypesRepresentingEmberStrings)
	{
		this.javaTypesRepresentingEmberStrings = javaTypesRepresentingEmberStrings;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberArrays()
	{
		return this.javaTypesRepresentingEmberArrays;
	}

	@Override
	public void setJavaTypesRepresentingEmberArrays(Set<Class> javaTypesRepresentingEmberArrays)
	{
		this.javaTypesRepresentingEmberArrays = javaTypesRepresentingEmberArrays;
	}

	@Override
	public Set<Class> getJavaTypesRepresentingEmberObjects()
	{
		return this.javaTypesRepresentingEmberObjects;
	}

	@Override
	public void setJavaTypesRepresentingEmberObjects(Set<Class> javaTypesRepresentingEmberObjects)
	{
		this.javaTypesRepresentingEmberObjects = javaTypesRepresentingEmberObjects;
	}

}
