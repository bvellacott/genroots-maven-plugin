package org.smicon.rest;

import org.smicon.rest.data.wiring.DefaultWiringConfiguration;
import org.smicon.rest.data.wiring.MutableWiringConfiguration;
import org.smicon.rest.data.wiring.MutableWiringConfigurationI;
import org.smicon.rest.data.wiring.WiringConfigurationI;
import org.smicon.rest.functionality.wiring.WiringFunctions;

public class EmberWirings
{
	
	public static WiringConfigurationI getDefaultConfiguration()
	{
		return DefaultWiringConfiguration.getInstance();
	}

	public static MutableWiringConfigurationI newConfiguration()
	{
		return new MutableWiringConfiguration();
	}
	
	public static void wire(WiringConfigurationI aConfiguration) throws Exception
	{
		WiringFunctions.wire(aConfiguration);
	}
}
