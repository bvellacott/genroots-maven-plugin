package org.smicon.rest.populators.modelwrapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.EntityIdMeta;
import org.smicon.rest.metas.EntityPropertyMeta;
import org.smicon.rest.populators.RouteTemplatePopulator;

public class ModelWrapperEntityPropertyWithCompositeIdsPopulator 
extends
ModelWrapperEntityPropertyWithSimpleIdsPopulator 
{
	
	private ArrayList<IdPropertyPopulator> idProperties = new ArrayList<IdPropertyPopulator>();
	private ArrayList<IdAccessorPopulator> idAccessors = new ArrayList<IdAccessorPopulator>();

	public ModelWrapperEntityPropertyWithCompositeIdsPopulator(EntityPropertyMeta aEntityPropertyMeta) {
		super(aEntityPropertyMeta);
		this.populate();
	}
	
	private void populate() {
		LinkedHashMap<String, EntityIdMeta> metas = this.entityPropertyMeta.getChildMeta().getIdPathParameterMetas();
		int index = 0;
		for(String propName : metas.keySet()) {
			EntityIdMeta propMeta = metas.get(propName);
			if(propMeta.getPropertyDescriptor() != null && 
					propMeta.getPropertyDescriptor().getReadMethod() != null && 
					propMeta.getPropertyDescriptor().getWriteMethod() != null) {
				this.idAccessors.add(new IdAccessorPopulator(propMeta.getPropertyDescriptor(), index, RouteTemplatePopulator.urlIdDelimiter));
			}
			else {
				this.idProperties.add(new IdPropertyPopulator(propMeta.getFieldType(), propName, index, RouteTemplatePopulator.urlIdDelimiter));
			}
			
			index++;
		}
	}

	public ArrayList<IdPropertyPopulator> getIdProperties() {
		return idProperties;
	}

	public ArrayList<IdAccessorPopulator> getIdAccessors() {
		return idAccessors;
	}
	
}
