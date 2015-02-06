package org.smicon.rest.data.wiring;

import java.io.File;

import org.smicon.rest.data.modelmeta.configuration.ModelMetaCollectingConfigurationI;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.instances.logging.LoggerI;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class DefaultWiringConfiguration
implements
WiringConfigurationI
{
	
	private static final WiringConfigurationI instance = new DefaultWiringConfiguration();

	public static WiringConfigurationI getInstance()
	{
		return instance;
	}
	
	public static MustacheFactory getDefaultMustacheFactory()
	{
		return default_mustache_factory;
	}

	@Override
	public File getControllerOutputDirectory()
	{
		// Create the directories if they are actually used
		if(!default_controller_output_directory.exists()) default_controller_output_directory.mkdirs();
		
		return default_controller_output_directory;
	}

	@Override
	public File getEmberModelOutputDirectory()
	{
		// Create the directories if they are actually used
		if(!default_ember_model_output_directory.exists()) default_ember_model_output_directory.mkdirs();
		
		return default_ember_model_output_directory;
	}

	@Override
	public String getResourceRoot()
	{
		return default_resource_root;
	}

	@Override
	public String getControllerTemplateFileName()
	{
		return default_controller_template_file_name;
	}

	@Override
	public String getEmberModelTemplateFileName()
	{
		return default_ember_model_template_file_name;
	}

	@Override
	public String getControllerClassPostfix()
	{
		return default_controller_class_postfix;
	}
	
	@Override
	public MustacheFactory getMustacheFactory()
	{
		return default_mustache_factory;
	}

	@Override
	public Mustache getControllerMustacheCompiler()
	{
		return this.getMustacheFactory().compile(this.getControllerTemplateFileName());
	}

	@Override
	public Mustache getEmberModelMustacheCompiler()
	{
		return this.getMustacheFactory().compile(this.getEmberModelTemplateFileName());
	}

	@Override
	public String getPackageName()
	{
		return default_package_name;
	}

	@Override
	public LoggerI getLogger()
	{
		return default_logger;
	}

	@Override
	public ModelMetaCollectingConfigurationI getMetaCollectingConfiguration()
	{
		return default_model_meta_collecting_configuration;
	}

	@Override
	public ModelMetaRegistryI getMetaRegistry()
	{
		return default_model_meta_registry;
	}

}
