package org.smicon.rest.emberwiring.general.Builders;

import org.smicon.rest.emberwiring.metas.ModelMetaCollectingConfigurationBuilder;
import org.smicon.rest.emberwiring.metas.ModelValidationDataBuilder;
import org.smicon.rest.emberwiring.wiring.WiringConfigurationBuilder;

public final class Builders
{

	public static final ModelMetaCollectingConfigurationBuilder ModelMetaCollectingConfiguration(String aPackageName)
	{
		return new ModelMetaCollectingConfigurationBuilder(aPackageName);
	}

	public static final ModelValidationDataBuilder ModelValidationData()
	{
		return new ModelValidationDataBuilder();
	}

	public static final WiringConfigurationBuilder WiringConfiguration(String aMustacheTemplateRoot, String aPackageName) throws Exception
	{
		return new WiringConfigurationBuilder(aMustacheTemplateRoot, aPackageName);
	}

}
