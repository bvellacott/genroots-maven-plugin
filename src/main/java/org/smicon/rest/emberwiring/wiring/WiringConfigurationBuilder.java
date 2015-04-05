package org.smicon.rest.emberwiring.wiring;

import java.io.File;
import java.util.List;

import org.smicon.rest.emberwiring.controller.ControllerWiringConfigurationI;
import org.smicon.rest.emberwiring.embermodel.EmberModelTypeConfigurationI;
import org.smicon.rest.emberwiring.embermodel.EmberModelWiringConfigurationI;
import org.smicon.rest.emberwiring.general.Builders.BuilderI;
import org.smicon.rest.emberwiring.general.Builders.Builders;
import org.smicon.rest.emberwiring.general.factories.Factories;
import org.smicon.rest.emberwiring.general.factories.KeyedFactoryI;
import org.smicon.rest.emberwiring.general.pools.MustacheFactoryPool;
import org.smicon.rest.emberwiring.general.pools.Pools;
import org.smicon.rest.emberwiring.metas.ModelFunctions;
import org.smicon.rest.emberwiring.metas.ModelMetaCollectingConfigurationI;
import org.smicon.rest.emberwiring.metas.ModelMetaI;
import org.smicon.rest.emberwiring.metas.ModelMetaRegistryI;
import org.smicon.rest.emberwiring.metas.ModelValidationDataI;
import org.smicon.rest.logging.LoggerI;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class WiringConfigurationBuilder
implements
BuilderI<WiringConfigurationI>
{
	
	LoggerI logger = WiringConfigurationI.default_logger;
	String modelVariableName = ControllerWiringConfigurationI.default_model_variable_name;
	String urlDelimiter = ControllerWiringConfigurationI.default_url_delimiter;
	File controllerOutputDirectory = ControllerWiringConfigurationI.default_output_directory;
	File emberModelOutputDirectory = EmberModelWiringConfigurationI.default_output_directory;
	String controllerTemplateFileName = ControllerWiringConfigurationI.default_template_file_name;
	String emberModelTemplateFileName = EmberModelWiringConfigurationI.default_template_file_name;
	String controllerClassPostfix = ControllerWiringConfigurationI.default_class_postfix;
	String emberModelClassPostfix = EmberModelWiringConfigurationI.default_postfix;
	EmberModelTypeConfigurationI emberModelTypeConfiguration = EmberModelWiringConfigurationI.default_type_configuration;
	KeyedFactoryI<Class<?>, ModelMetaI, ModelValidationDataI> metaFactory = Factories.modelMetaFactory;
	MustacheFactory mustacheFactory;
	String resourceRoot = null;
	String packageName = null;
	List<String> classpathElements = null;
	
	public WiringConfigurationBuilder withLogger(LoggerI aLogger) { logger = aLogger; return this; }
	public WiringConfigurationBuilder withModelVariableName(String aModelVariableName) { modelVariableName = aModelVariableName; return this; }
	public WiringConfigurationBuilder withUrlDelimiter(String aDelimiter) { urlDelimiter = aDelimiter; return this; }
	public WiringConfigurationBuilder withControllerOutputDirectory(File aDir) { controllerOutputDirectory = aDir; return this; }
	public WiringConfigurationBuilder withEmberModelOutputDirectory(File aDir) { emberModelOutputDirectory = aDir; return this; }
	public WiringConfigurationBuilder withControllerTemplateFileName(String aControllerTemplateFileName) { controllerTemplateFileName = aControllerTemplateFileName; return this; }
	public WiringConfigurationBuilder withEmberModelTemplateFileName(String aEmberModelTemplateFileName) { emberModelTemplateFileName = aEmberModelTemplateFileName; return this; }
	public WiringConfigurationBuilder withControllerClassPostfix(String aPostfix) { controllerClassPostfix = aPostfix; return this; }
	public WiringConfigurationBuilder withEmberModelClassPostfix(String aPostfix) { emberModelClassPostfix = aPostfix; return this; }
	public WiringConfigurationBuilder withEmberModelTypeConfiguration(EmberModelTypeConfigurationI aConfiguration) { emberModelTypeConfiguration = aConfiguration; return this; }
	public WiringConfigurationBuilder withMetaFactory(KeyedFactoryI<Class<?>, ModelMetaI, ModelValidationDataI> aFactory) { metaFactory = aFactory; return this; }
	public WiringConfigurationBuilder withResourceRoot(String aRoot) { resourceRoot = aRoot; return this; }
	public WiringConfigurationBuilder withPackageName(String aPackageName) { packageName = aPackageName; return this; }
	public WiringConfigurationBuilder withClassPathElements(List<String> aClasspathElements) { classpathElements = aClasspathElements; return this; }

	public WiringConfigurationBuilder(String aMustacheTemplateRoot, String aPackageName) throws Exception
	{
		resourceRoot = aMustacheTemplateRoot;
		packageName = aPackageName;
	}
	
	public WiringConfigurationI build() throws Exception
	{
		WiringConfigurationInstance instance = new WiringConfigurationInstance();
		
		ModelMetaCollectingConfigurationI metaCollectingConfiguration = Builders.ModelMetaCollectingConfiguration(packageName).
				withValidationData(Builders.ModelValidationData().build()).
				withClasspathElements(classpathElements).
				build();
		
		instance.metaRegistry = ModelFunctions.populateRegistry(metaFactory, metaCollectingConfiguration);
		instance.logger = logger;
		
		if(resourceRoot.startsWith("/")) resourceRoot = resourceRoot.replaceFirst("/", "");
		mustacheFactory = Pools.mustacheFactoryPool.borrowObject(resourceRoot);
		
		ControllerWiringConfigurationInstance controllerInstance = new ControllerWiringConfigurationInstance(); 
		controllerInstance.logger = logger;
		controllerInstance.packageName = packageName;
		controllerInstance.templateFileName = controllerTemplateFileName;
		controllerInstance.ouputDir = controllerOutputDirectory;
		controllerInstance.controllerPostfix = controllerClassPostfix;
		controllerInstance.modelVariableName = modelVariableName;
		controllerInstance.urlDelimiter = urlDelimiter;
		controllerInstance.metaRegistry = instance.metaRegistry;
		controllerInstance.compiler = mustacheFactory.compile(controllerTemplateFileName);
		
		EmberModelWiringConfigurationInstance emberModelInstance = new EmberModelWiringConfigurationInstance();
		emberModelInstance.logger = logger;
		emberModelInstance.postfix = controllerClassPostfix;
		emberModelInstance.outputDir = emberModelOutputDirectory;
		emberModelInstance.templateFileName = emberModelTemplateFileName;
		emberModelInstance.typeConfiguration = emberModelTypeConfiguration;
		emberModelInstance.metaRegistry = instance.metaRegistry;
		emberModelInstance.compiler = mustacheFactory.compile(emberModelTemplateFileName);
		
		instance.controllerWiringConfiguration = controllerInstance;
		instance.emberModelWiringConfiguration = emberModelInstance;
		
		return instance;
	}
	
	class WiringConfigurationInstance
	implements
	WiringConfigurationI
	{
		LoggerI logger;
		String resourceRoot;
		ModelMetaRegistryI metaRegistry;
		ControllerWiringConfigurationI controllerWiringConfiguration;
		EmberModelWiringConfigurationI emberModelWiringConfiguration;
		
		@Override public LoggerI getLogger() { return logger; }

		@Override public String getTemplateRoot() { return resourceRoot; }

		@Override public ModelMetaRegistryI getMetaRegistry() { return metaRegistry; }

		@Override public ControllerWiringConfigurationI getControllerWiringConfiguration() { return controllerWiringConfiguration; }

		@Override public EmberModelWiringConfigurationI getEmberModelWiringConfiguration() { return emberModelWiringConfiguration; }

	}
	
	class ControllerWiringConfigurationInstance
	implements
	ControllerWiringConfigurationI
	{
		LoggerI logger;
		String packageName;
		String templateFileName;
		File ouputDir;
		String controllerPostfix;
		String modelVariableName;
		String urlDelimiter;
		ModelMetaRegistryI metaRegistry;
		Mustache compiler;

		@Override public LoggerI getLogger() { return logger; }

		@Override public String getPackageName() { return packageName; }

		@Override public String getTemplateFileName() { return templateFileName; }
		
		@Override public File getOutputDirectory() { return ouputDir; }

		@Override public String getClassPostfix() { return controllerPostfix; }

		@Override public String getModelVariableName() { return modelVariableName; }

		@Override public String getUrlIdDelimiter() { return urlDelimiter; }

		@Override public ModelMetaRegistryI getModelMetaRegistry() { return metaRegistry; }

		@Override public Mustache getCompiler() { return compiler; }

	}
	
	class EmberModelWiringConfigurationInstance
	implements
	EmberModelWiringConfigurationI
	{
		LoggerI logger;
		String templateFileName;
		String postfix;
		File outputDir;
		EmberModelTypeConfigurationI typeConfiguration;
		ModelMetaRegistryI metaRegistry;
		Mustache compiler;
		
		@Override public LoggerI getLogger() { return logger; }
		
		@Override public String getTemplateFileName() { return templateFileName; }
		
		@Override public File getOutputDirectory() { return outputDir; }

		@Override public ModelMetaRegistryI getModelMetaRegistry() { return metaRegistry; }
		
		@Override public EmberModelTypeConfigurationI getTypeConfiguration() { return typeConfiguration; }

		@Override public String getModelPostfix() { return postfix; }

		@Override public Mustache getCompiler() { return compiler; }

	}
}
