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
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.reflections.Reflections;
import org.smicon.rest.metas.Metas;
import org.smicon.rest.populators.RouteTemplatePopulator;
import org.smicon.rest.populators.embermodel.EmberModelPopulator;

import papu.annotations.Model;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Goal which generates roots from EJB Entities according to a given template.
 *
 */
@Mojo(name = "process", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class GenerateRoutes
extends
AbstractMojo
{
	/**
	 * Location of the file.
	 */
	@Parameter(defaultValue = "${project.build.sourceDirectory}", property = "routeOutputDir", required = true)
	private File routeOutputDir;

	@Parameter(defaultValue = "src/main/webapp/js/models", property = "emberModelOutputDir", required = true)
	private File emberModelOutputDir;

	@Parameter
	private String resourceRoot;
	private String defaultResourceRoot = GenerateRoutes.class.getPackage().getName().replace('.', '/');

	@Parameter(defaultValue = "RouteTemplate.mustache")
	private String routeTemplateFile;

	@Parameter(defaultValue = "EmberModel.mustache")
	private String emberModelTemplateFile;

	@Parameter(defaultValue = "Route")
	private String routeClassPostfix;

	private Mustache routeMustache;
	private Mustache emberModelMustache;

	@Parameter(defaultValue = "${session}", required = true)
	private MavenSession session;

	@Parameter(property = "project.compileClasspathElements", required = true, readonly = true)
	private List<String> classpath;

	private String packageName;

	private void init() throws Exception
	{
		if (classpath != null)
		{
			List<URL> urls = new ArrayList<URL>();
			for (String element : classpath)
			{
				urls.add(new File(element).toURI().toURL());
			}

			ClassLoader contextClassLoader = URLClassLoader.newInstance(urls.toArray(new URL[0]), Thread
			.currentThread().getContextClassLoader());

			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}

		if (session != null)
		{
			packageName = session.getCurrentProject().getGroupId();
		}
		else
		{
			getLog().info("The maven session object is null!!");
			packageName = GenerateRoutes.class.getPackage().getName();
		}
		getLog().info("Using package: " + packageName);
		getLog().info("Using output dir: " + routeOutputDir);

		Metas.init(packageName);

		MustacheFactory mf;
		if (this.resourceRoot == null)
		{
			this.resourceRoot = this.defaultResourceRoot;
		}

		getLog().info("Using classpath resource root: " + resourceRoot);
		getLog().info("Using route template file: " + routeTemplateFile);
		getLog().info("Using ember model template file: " + emberModelTemplateFile);
		getLog().info("Using route class postfix: " + routeClassPostfix);

		mf = new DefaultMustacheFactory(resourceRoot);
		routeMustache = mf.compile(routeTemplateFile);
		emberModelMustache = mf.compile(emberModelTemplateFile);
	}

	public static void main(String[] args) throws MojoExecutionException
	{
		GenerateRoutes genRoots = new GenerateRoutes();
		genRoots.routeOutputDir = new File("src/main/java");
		genRoots.emberModelOutputDir = new File("src/main/webapp/js/models");
		genRoots.routeTemplateFile = "RouteTemplate.mustache";
		genRoots.emberModelTemplateFile = "EmberModel.mustache";
		genRoots.routeClassPostfix = "Route";
		genRoots.execute();
	}

	public void execute() throws MojoExecutionException
	{
		try
		{
			init();

			StringBuilder logBuilder = new StringBuilder("Scanning for source files which are annotated with @"
			+ Model.class.getName() + " and @" + Entity.class.getName() + "\n");

			Collection<Class<?>> models = Metas.getModelClasses();

			this.getLog().info(logBuilder.toString());
			this.getLog().info("Found a total of " + models.size() + " matching class files");

			if (models.size() > 0)
			{
				this.getLog().info("Generating routes for maching classes");
				this.generateRoutes(models);
				this.getLog().info("Finished generating routes");
			}
		}
		catch (Exception e)
		{
			throw new MojoExecutionException("Failed to generate routes from source", e);
		}
	}

	private void generateRoutes(Collection<Class<?>> aClasses) throws Exception
	{
		StringBuilder logBuilder = new StringBuilder();

		for (Class<?> m : aClasses)
		{
			this.generateRoute(m, logBuilder);
			this.generateEmberModel(m, logBuilder);
		}
		this.getLog().info(logBuilder.toString());
	}

	private void generateRoute(Class<?> aModelClass, StringBuilder aLogBuilder) throws Exception
	{
		RouteTemplatePopulator populator = new RouteTemplatePopulator(aModelClass);

		File routeClassFile = new File(this.routeOutputDir.getAbsolutePath() + "/"
		+ populator.getPackageName().replace('.', '/') + "/" + populator.getRouteClassName() + ".java");
		routeMustache.execute(new FileWriter(routeClassFile), populator).flush();
		aLogBuilder.append("\tCreated route class file: " + routeClassFile + "\n");
	}

	private void generateEmberModel(Class<?> aModelClass, StringBuilder aLogBuilder) throws Exception
	{
		EmberModelPopulator populator = new EmberModelPopulator(aModelClass);

		File emberModelDir = new File(this.emberModelOutputDir.getAbsolutePath() + "/");
		if(!emberModelDir.exists()) 
			emberModelDir.mkdirs();
		
		File emberModelFile = new File(this.emberModelOutputDir.getAbsolutePath() + "/" + populator.getClassName() + ".js");
		emberModelMustache.execute(new FileWriter(emberModelFile), populator).flush();
		aLogBuilder.append("\tCreated ember model file: " + emberModelFile + "\n");
	}
}