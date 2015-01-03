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
    		Model ma = m.getAnnotation(Model.class);
    		IdClass idCls = m.getAnnotation(IdClass.class);
    		
    		RouteTemplate routeInstance = new RouteTemplate();
    		routeInstance.setClassName(m.getSimpleName());
    		routeInstance.addImport(m);
    		routeInstance.setRouteClassName(m.getSimpleName() + this.routeClassPostfix);
    		routeInstance.setPackageName(m.getPackage().getName());
    		routeInstance.setPlural(ma.plural().toLowerCase());
    		routeInstance.setPersistenceUnitName(ma.persistenceUnitName());
    		
    		boolean isCompositeId = false;
    		Class<?> idType = null;
    		
    		if(idCls == null)
    		{
        		Id id;
        		EmbeddedId embeddedId;
        		Field[] fieldArray = m.getDeclaredFields();
        		for(Field field : fieldArray)
        		{
        			id = field.getAnnotation(Id.class);
        			if(id == null)
        			{
        				embeddedId = field.getAnnotation(EmbeddedId.class);
        				if(embeddedId != null)
            			{
            				idType = this.wrapIfPrimitive(field.getType());
            				isCompositeId = true;
            				this.generateSimpleIdSetter(m, idType, field, routeInstance);
            				break;
            			}
        			}
        			else
        			{
        				idType = this.wrapIfPrimitive(field.getType());
        				isCompositeId = false;
        				this.generateSimpleIdSetter(m, idType, field, routeInstance);
        				break;
        			}
        		}
        		
        		if(idType == null)
        		{
        			Method[] methodArray = m.getDeclaredMethods();
            		for(Method method : methodArray)
            		{
            			id = method.getAnnotation(Id.class);
            			if(id == null)
            			{
            				embeddedId = method.getAnnotation(EmbeddedId.class);
            				if(embeddedId != null)
                			{
                				idType = this.wrapIfPrimitive(method.getReturnType());
                				isCompositeId = true;
                				this.generateSimpleIdSetter(m, idType, method, routeInstance);
                				break;
                			}
            			}
            			else
            			{
            				idType = this.wrapIfPrimitive(method.getReturnType());
            				isCompositeId = false;
            				this.generateSimpleIdSetter(m, idType, method, routeInstance);
            				break;
            			}
            		}
        		}
    		}
    		else
    		{
    			idType = idCls.value();
				isCompositeId = true;
				this.generateCompositeIdSetter(m, idType, routeInstance);
    		}
    		
    		if(idType == null)
    		{
    			getLog().error("No id has been declared for the class: " + m.getName() + "\nSkipping the class.");
    			continue;
     		}
    		
   			routeInstance.setIdClassName(idType.getSimpleName());
    		
    		if(isCompositeId)
    		{
        		routeInstance.setIdPathExtension("id");
        		routeInstance.setPathParamDeclaration("");
    		}
    		else
    		{
        		routeInstance.setIdPathExtension("{id}");
        		routeInstance.setPathParamDeclaration("@PathParam(\"id\")");
    		}
    		
    		File routeClassFile = new File(this.outputDir.getAbsolutePath() + "/" + routeInstance.getPackageName().replace('.', '/') + "/" + routeInstance.getRouteClassName() + ".java");
    		mustache.execute(new FileWriter(routeClassFile), routeInstance).flush();
    		logBuilder.append("\tCreated route class file: " + routeClassFile + "\n");
    	}
    	this.getLog().info(logBuilder.toString());
    }
    
    private void generateSimpleIdSetter(Class aModelType, Class aIdType, Field aField, RouteTemplate aTemplateInstance)
    {
    	aTemplateInstance.addImport(aIdType);
    	
    	StringBuilder sb = new StringBuilder("\tpublic void setModelId(");
    	sb.append(aModelType.getSimpleName()).append(" aModel, ").append(aIdType.getSimpleName()).append(" aId) {\n")
    	.append(this.generateSetterLine(aModelType, aIdType, aField, aTemplateInstance)).append("\t}");
    	
    	aTemplateInstance.setIdSetterCode(sb.toString());
    }
    
    private void generateSimpleIdSetter(Class aModelType, Class aIdType, Method aAccessor, RouteTemplate aTemplateInstance)
    {
    	StringBuilder sb = new StringBuilder("\tpublic void setModelId(");
    	sb.append(aModelType.getSimpleName()).append(" aModel, ").append(aIdType.getSimpleName()).append(" aId) {\n")
    	.append(this.generateSetterLine(aModelType, aIdType, aAccessor, aTemplateInstance)).append("\t}");
    	
    	aTemplateInstance.setIdSetterCode(sb.toString());
    }
    
    private void generateCompositeIdSetter(Class aModelType, Class aIdType, RouteTemplate aTemplateInstance) throws Exception
    {
    	aTemplateInstance.addImport(aIdType);
    	
    	StringBuilder sb = new StringBuilder("\tpublic void setModelId(");
    	sb.append(aModelType.getSimpleName()).append(" aModel, ").append(aIdType.getSimpleName()).append(" aId) {\n");
    	
    	Field[] fieldArray = aModelType.getDeclaredFields();
    	Id id;
    	for(Field f : fieldArray)
    	{
    		id = f.getAnnotation(Id.class);
    		
    		if(id != null)
    		{
    			sb.append(this.generateCompositeGetterAndSetterLine(aModelType, aIdType, f, aTemplateInstance));
    		}
    	}
    	
    	Method[] methodArray = aModelType.getDeclaredMethods();
    	for(Method m : methodArray)
    	{
    		id = m.getAnnotation(Id.class);
    		
    		if(id != null)
    		{
    			sb.append(this.generateCompositeGetterAndSetterLine(aModelType, aIdType, m, aTemplateInstance));
    		}
    	}
    	
    	sb.append("\t}");
    	
    	aTemplateInstance.setIdSetterCode(sb.toString());
    }
    
    private String generateCompositeGetterAndSetterLine(Class aModelType, Class aIdType, Method aMethod, RouteTemplate aTemplateInstance) throws Exception
    {
    	StringBuilder sb = new StringBuilder();
    	
    	String mn = aMethod.getName();
    	String n = mn.substring(2, 3).toLowerCase() + mn.substring(3); // field name
    	Class<?> expectedClass = aMethod.getReturnType();
    	
    	sb.append(this.generateCompositeGetterAndSetterLine(n, mn, expectedClass, aIdType, aTemplateInstance));
		sb.append(this.generateSetterLine(aModelType, aIdType, aMethod, aTemplateInstance, n));
		return sb.toString();
    }
    
    private String generateCompositeGetterAndSetterLine(Class aModelType, Class aIdType, Field aField, RouteTemplate aTemplateInstance) throws Exception
    {
    	StringBuilder sb = new StringBuilder();
    	
    	String n = aField.getName();
    	String mn = "get" + n.substring(0, 1).toUpperCase() + n.substring(1); // method name
    	Class expectedClass = aField.getType();
    	
    	sb.append(this.generateCompositeGetterAndSetterLine(n, mn, expectedClass, aIdType, aTemplateInstance));
		sb.append(this.generateSetterLine(aModelType, aIdType, aField, aTemplateInstance, n));
		return sb.toString();
    }
    
    private String generateCompositeGetterAndSetterLine(String n, String mn, Class expectedClass, Class aIdType, RouteTemplate aTemplateInstance) throws Exception
    {
    	expectedClass = this.wrapIfPrimitive(expectedClass);
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("\t\t").append(expectedClass.getSimpleName()).append(" ").append(n).append(" = aId.");
    	
    	aTemplateInstance.addImport(aIdType);
    	
    	Field idField = aIdType.getDeclaredField(n);
    	Class fieldType = null;
    	if(idField != null)
    	{
			fieldType = this.wrapIfPrimitive(idField.getType());
    	}
    	
    	if(idField == null || !fieldType.equals(expectedClass))
    	{
    		Method idAccessor = aIdType.getMethod(mn, null);
    		Class retType = this.wrapIfPrimitive(idAccessor.getReturnType());
    		if(idAccessor == null || !retType.equals(expectedClass))
    		{
    			throw new RuntimeException("No parameter or accessor exists in the composite id class: " + 
    					aIdType.getName() + " for a value by the name: " + n + " and type: " + expectedClass + "!");
    		}
    		sb.append(mn).append("();\n");
    	}
    	else
    	{
    		sb.append(n).append(";\n");
    	}

    	return sb.toString();
    }
    
    private String generateSetterLine(Class aModelType, Class aIdType, Field aField, RouteTemplate aTemplateInstance)
    {
    	aTemplateInstance.addImport(aIdType);
    	
    	StringBuilder sb = new StringBuilder("\t\taModel.").append(aField.getName()).append(" = aId;\n");
    	
    	return sb.toString();
    }
    
    private String generateSetterLine(Class aModelType, Class aIdType, Method aAccessor, RouteTemplate aTemplateInstance)
    {
    	aTemplateInstance.addImport(aIdType);
    	
    	StringBuilder sb = new StringBuilder("\t\taModel.").append(aAccessor.getName()).append("(aId);\n");
    	
    	return sb.toString();
    }
    
    private String generateSetterLine(Class aModelType, Class aIdType, Field aField, RouteTemplate aTemplateInstance, String varName)
    {
    	aTemplateInstance.addImport(aIdType);
    	
    	StringBuilder sb = new StringBuilder("\t\taModel.").append(aField.getName()).append(" = aId.").append(varName).append(";\n");
    	
    	return sb.toString();
    }
    
    private String generateSetterLine(Class aModelType, Class aIdType, Method aAccessor, RouteTemplate aTemplateInstance, String varName)
    {
    	aTemplateInstance.addImport(aIdType);
    	
    	StringBuilder sb = new StringBuilder("\t\taModel.").append(aAccessor.getName()).append("(aId.").append(varName).append(");\n");
    	
    	return sb.toString();
    }
    
    private Class wrapIfPrimitive(Class aCls)
    {
    	if(aCls.isPrimitive())
    	{
    		return Primitives.wrap(aCls);
    	}
    	return aCls;
    }
}