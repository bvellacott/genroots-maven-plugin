package org.smicon.rest.functionality.modelmeta;

import javax.persistence.Entity;

import papu.annotations.Model;

public final class ModelValidationFunctions
{
	
	public static boolean isModel(Class<?> aClass)
	{
		Model modelAnnotation = aClass.getAnnotation(Model.class);
		Entity entityAnnotation = aClass.getAnnotation(Entity.class);
		
		return modelAnnotation != null && entityAnnotation != null;
	}

}
