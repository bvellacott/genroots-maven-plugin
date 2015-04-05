package org.smicon.rest.emberwiring.metas;

import java.util.Set;

import org.smicon.rest.emberwiring.general.Builders.BuilderI;

public class ModelValidationDataBuilder
implements
BuilderI<ModelValidationDataI>
{
	public static ModelValidationDataI default_instance = (new ModelValidationDataBuilder()).build();
	
	Set<Class> idTypes = (Set<Class>) ModelValidationDataI.default_id_types;
	Set<Class> nonPrimitiveIdTypes = (Set<Class>)ModelValidationDataI.default_non_primitive_id_types;
	Set<Class> primitiveTypes = (Set<Class>)ModelValidationDataI.default_primitive_types;
	Set<Class> singularParameterTypes = (Set<Class>)ModelValidationDataI.default_singular_parameter_types;
	Set<Class> collectionParameterTypes = (Set<Class>)ModelValidationDataI.default_collection_parameter_types;

	public void withIdTypes(Set<Class> aIdTypes) { idTypes = aIdTypes; }
	
	public void withNonPrimitiveIdTypes(Set<Class> aIonPrimitiveIdTypes) { nonPrimitiveIdTypes = aIonPrimitiveIdTypes; }
	
	public void withPrimitiveTypes(Set<Class> aPrimitiveTypes) { primitiveTypes = aPrimitiveTypes; }
	
	public void withSingularParameterTypes(Set<Class> aSingularParameterTypes) { singularParameterTypes = aSingularParameterTypes; }
	
	public void withCollectionParameterTypes(Set<Class> aCollectionParameterTypes) { collectionParameterTypes = aCollectionParameterTypes; }
	
	@Override
	public ModelValidationDataI build()
	{
		ModelValidationDataInstance instance = new ModelValidationDataInstance();
		instance.idTypes = idTypes;
		instance.nonPrimitiveIdTypes = nonPrimitiveIdTypes;
		instance.primitiveTypes = primitiveTypes;
		instance.singularParameterTypes = singularParameterTypes;
		instance.collectionParameterTypes = collectionParameterTypes;
		return instance;
	}	
	
	static class ModelValidationDataInstance
	implements
	ModelValidationDataI
	{
		Set<Class> idTypes;
		Set<Class> nonPrimitiveIdTypes;
		Set<Class> primitiveTypes;
		Set<Class> singularParameterTypes;
		Set<Class> collectionParameterTypes;
		
		@Override
		public Set<Class> getIdTypes() { return (Set<Class>)idTypes; }
	
		@Override
		public Set<Class> getNonPrimitiveIdTypes() { return (Set<Class>)nonPrimitiveIdTypes; }
	
		@Override
		public Set<Class> getPrimitiveTypes() { return (Set<Class>)primitiveTypes; }
	
		@Override
		public Set<Class> getSingularParameterTypes() { return (Set<Class>)singularParameterTypes; }
	
		@Override
		public Set<Class> getCollectionParameterTypes() { return (Set<Class>)collectionParameterTypes; }
	}
}
