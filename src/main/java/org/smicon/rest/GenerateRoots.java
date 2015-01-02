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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import papu.annotations.Expose;

/**
 * Goal which generates roots from EJB Entities according to a given template.
 *
 */
@Mojo( name = "process", defaultPhase = LifecyclePhase.PROCESS_CLASSES )
public class GenerateRoots
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File projectDir;
    
    @Parameter( defaultValue = "${project.build.directory}", required = true )
    private File routeTemplate;

    @Parameter( defaultValue = "${session}", required = true )
    private MavenSession session;
    
    private ASTParser parser;
    
    public GenerateRoots()
    {
		parser = ASTParser.newParser(AST.JLS3);
		//parser.setSource("/*abc*/".toCharArray());

		Map options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6); 
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6); 
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		parser.setCompilerOptions(options);	
		
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
    }
    
    public static void main(String[] args) throws MojoExecutionException
    {
    	GenerateRoots genRoots = new GenerateRoots();
    	genRoots.projectDir = new File("src/main/java");
    	genRoots.routeTemplate = new File("test.template");
    	genRoots.execute();
    }
	
    public void execute() throws MojoExecutionException
    {
    	StringBuilder logBuilder = new StringBuilder();;
    	try
    	{
    		this.getLog().info("Scanning for source files annotated @" + Expose.class.getSimpleName());
    		List<File> files = this.getAnnotatedSourceFiles(projectDir, null);
    		logBuilder.setLength(0);
    		logBuilder.append("Finished scanning for annotated source files. Found " + files.size() + " files:\n");
    		for(File f : files)
    		{
    			logBuilder.append("\t");
    			logBuilder.append(f.getAbsolutePath());
    			logBuilder.append("\n");
    		}
    		this.getLog().info(logBuilder.toString());
    		
    		if(files.size() > 0)
    		{
        		this.getLog().info("Generating routes for annotated source files");
        		this.generateRoutes(files);
        		this.getLog().info("Finished generating routes for source files");
    		}
    	}
    	catch(Exception e)
    	{
    		throw new MojoExecutionException("Failed to generate routes from source", e);
    	}
    }
    
    private void generateRoutes(List<File> aFiles)
    {
    	StringBuilder logBuilder = new StringBuilder();
    	
    	logBuilder.append("Route generation hasn't been implemented!\n");
    	for(File f : aFiles)
    	{
    		logBuilder.append("\tNo route was generated for the annotated file: ");
    		logBuilder.append(f.getAbsolutePath());
    		logBuilder.append("\n");
    	}
    	this.getLog().info(logBuilder.toString());
    }

	private List<File> getAnnotatedSourceFiles(File aDir, List<File> aFileList) throws Exception
	{
		if(aFileList == null)
		{
			aFileList = new ArrayList<File>();
		}
		
		if(aDir == null)
		{
			throw new NullPointerException("The project file object is null!!");
		}
		if(!aDir.isDirectory())
		{
			throw new IllegalArgumentException("The project directory object doesn't point to a directory!");
		}
		
		File[] fileArray = aDir.listFiles();
		
		if(fileArray == null)
		{
			return aFileList;
		}
		
		for(File f : fileArray)
		{
			this.getLog().info("\tScanning: " + f.getAbsolutePath());
			if(f.isDirectory())
			{
				this.getAnnotatedSourceFiles(f, aFileList);
			}
			else if(f.getName().endsWith(".java"))
			{
				if(isFileAnnotated(f))
				{
					aFileList.add(f);
				}
			}
		}
		
		return aFileList;
	}
	
	private boolean isFileAnnotated(File aSourceFile) throws Exception
	{
		return this.isCompilationUnitAnnotated(this.parseSource(aSourceFile), aSourceFile);
	}
	
	private CompilationUnit parseSource(File aSourceFile) throws Exception
	{
		FileReader fr = new FileReader(aSourceFile);
		BufferedReader reader = new BufferedReader(fr); 
		
		ArrayList<String> lines = new ArrayList<String>(); 
		
		int charCount = 0;
		
		String line = null;
		while((line = reader.readLine()) != null)
		{
			lines.add(line);
			charCount += line.length();
		}
		
		char[] source = new char[charCount];
		
		int c = 0;
		for(int i = 0 ; i < lines.size(); i++)
		{
			line = lines.get(i);
			
			for(int j = 0; j < line.length(); j++, c++)
			{
				source[c] = line.charAt(j);
			}
		}
		
		parser.setSource(source);
		return (CompilationUnit) parser.createAST(null);
	}
	
	private boolean isCompilationUnitAnnotated(final CompilationUnit aCompilationUnit, final File aSourceFile)
	{
		Visitor visitor = new Visitor(aSourceFile);
		
		this.getLog().info("\tVisiting: " + aSourceFile);
		aCompilationUnit.accept(visitor);
		
		return visitor.isAnnotated;
	}
	
	private class Visitor extends ASTVisitor
	{
		public boolean isAnnotated = false;
		private TypeDeclaration parent;
		private File sourceFile;
		
		public Visitor(File aSourceFile)
		{
			sourceFile = aSourceFile;
		}
		
		public boolean visit(TypeDeclaration aType)
		{
			this.parent = aType;
			return true;
		}
		
		public boolean visit(MarkerAnnotation aAnnotation)
		{
			getLog().info(aAnnotation.getTypeName().toString());
			
			if(aAnnotation.getTypeName().toString().equals(Expose.class.getSimpleName()))
			{
				getLog().info("Annotation parent: " + ((TypeDeclaration)aAnnotation.getParent()).getName());
				getLog().info("Expected parent: " + parent.getName());
				
				if(!aAnnotation.getParent().equals(parent))
				{
					try 
					{
						throw new IllegalArgumentException("Annotations of inner classes is not supported! The annotaion is incorrectly placed in the compilation unit: " + sourceFile.getCanonicalPath());
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
						throw new IllegalArgumentException("Annotations of inner classes is not supported! The annotaion is incorrectly placed in the compilation unit!");
					}
				}
				
				isAnnotated = true;
				
				return false;
			}
			
			return true;
		}
		
	};
	
	private URL createUrl(File fe) {
	        URL url=fe.toURI().toURL();
	        URLClassLoader.getSystemClassLoader().
	        getLog().info("Added URL: '"+url.toString()+"'");
	        if(classPath.length()>0) { classPath+=File.pathSeparator; }
	        this.classPath+=fe.getPath();
	        return url;
	    }
	}
