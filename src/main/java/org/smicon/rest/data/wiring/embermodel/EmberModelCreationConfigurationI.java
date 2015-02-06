package org.smicon.rest.data.wiring.embermodel;

import java.io.File;

import org.smicon.rest.data.general.ConfigurationI;

import com.github.mustachejava.Mustache;

public interface EmberModelCreationConfigurationI
extends ConfigurationI
{
	
	File getOutputDirectory();
	
	String getResourceRoot();
	
	String getTemplateFileName();
	
	String getFileNamePostfix();
	
	Mustache getMustacheCompiler();

}
