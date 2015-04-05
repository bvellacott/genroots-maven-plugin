package org.smicon.rest;

import org.smicon.rest.logging.DefaultLoggingConfiguration;
import org.smicon.rest.logging.LoggerI;
import org.smicon.rest.logging.LoggingConfigurationI;
import org.smicon.rest.logging.basiclogger.BasicLogger;
import org.smicon.rest.logging.basiclogger.MutableBasicLogger;
import org.smicon.rest.logging.silentLogger.SilentLogger;


public final class BasicLoggers
{
	public static final LoggingConfigurationI default_logging_configuration = new DefaultLoggingConfiguration();
	
	private static final LoggerI defaultInstance = new BasicLogger();
	private static final LoggerI silentInstance = new SilentLogger();
	
	public static LoggerI getDefaultLogger()
	{
		return defaultInstance;
	}
	
	public static LoggerI getSilentLogger()
	{
		return silentInstance;
	}
	
	public static MutableBasicLogger newBasicLogger()
	{
		return new MutableBasicLogger();
	}
	
	public static LoggingConfigurationI getDefaultConfiguration()
	{
		return default_logging_configuration;
	}
	
	public static LoggingConfigurationI newConfiguration()
	{
		return new DefaultLoggingConfiguration();
	}
}
