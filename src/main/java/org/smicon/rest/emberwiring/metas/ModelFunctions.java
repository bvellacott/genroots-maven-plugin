package org.smicon.rest.emberwiring.metas;

import static java.beans.Introspector.USE_ALL_BEANINFO;
import static java.beans.Introspector.getBeanInfo;
import static org.smicon.rest.emberwiring.general.GeneralFunctions.getAnnotatedPropertyNamesAndTypes;
import static org.smicon.rest.emberwiring.metas.ModelValidationFunctions.isAccessibleMember;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.smicon.rest.emberwiring.exceptions.InvalidFactoryConfigurationException;
import org.smicon.rest.emberwiring.general.factories.KeyedFactoryI;
import org.smicon.rest.emberwiring.types.id.IdType;
import org.smicon.rest.emberwiring.types.id.IdTypeEnum;

import papu.annotations.Model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;


public final class ModelFunctions
{
	
	public static Class<? extends IdType> deduceIdType(Class<?> aModelClass) throws Exception
	{
		if (aModelClass.getAnnotation(IdClass.class) != null) return IdTypeEnum.composite;

		boolean containsSimpleId = false;
		for(PropertyDescriptor pd : getBeanInfo(aModelClass, USE_ALL_BEANINFO).getPropertyDescriptors())
		{
			if (pd.getReadMethod().getAnnotation(EmbeddedId.class) != null) return IdTypeEnum.embedded;
			else if (pd.getReadMethod().getAnnotation(Id.class) != null) containsSimpleId = true;
		}
		for (Field field : aModelClass.getDeclaredFields())
		{
			if (field.getAnnotation(EmbeddedId.class) != null) return IdTypeEnum.embedded;
			else if (field.getAnnotation(Id.class) != null) containsSimpleId = true;
		}

		if (containsSimpleId) return IdTypeEnum.simple;

		return IdTypeEnum.undefinded;
	}
	
	public static Map<String, Field> getInaccessibleFieldsAndNames(Class<?> aEntityClass)
	{
		HashMap<String, Field> map = new HashMap();
		for(Field f : aEntityClass.getDeclaredFields())
		{
			if(!isAccessibleMember(f)) map.put(f.getName(), f);
		}
		
		return map;
	}

	public static Map<String, Field> getAccessibleFieldsAndNames(Class<?> aEntityClass)
	{
		HashMap<String, Field> map = new HashMap();
		for(Field f : aEntityClass.getFields())
		{
			map.put(f.getName(), f);
		}
		return map;
	}
	
	public static void orderFieldsByAnnotation(Collection<? extends Field> aFrom, Map<Class<? extends Annotation>, Set<String>> aTo)
	{
		for(Field f : aFrom) 
		{
			Annotation[] as = f.getAnnotations();
			if(as.length > 0)
			{
				for(Annotation a : as)
				{
					Set<String> fieldNames = aTo.get(a.annotationType());
					if(fieldNames == null)
					{
						fieldNames = new HashSet();
						aTo.put(a.annotationType(), fieldNames);
					}
					fieldNames.add(f.getName());
				}
			}
			else
			{
				Set<String> fieldNames = aTo.get(null);
				if(fieldNames == null)
				{
					fieldNames = new HashSet();
					aTo.put(null, fieldNames);
				}
				fieldNames.add(f.getName());
			}
		}
	}
	
	public static SimplePropertyMetaI getSimplePropertyMeta(final PropertyDescriptor aDescriptor, final boolean aIsGenerated)
	{
		SimplePropertyMetaI meta = new SimplePropertyMetaI() {

			@Override public PropertyDescriptor getPropertyDescriptor() { return aDescriptor; }

			@Override public boolean isGenerated() { return aIsGenerated; }

		};
		
		return meta;
	}
	
	public static ModelPropertyMetaI getModelPropertyMeta(final PropertyDescriptor aDescriptor, final ModelMetaI aMeta) {
		return new ModelPropertyMetaI() {

			@Override public ModelMetaI getTargetMeta() { return aMeta; }

			@Override public PropertyDescriptor getPropertyDescriptor() { return aDescriptor; }
		};
	}
	
