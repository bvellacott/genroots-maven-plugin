package org.smicon.rest.emberwiring.metas;

import static java.beans.Introspector.USE_ALL_BEANINFO;
import static java.beans.Introspector.getBeanInfo;
import static org.smicon.rest.emberwiring.general.GeneralFunctions.getAnnotatedPropertyNamesAndTypes;
import static org.smicon.rest.emberwiring.general.GeneralFunctions.getPropertyNamesAndTypes;
import static org.smicon.rest.emberwiring.metas.ModelFunctions.deduceIdType;
import static org.smicon.rest.emberwiring.metas.ModelFunctions.getAccessibleFieldsAndNames;
import static org.smicon.rest.emberwiring.metas.ModelFunctions.getInaccessibleFieldsAndNames;
import static org.smicon.rest.emberwiring.metas.ModelFunctions.orderFieldsByAnnotation;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.smicon.rest.emberwiring.exceptions.IncorrectEntityStructureException;
import org.smicon.rest.emberwiring.exceptions.IncorrectIdStructureException;
import org.smicon.rest.emberwiring.exceptions.IncorrectModelStructureException;
import org.smicon.rest.emberwiring.types.id.IdType;
import org.smicon.rest.emberwiring.types.id.IdTypeEnum;

import papu.annotations.Model;

import com.google.common.primitives.Primitives;

public final class ModelValidationFunctions
{
	public boolean isIdField(Field aField)
	{
		return aField.getAnnotation(Id.class) != null ||
			aField.getAnnotation(EmbeddedId.class) == null;
	}

	public boolean isIdAccessor(Method aMethod)
	{
		return aMethod.getAnnotation(Id.class) != null ||
			aMethod.getAnnotation(EmbeddedId.class) == null;
	}

	public boolean isIdProperty(PropertyDescriptor aDescriptor)
	{
		return isIdAccessor(aDescriptor.getReadMethod());
	}

	public static boolean isValidSimpleIdType(Class<?> aType, ModelValidationDataI aData)
	{
		return aType.isPrimitive() || Primitives.allWrapperTypes().contains(aType) || aData.getNonPrimitiveIdTypes().contains(aType);
	}

	public static boolean isValidSimpleIdType(Class<?> aType)
	{
		return isValidSimpleIdType(aType, ModelValidationDataBuilder.default_instance);
	}

