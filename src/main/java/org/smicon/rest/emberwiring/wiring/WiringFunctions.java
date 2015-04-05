package org.smicon.rest.emberwiring.wiring;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

import javax.persistence.Entity;

import org.smicon.rest.emberwiring.controller.ControllerTemplateDatas;
import org.smicon.rest.emberwiring.controller.ControllerWiringConfigurationI;
import org.smicon.rest.emberwiring.embermodel.EmberModelTemplateDatas;
import org.smicon.rest.emberwiring.embermodel.EmberModelWiringConfigurationI;
import org.smicon.rest.emberwiring.metas.ModelMetaI;

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
			generateRoutes(aConfig);
			aConfig.getLogger().info("Finished generating routes");
		}
	}

	public static void generateRoutes(WiringConfigurationI aConfig) throws Exception
	{
		StringBuilder logBuilder = new StringBuilder();

		for (Class<?> m : aConfig.getMetaRegistry().getModels())
		{
			generateController(aConfig.getMetaRegistry().getModelMeta(m), logBuilder, aConfig.getControllerWiringConfiguration());
			generateEmberModel(aConfig.getMetaRegistry().getModelMeta(m), logBuilder, aConfig.getEmberModelWiringConfiguration());
		}
		aConfig.getLogger().info(logBuilder.toString());
	}

	public static void generateController(ModelMetaI aModelMeta, StringBuilder aLogBuilder, ControllerWiringConfigurationI aConfig) throws Exception
	{
		Object templateData = ControllerTemplateDatas.createControllerData(aModelMeta, aConfig);
		
		String controllerPackage = aModelMeta.getModelClass().getPackage().getName();

		File routeClassFile = new File(aConfig.getOutputDirectory().getAbsolutePath() + "/" + controllerPackage.replace('.', '/') + "/"
			+ aModelMeta.getModelClass().getSimpleName() + aConfig.getClassPostfix() + ".java");
		aConfig.getCompiler().execute(new FileWriter(routeClassFile), templateData).flush();
		aLogBuilder.append("\tCreated route class file: " + routeClassFile + "\n");
	}

	public static void generateEmberModel(ModelMetaI aModelMeta, StringBuilder aLogBuilder, EmberModelWiringConfigurationI aConfig) throws Exception
	{
		Object populator = EmberModelTemplateDatas.createEmberModelTemplateData(aModelMeta, aConfig.getTypeConfiguration());

		File emberModelDir = new File(aConfig.getOutputDirectory().getAbsolutePath() + "/");
		if (!emberModelDir.exists()) emberModelDir.mkdirs();

		File emberModelFile = new File(aConfig.getOutputDirectory().getAbsolutePath() + "/" + aModelMeta.getModelClass().getSimpleName() + ".js");
		aConfig.getCompiler().execute(new FileWriter(emberModelFile), populator).flush();
		aLogBuilder.append("\tCreated ember model file: " + emberModelFile + "\n");
	}
}
