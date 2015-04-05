package org.smicon.rest.logging.basiclogger;

import org.smicon.rest.BasicLoggers;
import org.smicon.rest.logging.LoggingConfigurationI;
import org.smicon.rest.logging.MutableLoggerI;

public class MutableBasicLogger
extends
BasicLogger
implements
MutableLoggerI
{

	LoggingConfigurationI configuration;
	
	public MutableBasicLogger()
	{
		this.setConfiguration(BasicLoggers.getDefaultConfiguration());
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
