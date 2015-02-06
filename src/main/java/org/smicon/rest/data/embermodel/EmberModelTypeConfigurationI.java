package org.smicon.rest.data.embermodel;

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

	public Set<Class> getJavaTypesRepresentingEmberBooleans();

	public Set<Class> getJavaTypesRepresentingEmberDates();

	public Set<Class> getJavaTypesRepresentingEmberStrings();

	public Set<Class> getJavaTypesRepresentingEmberArrays();
	
	public Set<Class> getJavaTypesRepresentingEmberObjects();
	
}