	public static boolean isAccessibleMember(Member aMember)
	{
		return (aMember.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE | Modifier.FINAL | Modifier.STATIC)) == Modifier.PUBLIC;
	}

	public static void validatePropertyDescriptor(PropertyDescriptor aPropertyDescriptor, String aPropertyName, Class<?> aPropertyType) throws Exception
	{
		if (aPropertyDescriptor.getName().equals("class")) return;

		if (!aPropertyDescriptor.getName().equals(aPropertyName))
			throw new IncorrectIdStructureException("The property name and property descriptor name mismatch!");

		if (Primitives.unwrap(aPropertyDescriptor.getPropertyType()) != Primitives.unwrap(aPropertyType))
			throw new IncorrectIdStructureException("The type of the property: " + aPropertyName
				+ "  doesn't match with a similarily named accessor method in the same class");

		if (aPropertyDescriptor.getReadMethod() == null)
			throw new IncorrectIdStructureException("The property: " + aPropertyName + " is missing a reader (getter) method");

		if (!isAccessibleMember(aPropertyDescriptor.getReadMethod()))
			throw new IncorrectIdStructureException("The property's: " + aPropertyName + " reader (getter) method isn't accessible");

//		if (aPropertyDescriptor.getWriteMethod() == null)
//			throw new IncorrectIdStructureException("The property: " + aPropertyName + " is missing a writer (setter) method");
//
//		if (!isAccessibleMember(aPropertyDescriptor.getWriteMethod()))
//			throw new IncorrectIdStructureException("The property's: " + aPropertyName + " writer (setter) method isn't accessible");
	}
	
	public static boolean isValidPropertyDescriptor(PropertyDescriptor aPropertyDescriptor, String aPropertyName, Class<?> aPropertyType) throws Exception
	{
		try {
			validatePropertyDescriptor(aPropertyDescriptor, aPropertyName, aPropertyType);
		} catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public static void validateFullPropertyDescriptor(PropertyDescriptor aPropertyDescriptor, String aPropertyName, Class<?> aPropertyType) throws Exception
	{
		validatePropertyDescriptor(aPropertyDescriptor, aPropertyName, aPropertyType);

		if (aPropertyDescriptor.getWriteMethod() == null)
			throw new IncorrectModelStructureException("The property: " + aPropertyName + " is missing a writer (setter) method");

		if (!isAccessibleMember(aPropertyDescriptor.getWriteMethod()))
			throw new IncorrectModelStructureException("The property's: " + aPropertyName + " writer (setter) method isn't accessible");
	}
	
	public static void validatePropertyDescriptors(Class<?> aModel, Map<String, Class<?>> aPropertyNamesAndTypes) throws Exception
	{
		BeanInfo bi = getBeanInfo(aModel, USE_ALL_BEANINFO);
		
		HashMap<String, Class<?>> propNamesAndTypes = new HashMap(aPropertyNamesAndTypes);
		
		for(PropertyDescriptor pd : bi.getPropertyDescriptors())
		{
			if(propNamesAndTypes.keySet().contains(pd.getName())) 
				validatePropertyDescriptor(pd, pd.getName(), propNamesAndTypes.remove(pd.getName()));
		}
		if(propNamesAndTypes.size() > 0)
		{
			throw new IncorrectModelStructureException("The following properties have no reader (getter) methods: " + propNamesAndTypes);
		}
	}
	
	public static void validateFullPropertyDescriptors(Class<?> aModel, Map<String, Class<?>> aPropertyNamesAndTypes) throws Exception
	{
		BeanInfo bi = getBeanInfo(aModel, USE_ALL_BEANINFO);
		
		HashMap<String, Class<?>> propNamesAndTypes = new HashMap(aPropertyNamesAndTypes);
		
		for(PropertyDescriptor pd : bi.getPropertyDescriptors())
		{
			if(propNamesAndTypes.keySet().contains(pd.getName())) 
				validateFullPropertyDescriptor(pd, pd.getName(), propNamesAndTypes.remove(pd.getName()));
		}
		if(propNamesAndTypes.size() > 0)
		{
			throw new IncorrectModelStructureException("The following properties have no reader (getter) and/or writer (setter) methods: " + propNamesAndTypes);
		}
	}
	
	public static boolean isValidFullPropertyDescriptor(PropertyDescriptor aPropertyDescriptor, String aPropertyName, Class<?> aPropertyType) throws Exception
	{
		try {
			validateFullPropertyDescriptor(aPropertyDescriptor, aPropertyName, aPropertyType);
		} catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public static boolean isCompletePropertyDescriptor(PropertyDescriptor aPropertyDescriptor)
	{
		if (aPropertyDescriptor.getReadMethod() == null) return false;
		if (!isAccessibleMember(aPropertyDescriptor.getReadMethod())) return false;

		if (aPropertyDescriptor.getWriteMethod() == null) return false;
		if (!isAccessibleMember(aPropertyDescriptor.getWriteMethod())) return false;

		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean isAnnotatedAs(Class<?> aClass, @SuppressWarnings("rawtypes") Class aAnnotation)
	{
		if(!aAnnotation.isAnnotation())
			throw new IllegalArgumentException("The parameter aAnnotation must be a valid annotation!");
		return aClass.getAnnotation(aAnnotation) != null;
	}

	public static void validateModel(Class<?> aModelClass, ModelValidationDataI aData) throws Exception
	{
		if (!isAnnotatedAs(aModelClass, Entity.class))
			throw new IllegalArgumentException("The given type: " + aModelClass.getSimpleName() + " doesn't represent a valid Model. It is not annotated with the " + Entity.class + " annotation!");
		if (!isAnnotatedAs(aModelClass, Model.class))
			throw new IllegalArgumentException("The given type: " + aModelClass.getSimpleName() + " doesn't represent a valid Model. It is not annotated with the " + Model.class + " annotation!");
		Class<? extends IdType> idType = deduceIdType(aModelClass);
		if(idType == IdTypeEnum.simple) 
			validateSimpleId(aModelClass, aData);
		else if(idType == IdTypeEnum.composite) 
			validateCompositeId(aModelClass, aData);
		else if(idType == IdTypeEnum.embedded) 
			validateEmbeddedId(aModelClass, aData);
		else 
			throw new IncorrectIdStructureException("The type " + aModelClass.getSimpleName() + " either doesn't contain an id or the id type couldn't be identified!");
		validateNonIdPropertyTypes(aModelClass, aData);
	}

	public static void validateCompositeId(Class<?> aModelClass, ModelValidationDataI aData) throws Exception
	{
		IdClass idClsAnnotation = aModelClass.getAnnotation(IdClass.class);

		if (idClsAnnotation == null)
			throw new IncorrectIdStructureException("The @" + IdClass.class.getSimpleName()
				+ " annotation is missing from the composite id setup for the class: " + aModelClass.getName() + "!");

		Class<?> idClass = idClsAnnotation.value();

		Map<String, Class<?>> idClassProperties = getPropertyNamesAndTypes(idClass);
		Map<String, Class<?>> modelIdProperties = getAnnotatedPropertyNamesAndTypes(aModelClass, Id.class);
		Map<String, Class<?>> modelEmbeddedIdProperties = getAnnotatedPropertyNamesAndTypes(aModelClass, EmbeddedId.class);

		if(modelEmbeddedIdProperties.size() > 0)
			throw new IncorrectIdStructureException("A class annotated with the @" + IdClass.class.getSimpleName() 
				+ " annotation must not contain an embedded id!");

		if(modelIdProperties.size() == 0)
			throw new IncorrectIdStructureException("A class annotated with the @" + IdClass.class.getSimpleName() 
				+ " annotation must contain at least one id property!");
		
		validateFullPropertyDescriptors(aModelClass, modelIdProperties);
		
		for(String propertyName : idClassProperties.keySet())
		{
			Class<?> idClassPropertyType = idClassProperties.get(propertyName);
			Class<?> modelClassPropertyType = modelIdProperties.get(propertyName);
			
			if(modelClassPropertyType == null)
				throw new IncorrectIdStructureException("The model doesn't contain a property by the name '" + propertyName + "'!");
			
			if(modelClassPropertyType != idClassPropertyType)
				throw new IncorrectIdStructureException("The types missmatch between the model and the IdClass on the property: '" + propertyName + "'!");
		}
		
		for(String propertyName : modelIdProperties.keySet())
		{
			Class<?> idClassPropertyType = idClassProperties.get(propertyName);
			Class<?> modelClassPropertyType = modelIdProperties.get(propertyName);
			
			if(idClassPropertyType == null)
				throw new IncorrectIdStructureException("The IdClass doesn't contain a property by the name '" + propertyName + "'!");
			
			if(modelClassPropertyType != idClassPropertyType)
				throw new IncorrectIdStructureException("The types missmatch between the model and the IdClass on the property: '" + propertyName + "'!");
			
			if(!isValidSimpleIdType(modelClassPropertyType, aData))
				 throw new IncorrectIdStructureException("The property: " + propertyName + " in the model " + aModelClass.getSimpleName() + " isn't a valid simple id type!");
		}
	}
	
	public static void validateEmbeddedId(Class<?> aModelClass, ModelValidationDataI aData) throws Exception
	{
		IdClass idClsAnnotation = aModelClass.getAnnotation(IdClass.class);

		if (idClsAnnotation != null)
			throw new IncorrectIdStructureException("A class with an embedded id must not be annotated with the @" + IdClass.class.getSimpleName());

		Map<String, Class<?>> modelIdProperties = getAnnotatedPropertyNamesAndTypes(aModelClass, Id.class);
		Map<String, Class<?>> modelEmbeddedIdProperties = getAnnotatedPropertyNamesAndTypes(aModelClass, EmbeddedId.class);

		if(modelEmbeddedIdProperties.size() != 1)
			throw new IncorrectIdStructureException("A model with an embedded id must have one and only one property annotated: @" + EmbeddedId.class);

		if(modelIdProperties.size() > 0)
			throw new IncorrectIdStructureException("A model with an embedded id must have no properties annotated: @" + Id.class);

		validateFullPropertyDescriptors(aModelClass, modelEmbeddedIdProperties);
		
		for(String propertyName : modelEmbeddedIdProperties.keySet())
		{
			Map<String, Class<?>> embeddedIdClassProperties = getPropertyNamesAndTypes(modelEmbeddedIdProperties.get(propertyName));
			
			Class<?> embeddedIdPropertyType = modelEmbeddedIdProperties.get(propertyName);
			if(!isAnnotatedAs(embeddedIdPropertyType, Embeddable.class))
				throw new IncorrectIdStructureException("The property: " + propertyName + " annotated: @" + EmbeddedId.class.getSimpleName() + " must point to a type annotated @" + Embeddable.class.getSimpleName() + "!");
			
			for(String embeddedIdPropertyName : embeddedIdClassProperties.keySet())
			{
				Class<?> idPropertyType = embeddedIdClassProperties.get(embeddedIdPropertyName);
			
				if(!isValidSimpleIdType(idPropertyType, aData))
					 throw new IncorrectIdStructureException("The property: " + propertyName + " in the model " + aModelClass.getSimpleName() + " contains a property: " + embeddedIdPropertyName + " which isn't a valid simple id type!");
			}
		}
	}
	
	public static void validateSimpleId(Class<?> aModelClass, ModelValidationDataI aData) throws Exception
	{
		IdClass idClsAnnotation = aModelClass.getAnnotation(IdClass.class);

		if (idClsAnnotation != null)
			throw new IncorrectIdStructureException("A class with a simple id membeddedust not be annotated with the @" + IdClass.class.getSimpleName());

		Map<String, Class<?>> modelIdProperties = getAnnotatedPropertyNamesAndTypes(aModelClass, Id.class);
		Map<String, Class<?>> modelEmbeddedIdProperties = getAnnotatedPropertyNamesAndTypes(aModelClass, EmbeddedId.class);

		if(modelEmbeddedIdProperties.size() > 0)
			throw new IncorrectIdStructureException("A model with a simple id must not have a property annotated: @" + EmbeddedId.class);

		if(modelIdProperties.size() != 1)
			throw new IncorrectIdStructureException("A model with a simple id must have one and only one property annotated: @" + Id.class);

		for(String propertyName : modelIdProperties.keySet())
		{
			Class<?> idPropertyType = modelIdProperties.get(propertyName);
			if(!isValidSimpleIdType(idPropertyType, aData))
				 throw new IncorrectIdStructureException("The property: " + propertyName + " in the model " + aModelClass.getSimpleName() + " isn't a valid simple id type!");
		}
	}
	
	public static void validateNonIdPropertyTypes(Class<?> aEntityClass, ModelValidationDataI aData)
	throws Exception
	{
		BeanInfo bi = getBeanInfo(aEntityClass, USE_ALL_BEANINFO);
	
		Map<Class<? extends Annotation>, Set<String>> annotatedFieldNames = new HashMap();
	
		Map<String, Field> accessibleFields = getAccessibleFieldsAndNames(aEntityClass);
		Map<String, Field> inaccessibleFields = getInaccessibleFieldsAndNames(aEntityClass);
	
		orderFieldsByAnnotation(accessibleFields.values(), annotatedFieldNames);
		orderFieldsByAnnotation(inaccessibleFields.values(), annotatedFieldNames);
	
		Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap();
		for (PropertyDescriptor pd : bi.getPropertyDescriptors())
		{
			if(pd.getName().equals("class")) continue;
			
			Class<?> type = pd.getPropertyType();
			if(type.getAnnotation(IdClass.class) != null) continue;
			if(type.getAnnotation(Model.class) != null) continue;
			
			Method getter = pd.getReadMethod();
			if(getter == null) continue; 
			if(getter.getAnnotation(Id.class) != null) continue;
			if(getter.getAnnotation(EmbeddedId.class) != null) continue;
			Set idFields = annotatedFieldNames.get(Id.class);
			if(idFields != null && idFields.contains(pd.getName())) continue;
			Set embeddedIdFields = annotatedFieldNames.get(EmbeddedId.class);
			if(embeddedIdFields != null && embeddedIdFields.contains(pd.getName())) continue;
	
			for (Annotation a : getter.getAnnotations())
				annotationMap.put(a.annotationType(), a);
			
			if(annotationMap.containsKey(Id.class)) continue;
			if(annotationMap.containsKey(EmbeddedId.class)) continue;
	
			Field matchingField = accessibleFields.remove(pd.getName());
			if (matchingField == null) matchingField = inaccessibleFields.remove(pd.getName());
	
			if (matchingField != null)
			{
				if (!type.isAssignableFrom(matchingField.getType()))
					throw new IncorrectEntityStructureException("The class: " + aEntityClass.getSimpleName()
						+ " contains a reader (getter) method and a value property by the same name: " + pd.getName() + ", but mismatching types!");
				for (Annotation a : matchingField.getAnnotations())
					annotationMap.put(a.annotationType(), a);
			}
			else if(annotationMap.keySet().contains(Embedded.class))
			{
				try {
					validateEmbeddableType(type, aData);
				} catch(Exception e) {
					throw new IncorrectIdStructureException("The property: " + pd.getName() + " in the model " + aEntityClass.getSimpleName() + " isn't a valid embeddable type!");
				}
			}
			else if(aData.getCollectionParameterTypes().contains(type))
			{
				try {
					validateCollectionParameterType(type, annotationMap, aData);
				}catch(Exception e) {
					throw new IncorrectModelStructureException("The collection parameter: " + pd.getName() + " is invalid!", e);
				}
			}
			else if(!aData.getSingularParameterTypes().contains(type))
				throw new IncorrectModelStructureException("The property: " + pd.getName() + " in the model " + aEntityClass.getSimpleName() + " isn't an accepted singular type for a model! The property needs to be another model or of type: " + aData.getSingularParameterTypes());
			annotationMap.clear();
		}
	}
	
	private static void validateCollectionParameterType(Class<?> aType, Map<Class<? extends Annotation>, Annotation> aAnnotationMap, ModelValidationDataI aData) throws Exception
	{
		if(aAnnotationMap.keySet().contains(ElementCollection.class))
		{
			ElementCollection a = (ElementCollection) aAnnotationMap.get(ElementCollection.class);
			validateSimpleCollectionTargetType(a.targetClass(), aData);
		}
		
		Class<?> aTargetModel = null;
		if(aAnnotationMap.keySet().contains(OneToMany.class))
		{
			OneToMany a = (OneToMany) aAnnotationMap.get(OneToMany.class);
			aTargetModel = a.targetEntity();
		}
		if(aAnnotationMap.keySet().contains(ManyToMany.class))
		{
			ManyToMany a = (ManyToMany) aAnnotationMap.get(ManyToMany.class);
			aTargetModel = a.targetEntity();
		}
		if(aTargetModel == null)
			throw new IncorrectModelStructureException("The many relationship specifies no target model type! Please specify one using the targetEntity property of the annotation!");
	}
	
	public static void validateSimpleCollectionTargetType(Class<?> aTargetType, ModelValidationDataI aData) throws Exception
	{
		if(isAnnotatedAs(aTargetType, Embeddable.class))
		{
			try {
				validateEmbeddableType(aTargetType, aData);
			} catch(Exception e) {
				throw new IncorrectModelStructureException("The target type: " + aTargetType.getSimpleName() + " of the collection property isn't a valid embeddable type!");
			}
		}
		if(!aData.getSingularParameterTypes().contains(aTargetType))
		{
			throw new IncorrectModelStructureException("The target type: " + aTargetType.getSimpleName() + " of the collection property is neither a valid basic type or an embeddable type! The valid basic types are: " + aData.getSingularParameterTypes());
		}
	}

	public static void validateModelCollectionTargetType(Class<?> aTargetType, ModelValidationDataI aData) throws Exception
	{
		if(!isAnnotatedAs(aTargetType, Model.class))
			throw new IncorrectModelStructureException("The target type: " + aTargetType.getSimpleName() + " of the model collection property isn't annotated @" + Model.class.getSimpleName() + "!");
	}

	public static void validateEmbeddableType(Class<?> aType, ModelValidationDataI aData) throws Exception
	{
		if (!isAnnotatedAs(aType, Embeddable.class))
			throw new IllegalArgumentException("The embeddable type: " + aType.getSimpleName() + " isn't annotated with the: @" + Embeddable.class.getSimpleName() + " annotation!");
		if (isAnnotatedAs(aType, Entity.class))
			throw new IllegalArgumentException("The embeddable type: " + aType.getSimpleName() + " is annotated with the: @" + Entity.class.getSimpleName() + " annotation. An embeddable type must not be an entity itself!");
		Class<? extends IdType> idType = deduceIdType(aType);
		if(idType == IdTypeEnum.simple) 
			throw new IncorrectIdStructureException("The embeddable type " + aType.getSimpleName() + " has a simple id structure. An embeddable type must specify no id structure!");
		else if(idType == IdTypeEnum.composite) 
			throw new IncorrectIdStructureException("The embeddable type " + aType.getSimpleName() + " has a composite id structure. An embeddable type must specify no id structure!");
		else if(idType == IdTypeEnum.embedded) 
			throw new IncorrectIdStructureException("The embeddable type " + aType.getSimpleName() + " has an embedded id structure. An embeddable type must specify no id structure!");
		
		BeanInfo bi = getBeanInfo(aType, USE_ALL_BEANINFO);

		Map<String, Field> accessibleFields = getAccessibleFieldsAndNames(aType);
		Map<String, Field> inaccessibleFields = getInaccessibleFieldsAndNames(aType);
	
		Set<Annotation> annotationSet = new HashSet();
		for (PropertyDescriptor pd : bi.getPropertyDescriptors())
		{
			Class<?> propertyType = pd.getPropertyType();
			Method getter = pd.getReadMethod();
			if (getter == null) continue;
	
			for (Annotation a : getter.getAnnotations())
				annotationSet.add(a);
	
			Field matchingField = accessibleFields.remove(pd.getName());
			if (matchingField == null) matchingField = inaccessibleFields.remove(pd.getName());
	
			if (matchingField != null)
			{
				if (!propertyType.isAssignableFrom(matchingField.getType()))
					throw new IncorrectEntityStructureException("The embeddable type: " + propertyType.getSimpleName()
						+ " contains a reader (getter) method and a value property by the same name: " + pd.getName() + ", but mismatching types!");
				for (Annotation a : matchingField.getAnnotations())
					annotationSet.add(a);
			}
	
			if(annotationSet.contains(Embedded.class))
				 throw new IncorrectModelStructureException("The property: " + pd.getName() + " is an embeddable type in an embeddable type " + aType.getSimpleName() + "! This isn't supported.");
			if(propertyType.isAssignableFrom(Collection.class))
				throw new IncorrectModelStructureException("The property: " + pd.getName() + " is a collection in an embeddable type " + aType.getSimpleName() + "! This isn't supported.");
			if(propertyType.isAssignableFrom(Map.class))
				throw new IncorrectModelStructureException("The property: " + pd.getName() + " is a map in an embeddable type " + aType.getSimpleName() + "! This isn't supported.");
			if(!aData.getSingularParameterTypes().contains(propertyType))
				throw new IncorrectModelStructureException("The property: " + pd.getName() + " in the embeddable type " + aType.getSimpleName() + " isn't an accepted singular type for a model! The valid types are: " + aData.getSingularParameterTypes());
		}
	}
}
