package org.smicon.rest.emberwiring.general;

import org.smicon.rest.BasicLoggers;
import org.smicon.rest.logging.LoggerI;

public interface ConfigurationI
{
	
	public static final LoggerI default_logger = BasicLoggers.getDefaultLogger();

	LoggerI getLogger();
	
}
