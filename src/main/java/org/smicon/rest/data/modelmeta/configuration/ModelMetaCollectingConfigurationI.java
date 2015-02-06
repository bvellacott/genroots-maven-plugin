package org.smicon.rest.data.modelmeta.configuration;

import java.util.Arrays;
import java.util.List;

import org.reflections.Reflections;

public interface ModelMetaCollectingConfigurationI
{

	public static final String default_package_name = "default";
	public static final Reflections default_reflections = new Reflections(default_package_name);
	public static final List<String> default_classpath_elements = Arrays.asList("target/project/WEB-INF/classes/" + default_package_name);

	String getPackageName();
	
	Reflections getReflections();
	
	List<String> getCompiledClasspathElements();
	
}
