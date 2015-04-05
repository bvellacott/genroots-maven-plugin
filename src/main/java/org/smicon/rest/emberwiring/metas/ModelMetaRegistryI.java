package org.smicon.rest.emberwiring.metas;

import java.util.Collection;
import java.util.Set;

public interface ModelMetaRegistryI
{

	Class<?> getModel(String aEntityClassName);

	boolean containsModel(String aEntityClassName);

	boolean containsModel(Class<?> aModel);
	
	ModelMetaI getModelMeta(String aEntityClassName);

	ModelMetaI getModelMeta(Class<?> aModel) throws Exception;

	Set<Class<?>> getModels();
	
}
