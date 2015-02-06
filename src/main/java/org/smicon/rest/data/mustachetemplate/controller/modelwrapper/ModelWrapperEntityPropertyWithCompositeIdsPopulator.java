package org.smicon.rest.data.mustachetemplate.controller.modelwrapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.smicon.rest.data.metas.EntityIdMeta;
import org.smicon.rest.data.metas.EntityPropertyMeta;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.data.mustachetemplate.controller.MutableControllerTemplateData;

public class ModelWrapperEntityPropertyWithCompositeIdsPopulator
extends
ModelWrapperEntityPropertyWithSimpleIdsPopulator
{

	private ArrayList<IdPropertyPopulator> idProperties = new ArrayList<IdPropertyPopulator>();
	private ArrayList<IdAccessorPopulator> idAccessors = new ArrayList<IdAccessorPopulator>();

	public ModelWrapperEntityPropertyWithCompositeIdsPopulator(EntityPropertyMeta aEntityPropertyMeta, ModelMetaRegistryI aRegistry) throws Exception
	{
		super(aEntityPropertyMeta, aRegistry);
		this.populate();
	}

	private void populate()
	{
		LinkedHashMap<String, EntityIdMeta> metas = this.entityPropertyMeta.getChildMeta().getIdPathParameterMetas();
		int index = 0;
		for (String propName : metas.keySet())
		{
			EntityIdMeta propMeta = metas.get(propName);
			if (propMeta.getPropertyDescriptor() != null && propMeta.getPropertyDescriptor().getReadMethod() != null
				&& propMeta.getPropertyDescriptor().getWriteMethod() != null)
			{
				this.idAccessors.add(new IdAccessorPopulator(propMeta.getPropertyDescriptor(), index, MutableControllerTemplateData.urlIdDelimiter));
			}
			else
			{
				this.idProperties.add(new IdPropertyPopulator(propMeta.getFieldType(), propName, index, MutableControllerTemplateData.urlIdDelimiter));
			}

			index++;
		}
	}

	public ArrayList<IdPropertyPopulator> getIdProperties()
	{
		return idProperties;
	}

	public ArrayList<IdAccessorPopulator> getIdAccessors()
	{
		return idAccessors;
	}

}
