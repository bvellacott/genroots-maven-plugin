package org.smicon.rest.emberwiring.metas;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Primitives;

public interface ModelValidationDataI
{
	
	public static final Set<?> default_primitive_types = ImmutableSet.builder().addAll(Primitives.allPrimitiveTypes()).addAll(Primitives.allWrapperTypes()).build();
	public static final Set<?> default_non_primitive_id_types = ImmutableSet.builder().add(String.class, java.util.Date.class, java.sql.Date.class, java.math.BigDecimal.class, java.math.BigInteger.class).build();
	public static final Set<?> default_id_types = ImmutableSet.builder().addAll(default_primitive_types).addAll(default_non_primitive_id_types).build();
	public static final Set<?> default_singular_parameter_types = ImmutableSet.builder().addAll(default_primitive_types).add(String.class, java.math.BigInteger.class, java.math.BigDecimal.class, java.util.Date.class, java.util.Calendar.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, byte[].class, Byte[].class, char[].class, Character[].class).build();
	public static final Set<?> default_collection_parameter_types = ImmutableSet.builder().add(java.util.Collection.class, java.util.Set.class, java.util.List.class, java.util.Map.class).build();
	
	Set<Class> getPrimitiveTypes();
	
	Set<Class> getNonPrimitiveIdTypes();
	
	Set<Class> getIdTypes();
	
	Set<Class> getSingularParameterTypes();
	
	Set<Class> getCollectionParameterTypes();
	
}
