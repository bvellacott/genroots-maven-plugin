package org.smicon.rest.populators.abstracts;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.smicon.rest.exceptions.IncorrectIdStructureException;
import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.EntityIdMeta;
import org.smicon.rest.metas.EntityMeta;
import org.smicon.rest.metas.EntityPropertyMeta;
import org.smicon.rest.metas.Metas;
import org.smicon.rest.metas.ModelEntityMeta;

import com.google.common.primitives.Primitives;


public abstract class ModelExtensionPopulator 
extends 
TemplatePopulator 
{
	
	protected ModelEntityMeta modelEntityMeta;
	
	public ModelExtensionPopulator(Class<?> aModelClass) throws Exception {
		this.modelEntityMeta = (ModelEntityMeta) Metas.getEntityMeta(aModelClass);
	}

	public ModelEntityMeta getModelEntityMeta() {
		return modelEntityMeta;
	}

	public String getPackageName() {
		return this.getModelEntityMeta().getModelClass().getPackage().getName();
	}

	public String getClassName() {
		return this.getModelEntityMeta().getModelClass().getSimpleName();
	}

	public String getPlural() {
		return this.getModelEntityMeta().getPlural();
	}

	public String getSingular() {
		return this.getModelEntityMeta().getSingular();
	}

	public String getPersistenceUnitName() {
		return this.getModelEntityMeta().getPersistenceUnitName();
	}

	public String getIdClassName() {
		return Primitives.wrap(this.getModelEntityMeta().getIdClass()).getSimpleName();
	}
	
	private void addNonPrimitiveTypeToSet(Set<Class<?>> aSet, Class<?> aType) {
		if(!Primitives.unwrap(aType).isPrimitive()) {
			aSet.add(aType);
		}
	}

	public String getImports() {
		HashSet<Class<?>> imports = new HashSet<Class<?>>();
		
		addNonPrimitiveTypeToSet(imports, this.getModelEntityMeta().getIdClass());
		for(EntityIdMeta meta : this.getModelEntityMeta().getIdPathParameterMetas().values()) {
			addNonPrimitiveTypeToSet(imports, Primitives.wrap(meta.getFieldType()));
		}
		for(PropertyDescriptor pd : this.getModelEntityMeta().getSimpleProperties().values()) {
			addNonPrimitiveTypeToSet(imports, Primitives.wrap(pd.getPropertyType()));
		}
		for(EntityPropertyMeta propMeta : this.getModelEntityMeta().getChildEntityPropertiesWithSimpleIds().values()) {
			addNonPrimitiveTypeToSet(imports, propMeta.getChildMeta().getModelClass());
			for(EntityIdMeta childMeta : propMeta.getChildMeta().getIdPathParameterMetas().values()) {
				addNonPrimitiveTypeToSet(imports, Primitives.wrap(childMeta.getFieldType()));
			}
		}
		for(EntityPropertyMeta propMeta : this.getModelEntityMeta().getChildEntityPropertiesWithEmbeddedIds().values()) {
			addNonPrimitiveTypeToSet(imports, propMeta.getChildMeta().getModelClass());
			addNonPrimitiveTypeToSet(imports, propMeta.getChildMeta().getIdClass());
			for(EntityIdMeta childMeta : propMeta.getChildMeta().getIdPathParameterMetas().values()) {
				addNonPrimitiveTypeToSet(imports, Primitives.wrap(childMeta.getFieldType()));
			}
		}
		for(EntityPropertyMeta propMeta : this.getModelEntityMeta().getChildEntityPropertiesWithCompositeIds().values()) {
			addNonPrimitiveTypeToSet(imports, propMeta.getChildMeta().getModelClass());
			addNonPrimitiveTypeToSet(imports, propMeta.getChildMeta().getIdClass());
			for(EntityIdMeta childMeta : propMeta.getChildMeta().getIdPathParameterMetas().values()) {
				addNonPrimitiveTypeToSet(imports, Primitives.wrap(childMeta.getFieldType()));
			}
		}
		for(EntityCollectionPropertyMeta propMeta : this.getModelEntityMeta().getEntityCollectionProperties().values()) {
			addNonPrimitiveTypeToSet(imports, Primitives.wrap(propMeta.getPropertyDescriptor().getPropertyType()));
			addNonPrimitiveTypeToSet(imports, Primitives.wrap(propMeta.getTargetType()));
		}
		
		StringBuilder sb = new StringBuilder();
		for (Class<?> cls : imports) {
			sb.append("import ").append(cls.getName()).append(";\n");
		}
		return sb.toString();
	}

	public List<EntityIdMeta> getNonGeneratedIdParameterMetas() {
		ArrayList<EntityIdMeta> nonGeneratedMetas = new ArrayList<EntityIdMeta>();
		for(EntityIdMeta meta : this.getModelEntityMeta().getIdPathParameterMetas().values()) {
			if(!meta.isGenerated()) {
				nonGeneratedMetas.add(meta);
			}
		}
		return nonGeneratedMetas;
	}
	
	public List<EntityIdMeta> getIdParameterMetas() {
		ArrayList<EntityIdMeta> nonGeneratedMetas = new ArrayList<EntityIdMeta>(this.getModelEntityMeta().getIdPathParameterMetas().values());
		return nonGeneratedMetas;
	}
	
	public static String generateSetterLine(String aSubjectName, BeanInfo aSubjectInfo, String aParamName, Class<?> aIdType) throws Exception {
		if(Metas.isValidSimpleIdType(aIdType)) {
			return j.join(aParamName, " = ", aSubjectName, ";\n");
		}
		else {		 
	 		for (PropertyDescriptor pd : aSubjectInfo.getPropertyDescriptors()) {
				if(pd.getName().equals(aParamName) && pd.getWriteMethod() != null)	{
					return j.join(aSubjectName, ".", pd.getWriteMethod().getName(), "(", aParamName, ");\n");
				}
			}
			for(Field field : aSubjectInfo.getBeanDescriptor().getBeanClass().getDeclaredFields()) {
				if(field.getName().equals(aParamName)) {
					return j.join(aSubjectName, ".", aParamName, " = ", aParamName, ";\n");
				}
			}
			throw new IncorrectIdStructureException("The composite type: " + aIdType + 
					" doesn't contain a field or accessor for a field by the name: " + aParamName);
		}
	}

}
