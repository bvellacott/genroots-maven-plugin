package org.smicon.rest.instances.logging;

import org.smicon.rest.data.logging.LoggingConfigurationI;

public interface MutableLoggerI
extends
LoggerI
{
	
	public void setConfiguration(LoggingConfigurationI configuration);
	
}
