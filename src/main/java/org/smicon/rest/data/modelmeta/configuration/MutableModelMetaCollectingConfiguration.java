package org.smicon.rest.data.modelmeta.configuration;

import java.util.List;

import org.reflections.Reflections;

public class MutableModelMetaCollectingConfiguration
implements
MutableModelMetaCollectingConfigurationI
{

	String packageName;
	Reflections reflections;
	List<String> classpathElements;
	
	public MutableModelMetaCollectingConfiguration()
	{
		packageName = default_package_name;
		reflections = default_reflections;
		classpathElements = default_classpath_elements;
	}
	
	@Override
	public void setPackageName(String aPackageName)
	{
		this.packageName = aPackageName;
		this.reflections = new Reflections(this.getPackageName());
	}


	@Override
	public String getPackageName()
	{
		return this.packageName;
	}


	@Override
	public Reflections getReflections()
	{
		return this.reflections;
	}

	@Override
	public List<String> getCompiledClasspathElements()
	{
		return this.classpathElements;
	}

	@Override
	public void setCompiledClasspathElements(List<String> aClasspathElements)
	{
		this.classpathElements = aClasspathElements;
	}

}
