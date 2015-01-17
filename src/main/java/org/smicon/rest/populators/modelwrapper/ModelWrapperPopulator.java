package org.smicon.rest.populators.modelwrapper;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;

import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.EntityPropertyMeta;
import org.smicon.rest.metas.Metas;
import org.smicon.rest.populators.abstracts.ModelExtensionPopulator;

public class ModelWrapperPopulator 
extends ModelExtensionPopulator 
{

	// Child populators
	private ArrayList<ModelWrapperSimplePropertyPopulator> simpleProperties = new ArrayList<ModelWrapperSimplePropertyPopulator>();
	private ArrayList<ModelWrapperEntityPropertyWithSimpleIdsPopulator> entityPropertiesWithSimpleIds = new ArrayList<ModelWrapperEntityPropertyWithSimpleIdsPopulator>();
	private ArrayList<ModelWrapperEntityPropertyWithEmbeddedIdsPopulator> entityPropertiesWithEmbeddedIds = new ArrayList<ModelWrapperEntityPropertyWithEmbeddedIdsPopulator>();
	private ArrayList<ModelWrapperEntityPropertyWithCompositeIdsPopulator> entityPropertiesWithCompositeIds = new ArrayList<ModelWrapperEntityPropertyWithCompositeIdsPopulator>();
	private ArrayList<ModelWrapperEntityCollectionPropertyPopulator> entityCollectionProperties = new ArrayList<ModelWrapperEntityCollectionPropertyPopulator>();
	
	private Object simplePropertyWrapper;
	private Object propertiesWithSimpleIdsWrapper;
	private Object propertiesWithEmbeddedIdsWrapper;
	private Object propertiesWithCompositeIdsWrapper;
	private Object entityCollectionPropertyWrapper;
	
	public ModelWrapperPopulator(Class<?> aModelClass) throws Exception {
		super(aModelClass);
		this.populate();
	}

	private void populate() throws Exception {
		for(PropertyDescriptor desc : this.modelEntityMeta.getSimpleProperties().values()) {
			if(Metas.isCompletePropertyDescriptor(desc)) {
				this.simpleProperties.add(new ModelWrapperSimplePropertyPopulator(desc));
			}
		}
		for(EntityPropertyMeta meta : this.modelEntityMeta.getChildEntityPropertiesWithSimpleIds().values()) {
			this.entityPropertiesWithSimpleIds.add(new ModelWrapperEntityPropertyWithSimpleIdsPopulator(meta));
		}
		for(EntityPropertyMeta meta : this.modelEntityMeta.getChildEntityPropertiesWithEmbeddedIds().values()) {
			this.entityPropertiesWithEmbeddedIds.add(new ModelWrapperEntityPropertyWithEmbeddedIdsPopulator(meta));
		}
		for(EntityPropertyMeta meta : this.modelEntityMeta.getChildEntityPropertiesWithCompositeIds().values()) {
			this.entityPropertiesWithCompositeIds.add(new ModelWrapperEntityPropertyWithCompositeIdsPopulator(meta));
		}
		for(EntityCollectionPropertyMeta meta : this.modelEntityMeta.getEntityCollectionProperties().values()) {
			this.entityCollectionProperties.add(new ModelWrapperEntityCollectionPropertyPopulator(meta));
		}
		
		this.simplePropertyWrapper = new Object() { public ArrayList<ModelWrapperSimplePropertyPopulator> properties = simpleProperties; };
		this.propertiesWithSimpleIdsWrapper = new Object() { public ArrayList<ModelWrapperEntityPropertyWithSimpleIdsPopulator> properties = entityPropertiesWithSimpleIds; };
		this.propertiesWithEmbeddedIdsWrapper = new Object() { public ArrayList<ModelWrapperEntityPropertyWithEmbeddedIdsPopulator> properties = entityPropertiesWithEmbeddedIds; };
		this.propertiesWithCompositeIdsWrapper = new Object() { public ArrayList<ModelWrapperEntityPropertyWithCompositeIdsPopulator> properties = entityPropertiesWithCompositeIds; };
		this.entityCollectionPropertyWrapper = new Object() { public ArrayList<ModelWrapperEntityCollectionPropertyPopulator> properties = entityCollectionProperties; };
	}

	/*
	 * *****************************************************************************
	 * Child populators
	 * *****************************************************************************
	 */
	
	public Object getSimpleProperties() {
		if(simpleProperties.size() > 0)  return simplePropertyWrapper; else return null;
	}

	public Object getEntityPropertiesWithSimpleIds() {
		if(entityPropertiesWithSimpleIds.size() > 0)  return propertiesWithSimpleIdsWrapper; else return null;
	}

	public Object getEntityPropertiesWithEmbeddedIds() {
		if(entityPropertiesWithEmbeddedIds.size() > 0)  return propertiesWithEmbeddedIdsWrapper; else return null;
	}

	public Object getEntityPropertiesWithCompositeIds() {
		if(entityPropertiesWithCompositeIds.size() > 0)  return propertiesWithCompositeIdsWrapper; else return null;
	}
	
	public Object getEntityCollectionProperties() {
		if(entityCollectionProperties.size() > 0)  return entityCollectionPropertyWrapper; else return null;
	}
	
}