	public static PropertyDescriptor getPropertyDescriptor(Class<?> aClass, String aPropertyName) throws Exception
	{
		for(PropertyDescriptor pd : getBeanInfo(aClass, USE_ALL_BEANINFO).getPropertyDescriptors())
			if(pd.getName() == aPropertyName) return pd;
		return null;
	}

	
	/**
	 * Creates a meta data object of a model class with a simple id. This method performs no validation. If an invalid class is given as a parameter 
	 * the outcome is unpredictable.
	 * 
	 * @param aModel
	 * @param aCollectionConfiguration
	 * @return
	 * @throws Exception
	 */
	public static void collectModelMeta(final Class<?> aModel, ModelMetaInstance aMetaInstance, KeyedFactoryI<Class<?>, ModelMetaI, ModelValidationDataI> aModelMetaFactory) throws Exception
	{
		if(!aModelMetaFactory.isCacheing() || !aModelMetaFactory.isInitialisingAfterCacheing()) throw new InvalidFactoryConfigurationException("The Model meta factory needs to initialise the meta object after cacheing it!");
		
		Map<String, Class<?>> idProperties = getAnnotatedPropertyNamesAndTypes(aModel, Id.class);
		Map<String, Class<?>> embeddedIdProperties = getAnnotatedPropertyNamesAndTypes(aModel, EmbeddedId.class);
		Map<String, Class<?>> simpleCollectionProperties = getAnnotatedPropertyNamesAndTypes(aModel, ElementCollection.class);
		Map<String, Class<?>> generatedProperties = getAnnotatedPropertyNamesAndTypes(aModel, GeneratedValue.class);
		Map<String, Class<?>> modelCollectionProperties = getAnnotatedPropertyNamesAndTypes(aModel, OneToMany.class);
		modelCollectionProperties.putAll(getAnnotatedPropertyNamesAndTypes(aModel, ManyToMany.class));
		
		Builder idPropertiesBuilder = ImmutableMap.builder();
		Builder embeddedIdPropertiesBuilder = ImmutableMap.builder();
		Builder simplePropertiesBuilder = ImmutableMap.builder();
		Builder simpleCollectionPropertiesBuilder = ImmutableMap.builder();
		Builder modelPropertiesWithSimpleIdsBuilder = ImmutableMap.builder();
		Builder modelPropertiesWithEmbeddedIdsBuilder = ImmutableMap.builder();
		Builder modelPropertiesWithCompositeIdsBuilder = ImmutableMap.builder();
		Builder modelCollectionPropertiesBuilder = ImmutableMap.builder();
		
		for(PropertyDescriptor desc : getBeanInfo(aModel, USE_ALL_BEANINFO).getPropertyDescriptors())
		{
			if(aModelMetaFactory.getConfiguration().getSingularParameterTypes().contains(desc.getPropertyType()))
				simplePropertiesBuilder.put(desc.getName(), getSimplePropertyMeta(desc, generatedProperties.containsKey(desc.getName())));
			if(idProperties.containsKey(desc.getName()))
				idPropertiesBuilder.put(desc.getName(), getSimplePropertyMeta(desc, generatedProperties.containsKey(desc.getName())));
			else if(embeddedIdProperties.containsKey(desc.getName()))
				embeddedIdPropertiesBuilder.put(desc.getName(), getSimplePropertyMeta(desc, generatedProperties.containsKey(desc.getName())));
			else if(aModelMetaFactory.getConfiguration().getCollectionParameterTypes().contains(desc.getPropertyType()) )
			{
				if(simpleCollectionProperties.containsKey(desc.getName()))
					simpleCollectionPropertiesBuilder.put(desc.getName(), getSimplePropertyMeta(desc, false));
				else if(modelCollectionProperties.containsKey(desc.getName())) {
					OneToMany om = desc.getReadMethod().getAnnotation(OneToMany.class);
					ManyToMany mm = desc.getReadMethod().getAnnotation(ManyToMany.class);
					Class targetModel = null;
					if(om != null) targetModel = om.targetEntity();
					if(targetModel == null && mm != null) targetModel = mm.targetEntity();
					if(targetModel == null) {
						Field f = aModel.getDeclaredField(desc.getName());
						om = f.getAnnotation(OneToMany.class);
						mm = f.getAnnotation(ManyToMany.class);
					}
					targetModel = (om != null) ? om.targetEntity() : mm.targetEntity();
					
					ModelMetaI targetMeta = aModelMetaFactory.create(targetModel);
					modelCollectionPropertiesBuilder.put(desc.getName(), getModelPropertyMeta(desc, targetMeta));
				}
			}
			else if(desc.getPropertyType().getAnnotation(Model.class) != null)
			{
				ModelMetaI targetMeta = aModelMetaFactory.create(desc.getPropertyType());
				ModelPropertyMetaI propertyMeta = getModelPropertyMeta(desc, targetMeta);
				if(deduceIdType(desc.getPropertyType()) == IdTypeEnum.simple)
					modelPropertiesWithSimpleIdsBuilder.put(desc.getName(), propertyMeta);
				if(deduceIdType(desc.getPropertyType()) == IdTypeEnum.embedded)
					modelPropertiesWithEmbeddedIdsBuilder.put(desc.getName(), propertyMeta);
				if(deduceIdType(desc.getPropertyType()) == IdTypeEnum.composite)
					modelPropertiesWithCompositeIdsBuilder.put(desc.getName(), propertyMeta);
			}
		}
		
		aMetaInstance.idType = deduceIdType(aModel);
		if(aMetaInstance.idType == IdTypeEnum.simple)
		{
			aMetaInstance.idName = idProperties.keySet().iterator().next();
			aMetaInstance.idClass = idProperties.get(aMetaInstance.idName);
		}
		else if(aMetaInstance.idType == IdTypeEnum.embedded)
		{
			aMetaInstance.idName = embeddedIdProperties.keySet().iterator().next();
			aMetaInstance.idClass = embeddedIdProperties.get(aMetaInstance.idName);
		}
		else if(aMetaInstance.idType == IdTypeEnum.composite)
		{
			aMetaInstance.idName = createUniqueIdName(idProperties.keySet());
			IdClass idAnnot = (IdClass)aModel.getAnnotation(IdClass.class);
			aMetaInstance.idClass = idAnnot.value();
		}

		aMetaInstance.model = aModel;
		aMetaInstance.modelAnnot = aModel.getAnnotation(Model.class);
		aMetaInstance.idBi = getBeanInfo(aMetaInstance.idClass, USE_ALL_BEANINFO);
		aMetaInstance.idPathParameterMetas = idPropertiesBuilder.build();
		aMetaInstance.embeddedIdPathParameterMetas = embeddedIdPropertiesBuilder.build();
		aMetaInstance.simpleProperties = simplePropertiesBuilder.build();
		aMetaInstance.simpleCollectionProperties = simpleCollectionPropertiesBuilder.build();
		aMetaInstance.modelPropertiesWithSimpleId = modelPropertiesWithSimpleIdsBuilder.build();
		aMetaInstance.modelPropertiesWithEmbeddedIds = modelPropertiesWithEmbeddedIdsBuilder.build();
		aMetaInstance.modelPropertiesWithCompositeIds = modelPropertiesWithCompositeIdsBuilder.build();
		aMetaInstance.modelCollectionProperties = modelCollectionPropertiesBuilder.build();
	}
	
