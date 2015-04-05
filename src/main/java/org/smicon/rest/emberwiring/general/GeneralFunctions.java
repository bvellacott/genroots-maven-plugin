package org.smicon.rest.emberwiring.general;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;


public final class GeneralFunctions
{

	public static <T> Iterable<T> iterableForArray(final T[] aArray)
	{
		return new Iterable<T>(){

			@Override
			public Iterator<T> iterator()
			{
				return Iterators.forArray(aArray);
			}
			
		};
	}
	
	public static <T> List<T> immutableListForArray(final T[] aArray)
	{
		return ImmutableList.copyOf(Iterators.forArray(aArray));
	}
	
	
	public static Map<String, Class<?>> getPropertyNamesAndTypes(Class<?> aClass) throws Exception
	{
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		BeanInfo info = Introspector.getBeanInfo(aClass);
		
		for(PropertyDescriptor desc : info.getPropertyDescriptors())
		{
			if(desc.getName().equals("class")) continue;
			map.put(desc.getName(), desc.getPropertyType());
		}
		
		for(Field field : aClass.getDeclaredFields())
		{
			map.put(field.getName(), field.getType());
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Class<?>> getAnnotatedPropertyNamesAndTypes(Class<?> aClass, @SuppressWarnings("rawtypes") Class aAnnotation) throws Exception
	{
		if(!aAnnotation.isAnnotation()) throw new IllegalArgumentException("The class: " + aAnnotation.getSimpleName() + " isn't an annotation!");
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		BeanInfo info = Introspector.getBeanInfo(aClass);
		
		for(PropertyDescriptor desc : info.getPropertyDescriptors())
		{
			if(desc.getReadMethod() == null) continue;
			
			if(desc.getReadMethod().getAnnotation(aAnnotation) != null) map.put(desc.getName(), desc.getPropertyType());
		}
		
		for(Field field : aClass.getDeclaredFields())
		{
			if(field.getAnnotation(aAnnotation) != null) map.put(field.getName(), field.getType());
		}
		
		return map;
	}

}
