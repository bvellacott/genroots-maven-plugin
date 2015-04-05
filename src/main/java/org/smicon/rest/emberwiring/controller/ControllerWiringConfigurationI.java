package org.smicon.rest.emberwiring.controller;

import java.io.File;

import org.smicon.rest.emberwiring.general.ConfigurationI;
import org.smicon.rest.emberwiring.metas.ModelMetaRegistryI;

import com.github.mustachejava.Mustache;

public interface ControllerWiringConfigurationI
extends 
ConfigurationI
{

	public static final String default_class_postfix = "Controller";
	public static final File default_output_directory = new File("src/main/java");
	public static final String default_template_file_name = "ControllerTemplate.mustache";
	public static final String default_model_variable_name = "aModel";
	public static final String default_url_delimiter = "::";

	String getClassPostfix();
	
	File getOutputDirectory();
	
	String getTemplateFileName();
	
	String getPackageName();
	
	String getModelVariableName();
	
	String getUrlIdDelimiter();
	
	ModelMetaRegistryI getModelMetaRegistry();
	
	Mustache getCompiler();
	
}
