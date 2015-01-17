package org.smicon.rest.populators.modelwrapper;

import org.smicon.rest.metas.EntityIdMeta;
import org.smicon.rest.metas.EntityPropertyMeta;

public class ModelWrapperEntityPropertyWithSimpleIdsPopulator 
extends
ModelWrapperSimplePropertyPopulator
{
	protected EntityPropertyMeta entityPropertyMeta;
	
	public ModelWrapperEntityPropertyWithSimpleIdsPopulator(EntityPropertyMeta aEntityPropertyMeta) {
		super(aEntityPropertyMeta.getPropertyDescriptor());
		this.entityPropertyMeta = aEntityPropertyMeta;
	}
	
	public String getIdType() {
		return this.entityPropertyMeta.getChildMeta().getIdClass().getSimpleName();
	}
	
	public String getIdGetterName() {
		String idName = this.entityPropertyMeta.getChildMeta().getSingularIdName();
		return this.entityPropertyMeta.getChildMeta().getSimpleProperties().get(idName).getReadMethod().getName();
	}
	
	public String getIdSetterName() {
		String idName = this.entityPropertyMeta.getChildMeta().getSingularIdName();
		return this.entityPropertyMeta.getChildMeta().getSimpleProperties().get(idName).getWriteMethod().getName();
	}
	
	public String getEntityType() {
		return this.entityPropertyMeta.getChildMeta().getModelClass().getSimpleName();
	}
}
