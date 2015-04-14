package org.smicon.rest.emberwiring.embermodel;

import java.io.File;

import org.smicon.rest.emberwiring.general.ConfigurationI;
import org.smicon.rest.emberwiring.metas.ModelMetaRegistryI;

import com.github.mustachejava.Mustache;

public interface EmberModelWiringConfigurationI
extends ConfigurationI
{
	
	public static final String default_postfix = "";
	public static final File default_output_directory = new File("src/main/webapp/js/models");
	public static final String default_template_file_name = "EmberModel.mustache";
	public static final EmberModelTypeConfigurationI default_type_configuration = EmberModelTemplateDatas.createTypeConfiguration();

	String getModelPostfix();
	
	String getTemplateFileName();
	
	File getOutputDirectory();
	
	ModelMetaRegistryI getModelMetaRegistry();
	
	EmberModelTypeConfigurationI getTypeConfiguration();

	Mustache getCompiler();

}
