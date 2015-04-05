package org.smicon.rest.emberwiring.wiring;

import org.smicon.rest.emberwiring.controller.ControllerWiringConfigurationI;
import org.smicon.rest.emberwiring.embermodel.EmberModelWiringConfigurationI;
import org.smicon.rest.emberwiring.general.ConfigurationI;
import org.smicon.rest.emberwiring.metas.ModelMetaRegistryI;

public interface WiringConfigurationI
extends
ConfigurationI
{

	String getTemplateRoot();

	ControllerWiringConfigurationI getControllerWiringConfiguration();
	
	EmberModelWiringConfigurationI getEmberModelWiringConfiguration();
	
	ModelMetaRegistryI getMetaRegistry();
	
}
