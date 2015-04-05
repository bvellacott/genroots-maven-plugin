package org.smicon.rest.emberwiring.metas;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.smicon.rest.emberwiring.types.id.IdType;

import papu.annotations.Model;

public interface ModelMetaI
{

	Class<?> getModelClass();

	BeanInfo getModelBeanInfo() throws IntrospectionException;
	
	Class<?> getIdClass();

	BeanInfo getIdBeanInfo();

	Class<? extends IdType> getIdType();

	String getSingularIdName();

	Map<String, SimplePropertyMetaI> getIdPathParameterMetas();

	Map<String, SimplePropertyMetaI> getEmbeddedIdPathParameterMetas();

	Map<String, SimplePropertyMetaI> getSimpleProperties();

	Map<String, SimplePropertyMetaI> getSimpleCollectionProperties();

	Map<String, ModelPropertyMetaI> getModelPropertiesWithSimpleIds();

	Map<String, ModelPropertyMetaI> getModelPropertiesWithEmbeddedIds();

	Map<String, ModelPropertyMetaI> getModelPropertiesWithCompositeIds();

	Map<String, ModelPropertyMetaI> getModelCollectionProperties();

	Model getModelAnnotation();
	
}
