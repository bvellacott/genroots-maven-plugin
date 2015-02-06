package org.smicon.rest.data.wiring;

import java.io.File;
import java.util.List;

import org.smicon.rest.data.modelmeta.configuration.ModelMetaCollectingConfigurationI;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.instances.logging.LoggerI;

import com.github.mustachejava.MustacheFactory;

public interface MutableWiringConfigurationI
extends
WiringConfigurationI
{
	
	void setMustacheFactory(MustacheFactory aFactory);
	
	void setControllerOutputDirectory(File aDir);
	
	void setEmberModelOutputDirectory(File aDir);
	
	void setResourceRoot(String aRoot);
	
	void setControllerTemplateFileName(String aFileName);
	
	void setEmberModelTemplateFileName(String aFileName);
	
	void setControllerClassPostfix(String aPostfix);
	
	void setPackageName(String aPackageName);

	void setLogger(LoggerI aLogger);

	void setMetaCollectingConfiguration(ModelMetaCollectingConfigurationI aMetaCollectingConfiguration);
	
	void setMetaRegistry(ModelMetaRegistryI aMetaRegistry);
	
}
