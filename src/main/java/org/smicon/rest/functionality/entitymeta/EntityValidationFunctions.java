package org.smicon.rest.functionality.entitymeta;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.smicon.rest.data.entitymeta.EntityValidationDataI;
import org.smicon.rest.exceptions.IncorrectIdStructureException;

import com.google.common.primitives.Primitives;

public class EntityValidationFunctions
{

	public static boolean isValidSimpleIdType(Class<?> aType, EntityValidationDataI aData)
	{
		return aType.isPrimitive() || Primitives.allWrapperTypes().contains(aType) || aData.getNonPrimitiveIdTypes().contains(aType);
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

}
