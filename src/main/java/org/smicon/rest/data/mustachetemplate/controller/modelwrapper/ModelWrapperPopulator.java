package org.smicon.rest.data.mustachetemplate.controller.modelwrapper;

import java.beans.PropertyDescriptor;

import org.smicon.rest.data.embermodel.EmberModelTypeConfigurationI;
import org.smicon.rest.data.entitymeta.EntityValidationDataI;
import org.smicon.rest.data.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.data.metas.EntityPropertyMeta;
import org.smicon.rest.data.metas.ModelMeta;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.data.populators.abstracts.ModelPopulator;
import org.smicon.rest.functionality.entitymeta.EntityValidationFunctions;

public class ModelWrapperPopulator 
extends ModelPopulator
<
ModelWrapperSimplePropertyPopulator,
ModelWrapperEntityPropertyWithSimpleIdsPopulator,
ModelWrapperEntityPropertyWithEmbeddedIdsPopulator,
ModelWrapperEntityPropertyWithCompositeIdsPopulator,
ModelWrapperEntityCollectionPropertyPopulator
>
{

	public ModelWrapperPopulator(ModelMeta aMeta, EntityValidationDataI aData, ModelMetaRegistryI aRegistry, EmberModelTypeConfigurationI aConfiguration) throws Exception {
		super(aMeta, aData, aRegistry, aConfiguration);
	}

	protected void populate() throws Exception {
		for(PropertyDescriptor desc : this.getModelEntityMeta().getSimpleProperties().values()) {
			if(EntityValidationFunctions.isCompletePropertyDescriptor(desc)) {
				this.simpleProperties.add(new ModelWrapperSimplePropertyPopulator(desc, this.getRegistry()));
			}
		}
		for(EntityPropertyMeta meta : this.getModelEntityMeta().getChildEntityPropertiesWithSimpleIds().values()) {
			this.entityPropertiesWithSimpleIds.add(new ModelWrapperEntityPropertyWithSimpleIdsPopulator(meta, this.getRegistry()));
		}
		for(EntityPropertyMeta meta : this.getModelEntityMeta().getChildEntityPropertiesWithEmbeddedIds().values()) {
			this.entityPropertiesWithEmbeddedIds.add(new ModelWrapperEntityPropertyWithEmbeddedIdsPopulator(meta, this.getRegistry()));
		}
		for(EntityPropertyMeta meta : this.getModelEntityMeta().getChildEntityPropertiesWithCompositeIds().values()) {
			this.entityPropertiesWithCompositeIds.add(new ModelWrapperEntityPropertyWithCompositeIdsPopulator(meta, this.getRegistry()));
		}
		for(EntityCollectionPropertyMeta meta : this.getModelEntityMeta().getEntityCollectionProperties().values()) {
			this.entityCollectionProperties.add(new ModelWrapperEntityCollectionPropertyPopulator(meta, this.getRegistry()));
		}
	}
}
