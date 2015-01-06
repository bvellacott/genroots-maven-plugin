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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.reflections.Reflections;

import papu.annotations.Model;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.primitives.Primitives;

/**
 * Goal which generates roots from EJB Entities according to a given template.
 *
 */
@Mojo( name = "process", defaultPhase = LifecyclePhase.PROCESS_CLASSES )
public class GenerateRoutes
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.sourceDirectory}", property = "outputDir", required = true )
    private File outputDir;
    
    @Parameter
    private String resourceRoot;
    private String defaultResourceRoot = GenerateRoutes.class.getPackage().getName().replace('.', '/'); 

    @Parameter( defaultValue = "RouteTemplate.mustache" )
    private String templateFile;
    
    @Parameter( defaultValue = "Route" )
    private String routeClassPostfix;
    
    private Mustache mustache;
    
    @Parameter( defaultValue = "${session}", required = true )
    private MavenSession session;
    
    @Parameter(property = "project.compileClasspathElements", required = true, readonly = true) 
    private List<String> classpath;
    
    private String packageName;
    private Reflections reflections;
    
    private void init() throws Exception
    {
		if (classpath != null) 
		{
			List<URL> urls = new ArrayList<URL>();
			for (String element : classpath) {
				urls.add(new File(element).toURI().toURL());
			}

			ClassLoader contextClassLoader = URLClassLoader.newInstance(urls
					.toArray(new URL[0]), Thread.currentThread()
					.getContextClassLoader());

			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}
        
    	if(session != null)
    	{
    		packageName = session.getCurrentProject().getGroupId();
    	}
    	else
    	{
        	getLog().info("The maven session object is null!!");
    		packageName = GenerateRoutes.class.getPackage().getName();
    	}
    	getLog().info("Using package: " + packageName);
    	getLog().info("Using output dir: " + outputDir);
    	
    	reflections = new Reflections(packageName);
    	
    	MustacheFactory mf;
    	if(this.resourceRoot == null)
    	{
    		this.resourceRoot = this.defaultResourceRoot;
    	}

    	getLog().info("Using classpath resource root: " + resourceRoot);
    	getLog().info("Using template file: " + templateFile);
    	getLog().info("Using route class postfix: " + routeClassPostfix);
        
		mf = new DefaultMustacheFactory(resourceRoot);
	    mustache = mf.compile(templateFile);
     }
    
    public static void main(String[] args) throws MojoExecutionException
    {
    	GenerateRoutes genRoots = new GenerateRoutes();
    	genRoots.outputDir = new File("src/main/java");
    	genRoots.templateFile = "RouteTemplate.mustache";
    	genRoots.routeClassPostfix = "Route";
    	genRoots.execute();
    }
	
    public void execute() throws MojoExecutionException
    {
    	try
    	{
        	init();
        	
        	StringBuilder logBuilder = new StringBuilder("Scanning for source files which are annotated with @" + Model.class.getName() + " and @" + Entity.class.getName() + "\n");
   		
    		Set<Class<?>> models = reflections.getTypesAnnotatedWith(Model.class);
    		logBuilder.append(models);
    		
    		Iterator<Class<?>> iterator = models.iterator();
    		
    		while(iterator.hasNext())
    		{
    			Class<?> modelClass = iterator.next();
    			
    			Entity entityAnnotation = modelClass.getAnnotation(Entity.class);
    			Model modelAnnotation = modelClass.getAnnotation(Model.class);
    			if(entityAnnotation != null && modelAnnotation != null)
    			{
    				logBuilder.append("\tFound class: " + modelClass.getCanonicalName() + "\n");
    			}
    			else
    			{
    				iterator.remove();
    			}
    		}
			this.getLog().info(logBuilder.toString());
			this.getLog().info("Found a total of " + models.size() + " matching class files");

     		if(models.size() > 0)
    		{
        		this.getLog().info("Generating routes for maching classes");
        		this.generateRoutes(models);
        		this.getLog().info("Finished generating routes");
    		}
    	}
    	catch(Exception e)
    	{
    		throw new MojoExecutionException("Failed to generate routes from source", e);
    	}
    }
    
    private void generateRoutes(Set<Class<?>> aClasses) throws Exception
    {
    	StringBuilder logBuilder = new StringBuilder();
    	
    	for(Class<?> m : aClasses)
    	{
    		RouteTemplate routeInstance = new RouteTemplate();
    		routeInstance.setModelClass(m);
    		
    		File routeClassFile = new File(this.outputDir.getAbsolutePath() + "/" + routeInstance.getPackageName().replace('.', '/') + "/" + routeInstance.getRouteClassName() + ".java");
    		mustache.execute(new FileWriter(routeClassFile), routeInstance).flush();
    		logBuilder.append("\tCreated route class file: " + routeClassFile + "\n");
    	}
    	this.getLog().info(logBuilder.toString());
    }
    
}