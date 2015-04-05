package org.smicon.rest.emberwiring.general.factories;

import java.beans.BeanInfo;
import java.beans.Beans;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.reflections.Reflections;
import org.smicon.rest.emberwiring.exceptions.IllegalMutablePropertyException;
import org.smicon.rest.emberwiring.exceptions.IncorrectConfigurationException;

import com.google.inject.internal.util.Classes;

public final class DefaultingFactoryFunctions
{
	public static <I, M extends I> void validateConfiguration(DefaultingFactoryConfigurationI<I, M> aConfiguration) throws Exception
	{
		if(aConfiguration.getDefault() == null)
		{
			throw new IncorrectConfigurationException("No default configuration has been provided!");
		}
		
		if(!Classes.isConcrete(aConfiguration.getMutableType()))
		{
			throw new IncorrectConfigurationException("The mutable type doesn't represent a concrete class!");
		}
		
		Constructor<M> constructor = aConfiguration.getMutableType().getConstructor();
		if(constructor == null || Modifier.isPublic(constructor.getModifiers()))
		{
			throw new IncorrectConfigurationException("The mutable type has no public constructor without parameters!");
		}
		
		BeanInfo immutableInfo = Introspector.getBeanInfo(aConfiguration.getDefault().getClass(), Introspector.USE_ALL_BEANINFO);
		BeanInfo mutableInfo = Introspector.getBeanInfo(aConfiguration.getMutableType(), Introspector.USE_ALL_BEANINFO);

		PropertyDescriptor[] immutableDescriptors = immutableInfo.getPropertyDescriptors();
		
		HashMap<String, PropertyDescriptor> mutableDescriptorMap = new HashMap<String, PropertyDescriptor>();
		for(PropertyDescriptor desc : mutableInfo.getPropertyDescriptors())
		{
			if(!desc.getName().equals("class"))
			{
				mutableDescriptorMap.put(desc.getName(), desc);
			}
		}
		
		for(PropertyDescriptor immutableDesc : immutableDescriptors)
		{
			if(!immutableDesc.getName().equals("class") && immutableDesc.getReadMethod() != null)
			{
				PropertyDescriptor mutableDesc = mutableDescriptorMap.get(immutableDesc.getName());
				try
				{
					validateMutablePropertyDescriptor(aConfiguration.getMutableType(), immutableDesc, mutableDesc);
				}
				catch(Exception e)
				{
					throw new IncorrectConfigurationException("The mutable type is invalid!", e);
				}
			}
		}
		
	}
	
	public static <I, M extends I> void formatInstance(I aDefault, M aInstance) throws Exception
	{
		BeanInfo immutableInfo = Introspector.getBeanInfo(aDefault.getClass(), Introspector.USE_ALL_BEANINFO);

		for(PropertyDescriptor desc : immutableInfo.getPropertyDescriptors())
		{
			copyProperty(aDefault, aInstance, desc.getReadMethod());
		}
	}
	
	
	public static void copyProperty(Object aSource, Object aTarget, Method aGetter) throws Exception
	{
		if(aGetter.getName().equals("getClass")) return;
		
		Method setter = aTarget.getClass().getMethod(aGetter.getName().replaceFirst("g", "s"), aGetter.getReturnType());
		setter.invoke(aTarget, aGetter.invoke(aSource, aGetter.getReturnType()));
	}
	
	public static <M> void enullInstance(M aInstance) throws Exception
	{
		BeanInfo mutableInfo = Introspector.getBeanInfo(aInstance.getClass(), Introspector.USE_ALL_BEANINFO);

		for(PropertyDescriptor desc : mutableInfo.getPropertyDescriptors())
		{
			enullProperty(aInstance, desc.getReadMethod());
		}
	}
	
	public static void enullProperty(Object aInstance, Method aSetter) throws Exception
	{
		if(aSetter == null) return;
		
		if(aSetter.getReturnType().isPrimitive()) return;
		
		aSetter.invoke(aInstance, (Object)null);
	}
	
	public static void validateMutablePropertyDescriptor(Class<?> aOwningClass, PropertyDescriptor aImmutableDescriptor, PropertyDescriptor aMutableDescriptor) throws Exception
	{
		if(aMutableDescriptor == null)
		{
			throw new IllegalMutablePropertyException("No property by the name: " + aImmutableDescriptor.getName() + " exists for the mutable type: " + aOwningClass.getSimpleName() + "!");
		}
		
		if(aMutableDescriptor.getWriteMethod() == null)
		{
			throw new IllegalMutablePropertyException("No reader(getter) method exists for the property: " + aMutableDescriptor.getName() + " in the class: " + aOwningClass.getSimpleName() + "!");
		}
	}
}
