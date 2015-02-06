package org.smicon.rest.data.wiring;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.smicon.rest.EmberWiringsMojo;
import org.smicon.rest.BasicLoggers;
import org.smicon.rest.data.general.ConfigurationI;
import org.smicon.rest.data.modelmeta.configuration.ModelMetaCollectingConfigurationI;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.data.wiring.controller.ControllerCreationConfigurationI;
import org.smicon.rest.data.wiring.embermodel.EmberModelCreationConfigurationI;
import org.smicon.rest.functionality.modelmeta.staticfactory.ModelMetas;
import org.smicon.rest.instances.logging.LoggerI;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public interface WiringConfigurationI
extends
ConfigurationI
{

	public static final File default_controller_output_directory = new File("src/main/java");
	public static final File default_ember_model_output_directory = new File("src/main/webapp/js/models");
	public static final String default_resource_root = EmberWiringsMojo.class.getPackage().getName().replace('.', '/');
	public static final String default_controller_template_file_name = "ControllerTemplate.mustache";
	public static final String default_ember_model_template_file_name = "EmberModel.mustache";
	public static final String default_controller_class_postfix = "Controller";
	public static final MustacheFactory default_mustache_factory = new DefaultMustacheFactory(default_resource_root);
	public static final String default_package_name = "default";
	public static final LoggerI default_logger = BasicLoggers.getDefaultLogger();
	public static final ModelMetaCollectingConfigurationI default_model_meta_collecting_configuration = ModelMetas.getDefaultConfiguration();
	public static final ModelMetaRegistryI default_model_meta_registry = ModelMetas.newRegistry_noException(default_model_meta_collecting_configuration);

	ControllerCreationConfigurationI getControllerWiringConfiguration();
	
	EmberModelCreationConfigurationI getEmberModelWiringConfiguration();
	
	ModelMetaCollectingConfigurationI getMetaCollectingConfiguration();
	
	ModelMetaRegistryI getMetaRegistry();
	
}
