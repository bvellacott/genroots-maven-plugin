package org.smicon.rest.metas;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import org.reflections.Reflections;
import org.smicon.rest.exceptions.IncorrectIdStructureException;

import papu.annotations.Model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.Primitives;

public final class Metas
{

	@SuppressWarnings("rawtypes")
	public static final Set<Class> nonPrimitiveIdTypes = Sets.newHashSet((Class) String.class, java.util.Date.class, java.sql.Date.class,
		java.math.BigDecimal.class, java.math.BigInteger.class);

	private static String packageName;
	private static Reflections reflections;

	private static HashMap<String, Class<?>> entitiesBySimpleName;
	private static HashMap<String, Class<?>> entitiesByFullName;

	private static HashMap<String, Class<?>> modelsBySimpleName;
	private static HashMap<String, Class<?>> modelsByFullName;

	private static ImmutableSet<?> javaSimpleTypesRepresentingEmberNumbers = ImmutableSet.builder()
		.add(int.class, Integer.class, double.class, Double.class, float.class, Float.class, long.class, Long.class, short.class, Short.class, java.math.BigInteger.class, java.math.BigDecimal.class).build();
	private static ImmutableSet<?> javaSimpleTypesRepresentingEmberBooleans = ImmutableSet.builder().add(boolean.class, Boolean.class).build();;
	private static ImmutableSet<?> javaSimpleTypesRepresentingEmberDates = ImmutableSet.builder().add(java.util.Date.class, java.sql.Date.class, java.sql.Timestamp.class, java.sql.Time.class, java.util.Calendar.class).build();;
	private static ImmutableSet<?> javaSimpleTypesRepresentingEmberStrings = ImmutableSet.builder().add(String.class, Enum.class, byte.class, Byte.class, char.class, Character.class, byte[].class, Byte[].class, char[].class, Character[].class).build();
	private static ImmutableSet<?> javaTypesRepresentingEmberArrays = ImmutableSet.builder().add(Collection.class).build();
	private static ImmutableSet<?> javaSimpleTypesRepresentingEmberObjects = ImmutableSet.builder().add(Map.class).build();

	// CACHE

	private static final HashMap<Class<?>, EntityMeta> entityMetaCache = new HashMap<Class<?>, EntityMeta>();

	public static void init(String aPackageName)
	{
		packageName = aPackageName;
		reflections = new Reflections(packageName);

		Set<Class<?>> types = reflections.getTypesAnnotatedWith(Entity.class);

		entitiesBySimpleName = new HashMap<String, Class<?>>();
		entitiesByFullName = new HashMap<String, Class<?>>();

		modelsBySimpleName = new HashMap<String, Class<?>>();
		modelsByFullName = new HashMap<String, Class<?>>();

		for (Class<?> type : types)
		{
			if (type.getAnnotation(Model.class) != null)
			{
				modelsByFullName.put(type.getName(), type);
				modelsBySimpleName.put(type.getSimpleName(), type);
			}
			else
			{
				entitiesByFullName.put(type.getName(), type);
				entitiesBySimpleName.put(type.getSimpleName(), type);
			}
		}
	}

	public static Class<?> getEntityByName(String aEntityClassName)
	{
		Class<?> entity = entitiesByFullName.get(aEntityClassName);

		if (entity == null)
		{
			entity = entitiesBySimpleName.get(aEntityClassName);
		}

		return entity;
	}

	public static Class<?> getModelByName(String aEntityClassName)
	{
		Class<?> entity = modelsByFullName.get(aEntityClassName);

		if (entity == null)
		{
			entity = modelsBySimpleName.get(aEntityClassName);
		}

		return entity;
	}

	public static Collection<Class<?>> getModelClasses()
	{
		return modelsBySimpleName.values();
	}

	public static Collection<Class<?>> getEntityClasses()
	{
		return entitiesBySimpleName.values();
	}

	public static Set<Class> getNonprimitiveidtypes()
	{
		return nonPrimitiveIdTypes;
	}

	public static String getPackageName()
	{
		return packageName;
	}

	public static Reflections getReflections()
	{
		return reflections;
	}

	public static EntityMeta getEntityMeta(Class<?> aClass) throws Exception
	{
		EntityMeta meta = entityMetaCache.get(aClass);
		if (meta != null)
		{
			return meta;
		}

		Entity entityAnt = aClass.getAnnotation(Entity.class);
		Model modelAnt = aClass.getAnnotation(Model.class);

		if (entityAnt != null)
		{
			if (modelAnt != null)
			{
				meta = new ModelEntityMeta();
			}
			else
			{
				meta = new EntityMeta();
			}
			entityMetaCache.put(aClass, meta);
			meta.init(aClass);

			return meta;
		}

		return null;
	}

	public static boolean isValidSimpleIdType(Class<?> aType)
	{
		return aType.isPrimitive() || Primitives.allWrapperTypes().contains(aType) || nonPrimitiveIdTypes.contains(aType);
	}

	public static boolean isAccessibleField(Field aField)
	{
		return (aField.getModifiers() & (Modifier.PRIVATE | Modifier.FINAL | Modifier.STATIC)) == 0;
	}

	public static boolean validatePropertyDescriptor(PropertyDescriptor aPropertyDescriptor, String aPropertyName, Class<?> aPropertyType) throws Exception
	{
		if (!aPropertyDescriptor.getName().equals(aPropertyName))
		{
			return false;
		}

		if (Primitives.unwrap(aPropertyDescriptor.getPropertyType()) != Primitives.unwrap(aPropertyType))
		{
			throw new IncorrectIdStructureException("The type of the property: " + aPropertyName
				+ "  doesn't match with a similarily named accessor method in the same class");
		}

		if (aPropertyDescriptor.getReadMethod() == null)
		{
			throw new IncorrectIdStructureException("The property: " + aPropertyName + " is missing a reader (getter) method");
		}

		if (aPropertyDescriptor.getWriteMethod() == null)
		{
			throw new IncorrectIdStructureException("The property: " + aPropertyName + " is missing a writer (setter) method");
		}

		return true;
	}

	public static boolean isCompletePropertyDescriptor(PropertyDescriptor aPropertyDescriptor)
	{
		if (aPropertyDescriptor.getReadMethod() == null)
		{
			return false;
		}

		if (aPropertyDescriptor.getWriteMethod() == null)
		{
			return false;
		}

		return true;
	}

	public static String getEmberJsType(Class<?> aJavaType)
	{
	    if(javaSimpleTypesRepresentingEmberBooleans.contains(aJavaType))
	    {
	    	return "boolean";
	    }
	    if(javaSimpleTypesRepresentingEmberNumbers.contains(aJavaType))
	    {
	    	return "number";
	    }
	    if(javaSimpleTypesRepresentingEmberDates.contains(aJavaType))
	    {
	    	return "date";
	    }
	    if(javaSimpleTypesRepresentingEmberStrings.contains(aJavaType))
	    {
	    	return "string";
	    }
	    for(Object o : javaTypesRepresentingEmberArrays)
	    {
	    	Class<?> cls = (Class<?>)o;
	    	if(cls.isAssignableFrom(aJavaType)) return "array" ;
	    }
	    
	    return "object";
	}

}
