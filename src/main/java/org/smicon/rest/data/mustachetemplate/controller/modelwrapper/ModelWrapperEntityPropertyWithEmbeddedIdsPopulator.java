package org.smicon.rest.data.mustachetemplate.controller.modelwrapper;

import org.smicon.rest.data.metas.EntityPropertyMeta;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;

public class ModelWrapperEntityPropertyWithEmbeddedIdsPopulator
extends
ModelWrapperEntityPropertyWithCompositeIdsPopulator
{

	public ModelWrapperEntityPropertyWithEmbeddedIdsPopulator(EntityPropertyMeta aEntityPropertyMeta, ModelMetaRegistryI aRegistry) throws Exception
	{
		super(aEntityPropertyMeta, aRegistry);
	}

}
