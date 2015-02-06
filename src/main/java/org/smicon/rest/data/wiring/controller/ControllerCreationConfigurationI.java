package org.smicon.rest.data.wiring.controller;

import java.io.File;

import org.smicon.rest.data.general.ConfigurationI;

import com.github.mustachejava.Mustache;

public interface ControllerCreationConfigurationI
extends 
ConfigurationI
{

	File getOutputDirectory();
	
	String getResourceRoot();
	
	String getTemplateFileName();
	
	String getFileNamePostfix();
	
	Mustache getMustacheCompiler();
	
}
