package org.smicon.rest.populators.embermodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.smicon.rest.metas.EntityIdMeta;
import org.smicon.rest.metas.EntityPropertyMeta;
import org.smicon.rest.populators.RouteTemplatePopulator;
import org.smicon.rest.populators.abstracts.ModelPropertyPopulator;
import org.smicon.rest.populators.modelwrapper.IdAccessorPopulator;
import org.smicon.rest.populators.modelwrapper.IdPropertyPopulator;

public class EmberModelEntityPropertyPopulator
extends
ModelPropertyPopulator
{
	protected EntityPropertyMeta entityPropertyMeta;
	private ArrayList<IdPropertyPopulator> idProperties = new ArrayList<IdPropertyPopulator>();
	
	public EmberModelEntityPropertyPopulator(EntityPropertyMeta aEntityPropertyMeta) {
		super(aEntityPropertyMeta.getPropertyDescriptor());
		this.entityPropertyMeta = aEntityPropertyMeta;
	}

	public String getTargetTypeObjectForm()
	{
		return this.entityPropertyMeta.getChildMeta().getModelClass().getSimpleName();
	}

	public String getTargetTypeStringForm()
	{
		String objForm = this.getTargetTypeObjectForm();
		return objForm.substring(0,1).toLowerCase() + objForm.substring(1);
	}

}
