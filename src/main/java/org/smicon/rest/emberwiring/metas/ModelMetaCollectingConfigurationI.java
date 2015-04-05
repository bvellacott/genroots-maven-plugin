package org.smicon.rest.emberwiring.metas;

import java.util.List;

import org.reflections.Reflections;

public interface ModelMetaCollectingConfigurationI
{

	public static final ModelValidationDataI default_model_validation_data = ModelValidationDataBuilder.default_instance;

	String getPackageName();
	
	Reflections getReflections();
	
	List<String> getCompiledClasspathElements();
	
	ModelValidationDataI getModelValidationData();
	
}
