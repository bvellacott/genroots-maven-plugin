package org.smicon.rest.emberwiring.embermodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultEmberModelTypeConfiguration
implements 
EmberModelTypeConfigurationI
{
	Set<Class> javaTypesRepresentingEmberNumbers;
	Set<Class> javaTypesRepresentingEmberBooleans;
	Set<Class> javaTypesRepresentingEmberDates;
	Set<Class> javaTypesRepresentingEmberStrings;
	Set<Class> javaTypesRepresentingEmberArrays;
	Set<Class> javaTypesRepresentingEmberObjects;
	
	DefaultEmberModelTypeConfiguration()
	{
		javaTypesRepresentingEmberNumbers = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_numbers);
		javaTypesRepresentingEmberBooleans = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_booleans);
		javaTypesRepresentingEmberDates = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_dates);
		javaTypesRepresentingEmberStrings = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_strings);
		javaTypesRepresentingEmberArrays = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_arrays);
		javaTypesRepresentingEmberObjects = new HashSet<Class>((Collection<? extends Class>) default_java_types_representing_ember_objects);
	}

	public Set<Class> getJavaTypesRepresentingEmberNumbers()
	{
		return this.javaTypesRepresentingEmberNumbers;
	}

	public void setJavaTypesRepresentingEmberNumbers(Set<Class> javaTypesRepresentingEmberNumbers)
	{
		this.javaTypesRepresentingEmberNumbers = javaTypesRepresentingEmberNumbers;
	}

	public Set<Class> getJavaTypesRepresentingEmberBooleans()
	{
		return this.javaTypesRepresentingEmberBooleans;
	}

	public void setJavaTypesRepresentingEmberBooleans(Set<Class> javaTypesRepresentingEmberBooleans)
	{
		this.javaTypesRepresentingEmberBooleans = javaTypesRepresentingEmberBooleans;
	}

	public Set<Class> getJavaTypesRepresentingEmberDates()
	{
		return this.javaTypesRepresentingEmberDates;
	}

	public void setJavaTypesRepresentingEmberDates(Set<Class> javaTypesRepresentingEmberDates)
	{
		this.javaTypesRepresentingEmberDates = javaTypesRepresentingEmberDates;
	}

	public Set<Class> getJavaTypesRepresentingEmberStrings()
	{
		return this.javaTypesRepresentingEmberStrings;
	}

	public void setJavaTypesRepresentingEmberStrings(Set<Class> javaTypesRepresentingEmberStrings)
	{
		this.javaTypesRepresentingEmberStrings = javaTypesRepresentingEmberStrings;
	}

	public Set<Class> getJavaTypesRepresentingEmberArrays()
	{
		return this.javaTypesRepresentingEmberArrays;
	}

	public void setJavaTypesRepresentingEmberArrays(Set<Class> javaTypesRepresentingEmberArrays)
	{
		this.javaTypesRepresentingEmberArrays = javaTypesRepresentingEmberArrays;
	}

	public Set<Class> getJavaTypesRepresentingEmberObjects()
	{
		return this.javaTypesRepresentingEmberObjects;
	}

	public void setJavaTypesRepresentingEmberObjects(Set<Class> javaTypesRepresentingEmberObjects)
	{
		this.javaTypesRepresentingEmberObjects = javaTypesRepresentingEmberObjects;
	}

}
