package org.smicon.rest.data.embermodel;

import java.util.Set;

public interface MutableEmberModelTypeConfigurationI
extends
EmberModelTypeConfigurationI
{

	public void setJavaTypesRepresentingEmberNumbers(Set<Class> javaTypesRepresentingEmberNumbers);
	
	public void setJavaTypesRepresentingEmberBooleans(Set<Class> javaTypesRepresentingEmberBooleans);
	
	public void setJavaTypesRepresentingEmberDates(Set<Class> javaTypesRepresentingEmberDates);
	
	public void setJavaTypesRepresentingEmberStrings(Set<Class> javaTypesRepresentingEmberStrings);
	
	public void setJavaTypesRepresentingEmberArrays(Set<Class> javaTypesRepresentingEmberArrays);
	
	public void setJavaTypesRepresentingEmberObjects(Set<Class> javaTypesRepresentingEmberObjects);
	
}
