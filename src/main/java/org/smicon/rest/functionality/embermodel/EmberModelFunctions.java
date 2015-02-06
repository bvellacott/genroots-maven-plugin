package org.smicon.rest.functionality.embermodel;

import org.smicon.rest.data.embermodel.EmberModelTypeConfigurationI;

public final class EmberModelFunctions
{

	public static String getEmberJsType(Class<?> aJavaType, EmberModelTypeConfigurationI aConfiguration)
	{
	    if(aConfiguration.getJavaTypesRepresentingEmberBooleans().contains(aJavaType))
	    {
	    	return "boolean";
	    }
	    if(aConfiguration.getJavaTypesRepresentingEmberNumbers().contains(aJavaType))
	    {
	    	return "number";
	    }
	    if(aConfiguration.getJavaTypesRepresentingEmberDates().contains(aJavaType))
	    {
	    	return "date";
	    }
	    if(aConfiguration.getJavaTypesRepresentingEmberStrings().contains(aJavaType))
	    {
	    	return "string";
	    }
	    for(Object o : aConfiguration.getJavaTypesRepresentingEmberArrays())
	    {
	    	Class<?> cls = (Class<?>)o;
	    	if(cls.isAssignableFrom(aJavaType)) return "array" ;
	    }
	    
	    return "object";
	}
	
}
