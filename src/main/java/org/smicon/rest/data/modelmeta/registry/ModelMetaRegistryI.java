package org.smicon.rest.data.modelmeta.registry;

import java.util.Collection;
import java.util.Set;

import org.smicon.rest.data.metas.ModelMeta;

public interface ModelMetaRegistryI
{

	Class<?> getModel(String aEntityClassName);

	boolean containsModel(String aEntityClassName);

	boolean containsModel(Class<?> aModel);
	
	ModelMeta getModelMeta(String aEntityClassName);

	ModelMeta getModelMeta(Class<?> aModel) throws Exception;

	Set<Class<?>> getModels();
	
	void registerModel(Class<?> aModel);

	void registerModels(Collection<Class<?>> aModels);

	void registerModels(Class<?>[] aModels);
	
	Class<?> unregisterModel(Class<?> aModel);
	
	Class<?> unregisterModel(String aModelClassName);

}
