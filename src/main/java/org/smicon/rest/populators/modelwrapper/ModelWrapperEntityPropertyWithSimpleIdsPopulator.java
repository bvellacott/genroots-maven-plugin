package org.smicon.rest.populators.modelwrapper;

import org.smicon.rest.exceptions.IncorrectEntityStructureException;
import org.smicon.rest.metas.EntityPropertyMeta;
import org.smicon.rest.metas.ModelEntityMeta;

public class ModelWrapperEntityPropertyWithSimpleIdsPopulator 
extends
ModelWrapperSimplePropertyPopulator
{
	protected EntityPropertyMeta entityPropertyMeta;
	
	public ModelWrapperEntityPropertyWithSimpleIdsPopulator(EntityPropertyMeta aEntityPropertyMeta) throws Exception {
		super(aEntityPropertyMeta.getPropertyDescriptor());
		this.entityPropertyMeta = aEntityPropertyMeta;
		
		if(!(this.entityPropertyMeta.getChildMeta() instanceof ModelEntityMeta))
		{
			throw new IncorrectEntityStructureException(
				"The target type of a public entity relationship doesn't point to a valid Model entity. i.e The target entity is not annotated @Model.");
		}
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
