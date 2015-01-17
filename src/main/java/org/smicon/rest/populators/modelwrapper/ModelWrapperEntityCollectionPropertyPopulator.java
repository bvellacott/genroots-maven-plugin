package org.smicon.rest.populators.modelwrapper;

import java.util.Collection;
import java.util.Map;

import org.smicon.rest.exceptions.IncorrectEntityStructureException;
import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.EntityPropertyMeta;

public class ModelWrapperEntityCollectionPropertyPopulator 
extends
ModelWrapperSimplePropertyPopulator
{
	
	private Class<Collection<?>> collectionType;
	private EntityCollectionPropertyMeta meta;

	public ModelWrapperEntityCollectionPropertyPopulator(EntityCollectionPropertyMeta aMeta) throws Exception {
		super(aMeta.getPropertyDescriptor());
		
		if(!(Collection.class.isAssignableFrom(this.propertyDescriptor.getPropertyType()) ||
				Map.class.isAssignableFrom(this.propertyDescriptor.getPropertyType()))) {
			throw new IncorrectEntityStructureException("The property: " + this.getParameterName() +" doesn't represent a Collection or a Map!");
		}
		collectionType = (Class<Collection<?>>)this.propertyDescriptor.getPropertyType();
		this.meta = aMeta;
	}
	
	public String getPlural() {
		return this.propertyDescriptor.getName();
	}
	
	public String getTargetType() {
		return this.meta.getTargetType().getSimpleName();
	}

}
