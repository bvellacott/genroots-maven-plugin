package org.smicon.rest.populators.modelwrapper;

import java.beans.PropertyDescriptor;

import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.EntityPropertyMeta;
import org.smicon.rest.metas.Metas;
import org.smicon.rest.populators.abstracts.ModelPopulator;

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

	public ModelWrapperPopulator(Class<?> aModelClass) throws Exception {
		super(aModelClass);
	}

	protected void populate() throws Exception {
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
	}
}
