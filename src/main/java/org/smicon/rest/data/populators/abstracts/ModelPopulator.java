package org.smicon.rest.data.populators.abstracts;

import java.util.ArrayList;

import org.smicon.rest.data.embermodel.EmberModelTypeConfigurationI;
import org.smicon.rest.data.entitymeta.EntityValidationDataI;
import org.smicon.rest.data.metas.ModelMeta;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;

public abstract class ModelPopulator<A extends TemplatePopulator, B extends TemplatePopulator, C extends TemplatePopulator, D extends TemplatePopulator, E extends TemplatePopulator>
extends
ModelExtensionPopulator
{

	// Child populators
	protected ArrayList<A> simpleProperties = new ArrayList<A>();
	protected ArrayList<B> entityPropertiesWithSimpleIds = new ArrayList<B>();
	protected ArrayList<C> entityPropertiesWithEmbeddedIds = new ArrayList<C>();
	protected ArrayList<D> entityPropertiesWithCompositeIds = new ArrayList<D>();
	protected ArrayList<E> entityCollectionProperties = new ArrayList<E>();

	protected Object simplePropertyWrapper;
	protected Object propertiesWithSimpleIdsWrapper;
	protected Object propertiesWithEmbeddedIdsWrapper;
	protected Object propertiesWithCompositeIdsWrapper;
	protected Object entityCollectionPropertyWrapper;

	public ModelPopulator(ModelMeta aMeta, EntityValidationDataI aData, ModelMetaRegistryI aRegistry, EmberModelTypeConfigurationI aConfiguration) throws Exception
	{
		super(aMeta, aData, aRegistry, aConfiguration);
		this.populate();
		this.wrap();
	}

	protected abstract void populate() throws Exception;

	protected void wrap()
	{
		this.simplePropertyWrapper = new Object() {
			public ArrayList<A> properties = simpleProperties;
		};
		this.propertiesWithSimpleIdsWrapper = new Object() {
			public ArrayList<B> properties = entityPropertiesWithSimpleIds;
		};
		this.propertiesWithEmbeddedIdsWrapper = new Object() {
			public ArrayList<C> properties = entityPropertiesWithEmbeddedIds;
		};
		this.propertiesWithCompositeIdsWrapper = new Object() {
			public ArrayList<D> properties = entityPropertiesWithCompositeIds;
		};
		this.entityCollectionPropertyWrapper = new Object() {
			public ArrayList<E> properties = entityCollectionProperties;
		};
	}

	/*
	 * *****************************************************************************
	 * Child populators
	 * *********************************************************
	 * ********************
	 */

	public Object getSimpleProperties()
	{
		if (simpleProperties.size() > 0) return simplePropertyWrapper;
		else
			return null;
	}

	public Object getEntityPropertiesWithSimpleIds()
	{
		if (entityPropertiesWithSimpleIds.size() > 0) return propertiesWithSimpleIdsWrapper;
		else
			return null;
	}

	public Object getEntityPropertiesWithEmbeddedIds()
	{
		if (entityPropertiesWithEmbeddedIds.size() > 0) return propertiesWithEmbeddedIdsWrapper;
		else
			return null;
	}

	public Object getEntityPropertiesWithCompositeIds()
	{
		if (entityPropertiesWithCompositeIds.size() > 0) return propertiesWithCompositeIdsWrapper;
		else
			return null;
	}

	public Object getEntityCollectionProperties()
	{
		if (entityCollectionProperties.size() > 0) return entityCollectionPropertyWrapper;
		else
			return null;
	}

}
