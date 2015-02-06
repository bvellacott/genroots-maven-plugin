package org.smicon.rest;

import org.smicon.rest.data.logging.DefaultLoggingConfiguration;
import org.smicon.rest.data.logging.LoggingConfigurationI;
import org.smicon.rest.data.logging.MutableLoggingConfiguration;
import org.smicon.rest.data.logging.MutableLoggingConfigurationI;
import org.smicon.rest.instances.logging.LoggerI;
import org.smicon.rest.instances.logging.basiclogger.BasicLogger;
import org.smicon.rest.instances.logging.basiclogger.MutableBasicLogger;
import org.smicon.rest.instances.logging.silentLogger.SilentLogger;


public final class BasicLoggers
{
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
		return DefaultLoggingConfiguration.getInstance();
	}
	
	public static MutableLoggingConfigurationI newConfiguration()
	{
		return new MutableLoggingConfiguration();
	}
}