	public static String createUniqueIdName(Collection<String> aExistingIdNames)
	{
		String nameCandidate = "id";
		
		boolean uniqueIdFound;
		
		do
		{
			uniqueIdFound = true;
			for(String existingName : aExistingIdNames)
			{
				if(existingName.equalsIgnoreCase(nameCandidate))
				{
					nameCandidate = "" + nameCandidate + "_";
					uniqueIdFound = false;
					break;
				}
			}
		}while(!uniqueIdFound);
		
		return nameCandidate;
	}
	
	public static ModelMetaRegistryI populateRegistry(KeyedFactoryI<Class<?>, ModelMetaI, ModelValidationDataI> aModelMetaFactory, ModelMetaCollectingConfigurationI aConfiguration) throws Exception
	{
		ModelMetaRegistryInstance registry = new ModelMetaRegistryInstance();
		
		Builder modelsBySimpleName = ImmutableMap.builder();
		Builder modelsByFullName = ImmutableMap.builder();
		Builder modelMetas = ImmutableMap.builder();
		
		Set<Class<?>> models = aConfiguration.getReflections().getTypesAnnotatedWith(Model.class);

		for(Class<?> model : models)
		{
			ModelMetaI meta = aModelMetaFactory.create(model);
			modelMetas.put(model, meta);
			modelsByFullName.put(model.getName(), model);
			modelsBySimpleName.put(model.getSimpleName(), model);
		}
		
		registry.modelsBySimpleName = modelsBySimpleName.build();
		registry.modelsByFullName = modelsByFullName.build();
		registry.modelMetas = modelMetas.build();
		
		return registry;
	}

