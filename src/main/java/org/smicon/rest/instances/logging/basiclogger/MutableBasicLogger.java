package org.smicon.rest.instances.logging.basiclogger;

import org.smicon.rest.data.logging.DefaultLoggingConfiguration;
import org.smicon.rest.data.logging.LoggingConfigurationI;
import org.smicon.rest.instances.logging.MutableLoggerI;

public class MutableBasicLogger
extends
BasicLogger
implements
MutableLoggerI
{

	LoggingConfigurationI configuration;
	
	public MutableBasicLogger()
	{
		this.setConfiguration(DefaultLoggingConfiguration.getInstance());
	}

	public LoggingConfigurationI getConfiguration()
	{
		return this.configuration;
	}

	public void setConfiguration(LoggingConfigurationI configuration)
	{
		this.configuration = configuration;
	}

}
