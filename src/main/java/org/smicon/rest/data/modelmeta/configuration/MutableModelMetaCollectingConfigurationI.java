package org.smicon.rest.data.modelmeta.configuration;

import java.util.List;

public interface MutableModelMetaCollectingConfigurationI
extends
ModelMetaCollectingConfigurationI
{

	public void setPackageName(String aPackageName);
	
	public void setCompiledClasspathElements(List<String> aClasspathElements);
	
}
