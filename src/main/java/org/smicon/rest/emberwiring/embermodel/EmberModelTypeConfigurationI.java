package org.smicon.rest.emberwiring.embermodel;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public interface EmberModelTypeConfigurationI
{
	
	public static final Set<?> default_java_types_representing_ember_numbers = ImmutableSet.builder().add(int.class, Integer.class, double.class, Double.class, float.class, Float.class, long.class, Long.class, short.class, Short.class, java.math.BigInteger.class, java.math.BigDecimal.class).build();
	public static final Set<?> default_java_types_representing_ember_booleans = ImmutableSet.builder().add(boolean.class, Boolean.class).build();;
	public static final Set<?> default_java_types_representing_ember_dates = ImmutableSet.builder().add(java.util.Date.class, java.sql.Date.class, java.sql.Timestamp.class, java.sql.Time.class, java.util.Calendar.class).build();;
	public static final Set<?> default_java_types_representing_ember_strings = ImmutableSet.builder().add(String.class, Enum.class, byte.class, Byte.class, char.class, Character.class, byte[].class, Byte[].class, char[].class, Character[].class).build();
	public static final Set<?> default_java_types_representing_ember_arrays = ImmutableSet.builder().add(Collection.class).build();
	public static final Set<?> default_java_types_representing_ember_objects = ImmutableSet.builder().add(Map.class).build();
	
	public Set<Class> getJavaTypesRepresentingEmberNumbers();

	public void setJavaTypesRepresentingEmberNumbers(Set<Class> javaTypesRepresentingEmberNumbers);

	public Set<Class> getJavaTypesRepresentingEmberBooleans();

	public void setJavaTypesRepresentingEmberBooleans(Set<Class> javaTypesRepresentingEmberBooleans);

	public Set<Class> getJavaTypesRepresentingEmberDates();
	
	public void setJavaTypesRepresentingEmberDates(Set<Class> javaTypesRepresentingEmberDates);

	public Set<Class> getJavaTypesRepresentingEmberStrings();

	public void setJavaTypesRepresentingEmberStrings(Set<Class> javaTypesRepresentingEmberStrings);

	public Set<Class> getJavaTypesRepresentingEmberArrays();

	public void setJavaTypesRepresentingEmberArrays(Set<Class> javaTypesRepresentingEmberArrays);

	public Set<Class> getJavaTypesRepresentingEmberObjects();

	public void setJavaTypesRepresentingEmberObjects(Set<Class> javaTypesRepresentingEmberObjects);

}
