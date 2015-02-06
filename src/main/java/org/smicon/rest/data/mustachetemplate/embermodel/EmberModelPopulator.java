package org.smicon.rest.data.mustachetemplate.embermodel;

import java.beans.PropertyDescriptor;

import org.smicon.rest.data.embermodel.EmberModelTypeConfigurationI;
import org.smicon.rest.data.entitymeta.EntityValidationDataI;
import org.smicon.rest.data.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.data.metas.EntityPropertyMeta;
import org.smicon.rest.data.metas.ModelMeta;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.data.populators.abstracts.ModelPopulator;
import org.smicon.rest.functionality.entitymeta.EntityValidationFunctions;

public class EmberModelPopulator
extends
ModelPopulator<EmberModelSimplePropertyPopulator, EmberModelEntityPropertyPopulator, EmberModelEntityPropertyPopulator, EmberModelEntityPropertyPopulator, EmberModelEntityCollectionPropertyPopulator>
{
	public EmberModelPopulator(ModelMeta aMeta, EntityValidationDataI aData, ModelMetaRegistryI aRegistry, EmberModelTypeConfigurationI aConfiguration) throws Exception
	{
		super(aMeta, aData, aRegistry, aConfiguration);
	}

	protected void populate() throws Exception
	{
		for (PropertyDescriptor desc : this.getModelEntityMeta().getSimpleProperties().values())
		{
			if (EntityValidationFunctions.isCompletePropertyDescriptor(desc) &&
			!desc.getName().equalsIgnoreCase("id") &&
			!this.getModelEntityMeta().getEmbeddedIdPathParameterMetas().keySet().contains(desc.getName()) &&
			!this.getModelEntityMeta().getIdPathParameterMetas().keySet().contains(desc.getName()))
			{
				this.simpleProperties.add(new EmberModelSimplePropertyPopulator(desc, this.getRegistry(), this.getConfiguration()));
			}
		}
		for (EntityPropertyMeta meta : this.getModelEntityMeta().getChildEntityPropertiesWithSimpleIds().values())
		{
			this.entityPropertiesWithSimpleIds.add(new EmberModelEntityPropertyPopulator(meta, this.getRegistry()));
		}
		for (EntityPropertyMeta meta : this.getModelEntityMeta().getChildEntityPropertiesWithEmbeddedIds().values())
		{
			this.entityPropertiesWithEmbeddedIds.add(new EmberModelEntityPropertyPopulator(meta, this.getRegistry()));
		}
		for (EntityPropertyMeta meta : this.getModelEntityMeta().getChildEntityPropertiesWithCompositeIds().values())
		{
			this.entityPropertiesWithCompositeIds.add(new EmberModelEntityPropertyPopulator(meta, this.getRegistry()));
		}
		for (EntityCollectionPropertyMeta meta : this.getModelEntityMeta().getEntityCollectionProperties().values())
		{
			this.entityCollectionProperties.add(new EmberModelEntityCollectionPropertyPopulator(meta, this.getRegistry()));
		}
	}
}
