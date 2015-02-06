package org.smicon.rest.data.entitymeta;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Primitives;

public interface EntityValidationDataI
{
	
	public static final Set<?> default_primitive_types = ImmutableSet.builder().addAll(Primitives.allPrimitiveTypes()).addAll(Primitives.allWrapperTypes()).build();
	public static final Set<?> default_non_primitive_id_types = ImmutableSet.builder().add(String.class, java.util.Date.class, java.sql.Date.class, java.math.BigDecimal.class, java.math.BigInteger.class).build();
	public static final Set<?> default_id_types = ImmutableSet.builder().addAll(default_primitive_types).addAll(default_non_primitive_id_types).build();
	
	Set<Class> getPrimitiveTypes();
	
	Set<Class> getNonPrimitiveIdTypes();
	
	Set<Class> getIdTypes();
	
}
