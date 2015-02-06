package org.smicon.rest.functionality.wiring;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

import javax.persistence.Entity;

import org.smicon.rest.data.modelmeta.Metas;
import org.smicon.rest.data.mustachetemplate.controller.MutableControllerTemplateData;
import org.smicon.rest.data.mustachetemplate.embermodel.EmberModelPopulator;
import org.smicon.rest.data.wiring.WiringConfigurationI;

import papu.annotations.Model;

public final class WiringFunctions
{

	public static void wire(WiringConfigurationI aConfig) throws Exception
	{
		StringBuilder logBuilder = new StringBuilder("Scanning for source files which are annotated with @" + Model.class.getName() + " and @"
			+ Entity.class.getName() + "\n");

		Collection<Class<?>> models = aConfig.getMetaRegistry().getModels();

		aConfig.getLogger().info(logBuilder.toString());
		aConfig.getLogger().info("Found a total of " + models.size() + " matching class files");

		if (models.size() > 0)
		{
			aConfig.getLogger().info("Generating routes for matching classes");
			generateRoutes(models, aConfig);
			aConfig.getLogger().info("Finished generating routes");
		}
	}

	private static void generateRoutes(Collection<Class<?>> aClasses, WiringConfigurationI aConfig) throws Exception
	{
		StringBuilder logBuilder = new StringBuilder();

		for (Class<?> m : aClasses)
		{
			generateController(m, logBuilder, aConfig);
			generateEmberModel(m, logBuilder, aConfig);
		}
		aConfig.getLogger().info(logBuilder.toString());
	}

	private static void generateController(Class<?> aModelClass, StringBuilder aLogBuilder, WiringConfigurationI aConfig) throws Exception
	{
		MutableControllerTemplateData populator = new MutableControllerTemplateData(aModelClass);
		populator.setControllerClassPostfix(aConfig.getControllerClassPostfix());

		File routeClassFile = new File(aConfig.getControllerOutputDirectory().getAbsolutePath() + "/" + populator.getPackageName().replace('.', '/') + "/"
			+ populator.getClassName() + populator.getControllerClassPostfix() + ".java");
		aConfig.getControllerMustacheCompiler().execute(new FileWriter(routeClassFile), populator).flush();
		aLogBuilder.append("\tCreated route class file: " + routeClassFile + "\n");
	}

	private static void generateEmberModel(Class<?> aModelClass, StringBuilder aLogBuilder, WiringConfigurationI aConfig) throws Exception
	{
		EmberModelPopulator populator = new EmberModelPopulator(aModelClass);

		File emberModelDir = new File(aConfig.getEmberModelOutputDirectory().getAbsolutePath() + "/");
		if (!emberModelDir.exists()) emberModelDir.mkdirs();

		File emberModelFile = new File(aConfig.getEmberModelOutputDirectory().getAbsolutePath() + "/" + populator.getClassName() + ".js");
		aConfig.getEmberModelMustacheCompiler().execute(new FileWriter(emberModelFile), populator).flush();
		aLogBuilder.append("\tCreated ember model file: " + emberModelFile + "\n");
	}
}
