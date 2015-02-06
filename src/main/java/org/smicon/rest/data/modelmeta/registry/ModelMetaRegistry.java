package org.smicon.rest.data.modelmeta.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.Entity;

import org.smicon.rest.data.metas.ModelMeta;
import org.smicon.rest.functionality.modelmeta.ModelValidationFunctions;
import org.smicon.rest.functionality.modelmeta.staticfactory.ModelMetas;

import papu.annotations.Model;

import com.google.common.collect.ImmutableSet;

public class ModelMetaRegistry
implements
ModelMetaRegistryI
{

	private HashMap<String, Class<?>> modelsBySimpleName;
	private HashMap<String, Class<?>> modelsByFullName;
	
	// Cache
	
	private HashMap<Class<?>, ModelMeta> entityMetaCache = new HashMap<Class<?>, ModelMeta>();

	public ModelMetaRegistry()
	{
		modelsBySimpleName = new HashMap<String, Class<?>>();
		modelsByFullName = new HashMap<String, Class<?>>();
	}
	
	@Override
	public Class<?> getModel(String aEntityClassName)
	{
		Class<?> model = this.modelsByFullName.get(aEntityClassName);
		if(model == null)
		{
			model = this.modelsBySimpleName.get(aEntityClassName);
		}
		return model;
	}

	@Override
	public boolean containsModel(String aEntityClassName)
	{
		return this.modelsByFullName.containsKey(aEntityClassName) || this.modelsBySimpleName.containsKey(aEntityClassName);
	}

	@Override
	public boolean containsModel(Class<?> aModel)
	{
		return this.modelsByFullName.containsKey(aModel.getName());
	}

	@Override
	public ModelMeta getModelMeta(String aEntityClassName)
	{
		return null;
	}

	@Override
	public ModelMeta getModelMeta(Class<?> aModel) throws Exception
	{
		if(!this.containsModel(aModel))
		{
			return null;
		}
		
		ModelMeta meta = entityMetaCache.get(aModel);
		if (meta != null)
		{
			return meta;
		}

		meta = ModelMetas.newMeta();
		entityMetaCache.put(aModel, meta);
		ModelMetas.initMeta(meta);
		
		return meta;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<?>> getModels()
	{
		ImmutableSet<?> modelSet = ImmutableSet.builder().addAll(modelsBySimpleName.values()).addAll(modelsByFullName.values()).build();
		return (Set<Class<?>>)modelSet;
	}

	@Override
	public void registerModel(Class<?> aModel)
	{
		if(ModelValidationFunctions.isModel(aModel))
		{
			this.modelsByFullName.put(aModel.getName(), aModel);
			this.modelsBySimpleName.put(aModel.getSimpleName(), aModel);
		}
	}
	
	@Override
	public void registerModels(Collection<Class<?>> aModels)
	{
		for(Class<?> model : aModels)
		{
			this.registerModel(model);
		}
	}

	@Override
	public void registerModels(Class<?>[] aModels)
	{
		for(Class<?> model : aModels)
		{
			this.registerModel(model);
		}
	}

	@Override
	public Class<?> unregisterModel(Class<?> aModel)
	{
		Class<?> model = this.modelsByFullName.remove(aModel.getName());
		this.modelsByFullName.remove(aModel.getSimpleName());
		return model;
	}

	@Override
	public Class<?> unregisterModel(String aModelClassName)
	{
		Class<?> model = this.modelsByFullName.remove(aModelClassName);
		if(model == null)
		{
			model = this.modelsBySimpleName.remove(aModelClassName);
			if(model == null)
			{
				return null;
			}
			this.modelsByFullName.remove(aModelClassName);
		}

		return model;
	}


}
