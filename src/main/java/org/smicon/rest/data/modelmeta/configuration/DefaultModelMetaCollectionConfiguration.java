package org.smicon.rest.data.modelmeta.configuration;

import java.util.List;

import org.reflections.Reflections;

public class DefaultModelMetaCollectionConfiguration
implements
ModelMetaCollectingConfigurationI
{
	private static final DefaultModelMetaCollectionConfiguration instance = new DefaultModelMetaCollectionConfiguration();
	
	private DefaultModelMetaCollectionConfiguration()
	{}
	
	public static DefaultModelMetaCollectionConfiguration getInstance()
	{
		return instance;
	}

	@Override
	public String getPackageName()
	{
		return default_package_name;
	}

	@Override
	public Reflections getReflections()
	{
		return default_reflections;
	}

	@Override
	public List<String> getCompiledClasspathElements()
	{
		return default_classpath_elements;
	}

}
