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
import org.smicon.rest.emberwiring.controller.ControllerWiringConfigurationI;
import org.smicon.rest.emberwiring.embermodel.EmberModelWiringConfigurationI;
import org.smicon.rest.emberwiring.general.Builders.Builders;
import org.smicon.rest.emberwiring.wiring.WiringConfigurationBuilder;
import org.smicon.rest.emberwiring.wiring.WiringConfigurationI;
import org.smicon.rest.emberwiring.wiring.WiringFunctions;
import org.smicon.rest.logging.mavenplugin.MavenPluginLoggerWrapper;

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
	
	@Parameter(property = "project.compileClasspathElements", required = true, readonly = true)
	private List<String> classpathElements;

	private String packageName;
	
	WiringConfigurationI configuration;

	private void init() throws Exception
	{
		MavenPluginLoggerWrapper logger = new MavenPluginLoggerWrapper(this.getLog());
		
		if (session != null)
		{
			packageName = session.getCurrentProject().getGroupId();
		}
		else
		{
			logger.info("The maven session object is null!!");
			packageName = EmberWiringsMojo.class.getPackage().getName();
		}
		if(resourceRoot == null)
		{
			resourceRoot = "/" + packageName.replace('.', '/');
		}
		WiringConfigurationBuilder configBuilder = Builders.WiringConfiguration(resourceRoot, packageName).
			withLogger(logger);
		
		if(classpathElements != null)
			configBuilder.withClassPathElements(classpathElements);
		
		if(controllerOutputDirectory != null)
			configBuilder.withControllerOutputDirectory(controllerOutputDirectory);

		if(emberModelOutputDirectory != null)
			configBuilder.withEmberModelOutputDirectory(emberModelOutputDirectory);

		if(controllerTemplateFileName != null)
			configBuilder.withControllerTemplateFileName(controllerTemplateFileName);
		
		if(emberModelTemplateFileName != null)
			configBuilder.withEmberModelTemplateFileName(emberModelTemplateFileName);
		
		if(controllerClassPostfix != null)
			configBuilder.withControllerClassPostfix(controllerClassPostfix);
		
		configuration = configBuilder.build();
		
		ControllerWiringConfigurationI controllerConfig = configuration.getControllerWiringConfiguration();
		EmberModelWiringConfigurationI emberModelConfig = configuration.getEmberModelWiringConfiguration();
		
		logger.info("Using package: " + packageName);
		logger.info("Using controller output dir: " + controllerConfig.getOutputDirectory());
		logger.info("Using ember model output dir: " + emberModelConfig.getOutputDirectory());
		logger.info("Using classpath resource root: " + resourceRoot);
		logger.info("Using controller template file: " + controllerConfig.getTemplateFileName());
		logger.info("Using ember model template file: " + emberModelConfig.getTemplateFileName());
		logger.info("Using controller class postfix: " + controllerConfig.getClassPostfix());
	}

	@Override
	public void execute() throws MojoExecutionException
	{
		try {
			init();
			WiringFunctions.wire(configuration);
		}catch (Exception e) {
			throw new MojoExecutionException("Failed to generate controllers from source", e);
		}
	}

}