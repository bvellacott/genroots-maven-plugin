package org.smicon.rest.populators.embermodel;

import java.beans.PropertyDescriptor;

import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.EntityPropertyMeta;
import org.smicon.rest.metas.Metas;
import org.smicon.rest.populators.abstracts.ModelPopulator;

public class EmberModelPopulator
extends
ModelPopulator<EmberModelSimplePropertyPopulator, EmberModelEntityPropertyPopulator, EmberModelEntityPropertyPopulator, EmberModelEntityPropertyPopulator, EmberModelEntityCollectionPropertyPopulator>
{
	public EmberModelPopulator(Class<?> aModelClass) throws Exception
	{
		super(aModelClass);
	}

	protected void populate() throws Exception
	{
		for (PropertyDescriptor desc : this.modelEntityMeta.getSimpleProperties().values())
		{
			if (Metas.isCompletePropertyDescriptor(desc) &&
			!desc.getName().equalsIgnoreCase("id") &&
			!this.modelEntityMeta.getEmbeddedIdPathParameterMetas().keySet().contains(desc.getName()) &&
			!this.modelEntityMeta.getIdPathParameterMetas().keySet().contains(desc.getName()))
			{
				this.simpleProperties.add(new EmberModelSimplePropertyPopulator(desc));
			}
		}
		for (EntityPropertyMeta meta : this.modelEntityMeta.getChildEntityPropertiesWithSimpleIds().values())
		{
			this.entityPropertiesWithSimpleIds.add(new EmberModelEntityPropertyPopulator(meta));
		}
		for (EntityPropertyMeta meta : this.modelEntityMeta.getChildEntityPropertiesWithEmbeddedIds().values())
		{
			this.entityPropertiesWithEmbeddedIds.add(new EmberModelEntityPropertyPopulator(meta));
		}
		for (EntityPropertyMeta meta : this.modelEntityMeta.getChildEntityPropertiesWithCompositeIds().values())
		{
			this.entityPropertiesWithCompositeIds.add(new EmberModelEntityPropertyPopulator(meta));
		}
		for (EntityCollectionPropertyMeta meta : this.modelEntityMeta.getEntityCollectionProperties().values())
		{
			this.entityCollectionProperties.add(new EmberModelEntityCollectionPropertyPopulator(meta));
		}
	}
}