	public static class ModelMetaInstance 
	implements ModelMetaI
	{
		Class<?> model;
		Model modelAnnot;
		String idName;
		Class<?> idClass;
		BeanInfo idBi;
		Class<? extends IdType> idType;
		Map idPathParameterMetas;
		Map embeddedIdPathParameterMetas;
		Map simpleProperties;
		Map simpleCollectionProperties;
		Map modelPropertiesWithSimpleId;
		Map modelPropertiesWithEmbeddedIds;
		Map modelPropertiesWithCompositeIds;
		Map modelCollectionProperties;

		@Override public Class<?> getModelClass() { return model; }

		@Override public BeanInfo getModelBeanInfo() throws IntrospectionException { return Introspector.getBeanInfo(getModelClass(), Introspector.USE_ALL_BEANINFO); }

		@Override public Model getModelAnnotation() { return modelAnnot; }
		
		@Override public Class<?> getIdClass() { return idClass; }

		@Override public BeanInfo getIdBeanInfo() { return idBi; }

		@Override public Class<? extends IdType> getIdType() { return idType; }

		@Override public String getSingularIdName() { return idName; }

		@Override public Map<String, SimplePropertyMetaI> getIdPathParameterMetas() { return idPathParameterMetas; }

		@Override public Map<String, SimplePropertyMetaI> getEmbeddedIdPathParameterMetas() { return embeddedIdPathParameterMetas; }

		@Override public Map<String, SimplePropertyMetaI> getSimpleProperties() { return simpleProperties; }

		@Override public Map<String, SimplePropertyMetaI> getSimpleCollectionProperties() { return simpleCollectionProperties; }

		@Override public Map<String, ModelPropertyMetaI> getModelPropertiesWithSimpleIds() { return modelPropertiesWithSimpleId; }

		@Override public Map<String, ModelPropertyMetaI> getModelPropertiesWithEmbeddedIds() { return modelPropertiesWithEmbeddedIds; }

		@Override public Map<String, ModelPropertyMetaI> getModelPropertiesWithCompositeIds() { return modelPropertiesWithCompositeIds; }

		@Override public Map<String, ModelPropertyMetaI> getModelCollectionProperties() { return modelCollectionProperties; }
	}

	static class ModelMetaRegistryInstance
	implements
	ModelMetaRegistryI
	{
		Map<String, Class<?>> modelsBySimpleName;
		Map<String, Class<?>> modelsByFullName;
		Map<Class<?>, ModelMetaI> modelMetas;
		
		@Override
		public Class<?> getModel(String aEntityClassName)
		{
			Class<?> model = this.modelsByFullName.get(aEntityClassName);
			if(model == null)
				model = this.modelsBySimpleName.get(aEntityClassName);
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
		public ModelMetaI getModelMeta(String aEntityClassName)
		{
			return modelMetas.get(this.getModel(aEntityClassName));
		}

		@Override
		public ModelMetaI getModelMeta(Class<?> aModel) throws Exception
		{
			return modelMetas.get(aModel);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Set<Class<?>> getModels()
		{
			return modelMetas.keySet();
		}
	}

}
