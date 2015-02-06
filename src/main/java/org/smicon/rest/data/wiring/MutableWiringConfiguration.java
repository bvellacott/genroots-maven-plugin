package org.smicon.rest.data.wiring;

import java.io.File;
import java.util.List;

import org.smicon.rest.BasicLoggers;
import org.smicon.rest.data.modelmeta.configuration.ModelMetaCollectingConfigurationI;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.instances.logging.LoggerI;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MutableWiringConfiguration
implements
MutableWiringConfigurationI
{
	
	MustacheFactory mustacheFactory;
	Mustache controllerTemplateCompiler;
	Mustache emberModelTemplateCompiler;
	File controllerOutputDirectory;
	File emberModelOutputDirectory;
	String resourceRoot;
	String controllerTemplateFileName;
	String emberModelTemplateFileName;
	String controllerClassPostfix;
	String packageName;
	List<String> classpathElements;
	LoggerI logger;
	ModelMetaCollectingConfigurationI metaCollectingConfiguration;
	ModelMetaRegistryI metaRegistry;

	public MutableWiringConfiguration()
	{
		this.setDefaultValues();
	}
	
	private void setDefaultValues()
	{
		this.mustacheFactory = WiringConfigurationI.default_mustache_factory;
		this.controllerOutputDirectory = WiringConfigurationI.default_controller_output_directory;
		this.emberModelOutputDirectory = WiringConfigurationI.default_ember_model_output_directory;
		this.resourceRoot = WiringConfigurationI.default_resource_root;
		this.controllerTemplateFileName = WiringConfigurationI.default_controller_template_file_name;
		this.emberModelTemplateFileName = WiringConfigurationI.default_ember_model_template_file_name;
		this.controllerClassPostfix = WiringConfigurationI.default_controller_class_postfix;
		this.packageName = WiringConfigurationI.default_package_name;
		this.logger = default_logger;
		this.metaCollectingConfiguration = default_model_meta_collecting_configuration;
		this.metaRegistry = default_model_meta_registry;
	}

	
	@Override
	public void setMustacheFactory(MustacheFactory aFactory)
	{
		if(aFactory == null)
		{
			throw new NullPointerException("Can't set a null factory!");
		}
		
		this.mustacheFactory = aFactory;
		
		// remove cached compilers
		this.controllerTemplateCompiler = null;
		this.emberModelTemplateCompiler = null;
	}

	@Override
	public void setControllerOutputDirectory(File controllerOutputDirectory)
	{
		this.controllerOutputDirectory = controllerOutputDirectory;
	}

	@Override
	public void setEmberModelOutputDirectory(File emberModelOutputDirectory)
	{
		this.emberModelOutputDirectory = emberModelOutputDirectory;
	}

	public void setResourceRoot(String resourceRoot)
	{
		this.resourceRoot = resourceRoot;
	}

	public void setControllerTemplateFileName(String cotrollerTemplateFileName)
	{
		if(cotrollerTemplateFileName == null)
		{
			throw new NullPointerException("Can't set a null controller template file name!");
		}
		
		this.controllerTemplateFileName = cotrollerTemplateFileName;
	}

	public void setEmberModelTemplateFileName(String emberModelTemplateFileName)
	{
		if(emberModelTemplateFileName == null)
		{
			throw new NullPointerException("Can't set a null ember model template file name!");
		}

		this.emberModelTemplateFileName = emberModelTemplateFileName;
	}

	public void setControllerClassPostfix(String controllerClassPostfix)
	{
		if(controllerClassPostfix == null)
		{
			throw new NullPointerException("Can't set a null controller class postfix!");
		}

		this.controllerClassPostfix = controllerClassPostfix;
	}

	public void setPackageName(String packageName)
	{
		if(packageName == null)
		{
			throw new NullPointerException("Can't set a null package name!");
		}

		this.packageName = packageName;
	}

	@Override
	public File getControllerOutputDirectory()
	{
		// Create the directories if they are actually used
		if(!controllerOutputDirectory.exists()) controllerOutputDirectory.mkdirs();
		
		return controllerOutputDirectory;
	}

	@Override
	public File getEmberModelOutputDirectory()
	{
		// Create the directories if they are actually used
		if(!emberModelOutputDirectory.exists()) emberModelOutputDirectory.mkdirs();
		
		return emberModelOutputDirectory;
	}

	@Override
	public String getResourceRoot()
	{
		return resourceRoot;
	}

	@Override
	public String getControllerTemplateFileName()
	{
		return controllerTemplateFileName;
	}

	@Override
	public String getEmberModelTemplateFileName()
	{
		return emberModelTemplateFileName;
	}

	@Override
	public String getControllerClassPostfix()
	{
		return controllerClassPostfix;
	}
	
	@Override
	public MustacheFactory getMustacheFactory()
	{
		return mustacheFactory;
	}

	@Override
	public Mustache getControllerMustacheCompiler()
	{
		if(this.controllerTemplateCompiler == null)
		{
			this.controllerTemplateCompiler = this.getMustacheFactory().compile(this.getControllerTemplateFileName());
		}
		
		return this.controllerTemplateCompiler;
	}

	@Override
	public Mustache getEmberModelMustacheCompiler()
	{
		if(this.emberModelTemplateCompiler == null)
		{
			this.emberModelTemplateCompiler = this.getMustacheFactory().compile(this.getEmberModelTemplateFileName());
		}
		
		return this.emberModelTemplateCompiler;
	}

	@Override
	public String getPackageName()
	{
		return packageName;
	}

	/**
	 * Sets the logger to be used during the wiring process. If <code>aLogger == null</code> , 
	 * the silent logger will be used.
	 */
	@Override
	public void setLogger(LoggerI aLogger)
	{
		if(aLogger == null)
		{
			this.logger = BasicLoggers.getSilentLogger();
		}
		else
		{
			this.logger = aLogger;
		}
	}

	@Override
	public WiringConfigurationI getDefaultConfiguration()
	{
		return DefaultWiringConfiguration.getInstance();
	}

	@Override
	public LoggerI getLogger()
	{
		return this.logger;
	}

	@Override
	public ModelMetaCollectingConfigurationI getMetaCollectingConfiguration()
	{
		return this.metaCollectingConfiguration;
	}

	@Override
	public ModelMetaRegistryI getMetaRegistry()
	{
		return this.metaRegistry;
	}

	@Override
	public void setMetaCollectingConfiguration(ModelMetaCollectingConfigurationI aMetaCollectingConfiguration)
	{
		this.metaCollectingConfiguration = aMetaCollectingConfiguration;
	}

	@Override
	public void setMetaRegistry(ModelMetaRegistryI aMetaRegistry)
	{
		this.metaRegistry = aMetaRegistry;
	}

}
