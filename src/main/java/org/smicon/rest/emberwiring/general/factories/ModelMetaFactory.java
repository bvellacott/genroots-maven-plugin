package org.smicon.rest.emberwiring.general.factories;

import static org.smicon.rest.emberwiring.metas.ModelFunctions.collectModelMeta;
import static org.smicon.rest.emberwiring.metas.ModelValidationFunctions.validateModel;

import java.util.HashMap;

import org.smicon.rest.emberwiring.metas.ModelMetaI;
import org.smicon.rest.emberwiring.metas.ModelValidationDataBuilder;
import org.smicon.rest.emberwiring.metas.ModelValidationDataI;
import org.smicon.rest.emberwiring.metas.ModelFunctions.ModelMetaInstance;

/**
 * This class operates on a single thread model and will most likely fail in a multi-threaded setup.
 * @author benjamin
 *
 */
public class ModelMetaFactory
implements
KeyedFactoryI<Class<?>, ModelMetaI, ModelValidationDataI>
{
	ModelValidationDataI validationData;
	
	// CACHE
	HashMap<Class<?>, ModelMetaI> isOrHasBeenCollectedMap = new HashMap();
	
	public ModelMetaFactory()
	{
		this(ModelValidationDataBuilder.default_instance);
	}
	
	public ModelMetaFactory(ModelValidationDataI aData)
	{
		validationData = aData;
	}
	
	@Override
	public ModelValidationDataI getConfiguration()
	{
		return validationData;
	}
	
	@Override
	public ModelMetaI create(Class<?> aModel) throws Exception
	{ 
		if(isOrHasBeenCollectedMap.containsKey(aModel)) return isOrHasBeenCollectedMap.get(aModel);
		validateModel(aModel, validationData);
		ModelMetaInstance metaInstance = new ModelMetaInstance();
		isOrHasBeenCollectedMap.put(aModel, metaInstance);
		collectModelMeta(aModel, metaInstance, this);
		return metaInstance;
	}

	@Override
	public boolean isCacheing() { return true; }

	@Override
	public boolean isInitialisingAfterCacheing() { return true; }

}
