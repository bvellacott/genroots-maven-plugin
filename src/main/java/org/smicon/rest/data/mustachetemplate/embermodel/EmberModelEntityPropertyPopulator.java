package org.smicon.rest.data.mustachetemplate.embermodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.smicon.rest.data.metas.EntityIdMeta;
import org.smicon.rest.data.metas.EntityPropertyMeta;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.data.mustachetemplate.controller.MutableControllerTemplateData;
import org.smicon.rest.data.mustachetemplate.controller.modelwrapper.IdAccessorPopulator;
import org.smicon.rest.data.mustachetemplate.controller.modelwrapper.IdPropertyPopulator;
import org.smicon.rest.data.populators.abstracts.ModelPropertyPopulator;

public class EmberModelEntityPropertyPopulator
extends
ModelPropertyPopulator
{
	protected EntityPropertyMeta entityPropertyMeta;
	private ArrayList<IdPropertyPopulator> idProperties = new ArrayList<IdPropertyPopulator>();
	
	public EmberModelEntityPropertyPopulator(EntityPropertyMeta aEntityPropertyMeta, ModelMetaRegistryI aRegistry) {
		super(aEntityPropertyMeta.getPropertyDescriptor(), aRegistry);
		this.entityPropertyMeta = aEntityPropertyMeta;
	}

	public String getTargetTypeObjectForm()
	{
		return this.entityPropertyMeta.getChildMeta().getEntityClass().getSimpleName();
	}

	public String getTargetTypeStringForm()
	{
		String objForm = this.getTargetTypeObjectForm();
		return objForm.substring(0,1).toLowerCase() + objForm.substring(1);
	}

}
