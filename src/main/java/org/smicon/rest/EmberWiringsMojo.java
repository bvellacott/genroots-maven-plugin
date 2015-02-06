package org.smicon.rest;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.smicon.rest.data.modelmeta.Metas;
import org.smicon.rest.data.wiring.MutableWiringConfigurationI;
import org.smicon.rest.data.wiring.WiringConfigurationI;
import org.smicon.rest.instances.logging.mavenplugin.MavenPluginLoggerWrapper;

/**
 * Goal which generates controllers and equivalent Ember.js models for EJB Entities 
 * according to a given template.
 *
 */
@Mojo(name = "process", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class EmberWiringsMojo
extends
AbstractMojo
{
	/**
	 * Location of the file.
	 */
	@Parameter(defaultValue = "${project.build.sourceDirectory}", property = "controllerOutputDir", required = true)
	private File controllerOutputDirectory;

	@Parameter(property = "emberModelOutputDir")
	private File emberModelOutputDirectory;

	@Parameter
	private String resourceRoot;

	@Parameter
	private String controllerTemplateFileName;

	@Parameter
	private String emberModelTemplateFileName;

	@Parameter
	private String controllerClassPostfix;

	@Parameter(defaultValue = "${session}", required = true)
	private MavenSession session;
	
	private WiringConfigurationI wiringConfiguration;

	@Parameter(property = "project.compileClasspathElements", required = true, readonly = true)
	private List<String> classpathElements;

	private String packageName;

	private void init() throws Exception
	{
		MutableWiringConfigurationI config = EmberWirings.newConfiguration();
		config.setLogger(new MavenPluginLoggerWrapper(this.getLog()));

		if (session != null)
		{
			packageName = session.getCurrentProject().getGroupId();
		}
		else
		{
			config.getLogger().info("The maven session object is null!!");
			packageName = EmberWiringsMojo.class.getPackage().getName();
		}
		
		config.setPackageName(packageName);
		
		if(classpathElements != null)
		{
			config.setCompiledClasspathElements(classpathElements);
		}
		
		if(controllerOutputDirectory != null)
		{
			config.setControllerOutputDirectory(controllerOutputDirectory);
		}

		if(emberModelOutputDirectory != null)
		{
			config.setEmberModelOutputDirectory(emberModelOutputDirectory);
		}

		if (this.resourceRoot != null)
		{
			config.setResourceRoot(this.resourceRoot);
		}

		if(controllerTemplateFileName != null)
		{
			config.setControllerTemplateFileName(controllerTemplateFileName);
		}
		
		if(emberModelTemplateFileName != null)
		{
			config.setEmberModelTemplateFileName(emberModelTemplateFileName);
		}
		
		if(controllerClassPostfix != null)
		{
			config.setControllerClassPostfix(controllerClassPostfix);
		}
		
		config.getLogger().info("Using package: " + config.getPackageName());
		config.getLogger().info("Using controller output dir: " + config.getControllerOutputDirectory());
		config.getLogger().info("Using ember model output dir: " + config.getControllerOutputDirectory());
		config.getLogger().info("Using classpath resource root: " + config.getResourceRoot());
		config.getLogger().info("Using controller template file: " + config.getControllerTemplateFileName());
		config.getLogger().info("Using ember model template file: " + config.getEmberModelTemplateFileName());
		config.getLogger().info("Using controller class postfix: " + config.getControllerClassPostfix());
		
		this.wiringConfiguration = config;
		Metas.init(this.wiringConfiguration);

	}

	@Override
	public void execute() throws MojoExecutionException
	{
		try
		{
			init();
			
			EmberWirings.wire(this.wiringConfiguration);
		}
		catch (Exception e)
		{
			throw new MojoExecutionException("Failed to generate controllers from source", e);
		}
	}

}