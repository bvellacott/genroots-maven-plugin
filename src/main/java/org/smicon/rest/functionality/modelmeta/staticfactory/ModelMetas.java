package org.smicon.rest.functionality.modelmeta.staticfactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.smicon.rest.data.metas.ModelMeta;
import org.smicon.rest.data.modelmeta.configuration.DefaultModelMetaCollectionConfiguration;
import org.smicon.rest.data.modelmeta.configuration.ModelMetaCollectingConfigurationI;
import org.smicon.rest.data.modelmeta.configuration.MutableModelMetaCollectingConfiguration;
import org.smicon.rest.data.modelmeta.configuration.MutableModelMetaCollectingConfigurationI;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistry;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;

public final class ModelMetas
{

	public static ModelMetaCollectingConfigurationI getDefaultConfiguration()
	{
		return DefaultModelMetaCollectionConfiguration.getInstance();
	}

	public static MutableModelMetaCollectingConfigurationI newConfiguration()
	{
		return new MutableModelMetaCollectingConfiguration();
	}

	public static ModelMetaRegistryI newRegistry()
	{
		return new ModelMetaRegistry();
	}

	public static ModelMetaRegistryI newRegistry(ModelMetaCollectingConfigurationI aConfiguration) throws Exception
	{
		ModelMetaRegistryI registry = newRegistry();
		populateRegistry(registry, aConfiguration);
		return registry;
	}

	public static ModelMetaRegistryI newRegistry_noException(ModelMetaCollectingConfigurationI aConfiguration)
	{
		try
		{
			return newRegistry(aConfiguration);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	public static void populateRegistry(ModelMetaRegistryI aRegistry, ModelMetaCollectingConfigurationI aConfiguration) throws Exception
	{
		if (aConfiguration.getCompiledClasspathElements() != null)
		{
			List<URL> urls = new ArrayList<URL>();
			for (String element : aConfiguration.getCompiledClasspathElements())
			{
				urls.add(new File(element).toURI().toURL());
			}

			ClassLoader contextClassLoader = URLClassLoader.newInstance(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());

			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}

		Set<Class<?>> types = aConfiguration.getReflections().getTypesAnnotatedWith(Entity.class);

		aRegistry.registerModels(types);
	}

	public static ModelMeta newMeta() throws Exception
	{
		return new ModelMeta();
	}

	public static void initMeta(ModelMeta aMeta)
	{

	}

}
