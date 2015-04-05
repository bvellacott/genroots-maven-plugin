package org.smicon.rest.emberwiring.metas;

import java.util.Arrays;
import java.util.List;

import org.reflections.Reflections;
import org.smicon.rest.emberwiring.general.Builders.BuilderI;
import org.smicon.rest.emberwiring.general.pools.Pools;

public class ModelMetaCollectingConfigurationBuilder
implements 
BuilderI<ModelMetaCollectingConfigurationI>
{
	String packageName;
	Reflections reflections;
	List<String> classpathElements = null;
	ModelValidationDataI validationData = ModelMetaCollectingConfigurationI.default_model_validation_data;
	
	public ModelMetaCollectingConfigurationBuilder(String aPackageName)
	{
		packageName = aPackageName;
	}
	
	public ModelMetaCollectingConfigurationBuilder withPackageName(String aPackageName) { packageName = aPackageName; return this;}
	public ModelMetaCollectingConfigurationBuilder withClasspathElements(List<String> aClasspathElements) { classpathElements = aClasspathElements; return this;}
	public ModelMetaCollectingConfigurationBuilder withValidationData(ModelValidationDataI aValidationData) { validationData = aValidationData; return this;}
	
	@Override
	public ModelMetaCollectingConfigurationI build() throws Exception
	{
		ModelMetaCollectingConfigurationInstance instance = new ModelMetaCollectingConfigurationInstance();
		instance.packageName = packageName;
		instance.reflections = Pools.reflectionsPool.borrowObject(packageName);
		if(classpathElements != null)
			instance.classpathElements = classpathElements;
		else
			instance.classpathElements = Arrays.asList("target/project/WEB-INF/classes/" + packageName);
		instance.validationData = validationData;
		return instance;
	}

	static class ModelMetaCollectingConfigurationInstance
	implements
	ModelMetaCollectingConfigurationI
	{
	
		String packageName;
		Reflections reflections;
		List<String> classpathElements;
		ModelValidationDataI validationData;
		
		@Override public String getPackageName() { return this.packageName; }
	
		@Override public Reflections getReflections() { return this.reflections; }
	
		@Override public List<String> getCompiledClasspathElements() { return this.classpathElements; }
	
		@Override public ModelValidationDataI getModelValidationData() { return null; }
	
	}
}
