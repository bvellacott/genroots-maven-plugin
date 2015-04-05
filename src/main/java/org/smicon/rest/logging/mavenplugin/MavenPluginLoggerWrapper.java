package org.smicon.rest.logging.mavenplugin;

import org.apache.maven.plugin.logging.Log;
import org.smicon.rest.BasicLoggers;
import org.smicon.rest.logging.LoggerI;
import org.smicon.rest.logging.LoggingConfigurationI;

public class MavenPluginLoggerWrapper
implements
LoggerI
{
	private Log mavenLogger;

	LoggingConfigurationI configuration;
	
	public MavenPluginLoggerWrapper(Log aMavenPluginLogger)
	{
		this.mavenLogger = aMavenPluginLogger;

		LoggingConfigurationI config = BasicLoggers.newConfiguration(); 
		config.setDebugEnabled(mavenLogger.isDebugEnabled());
		config.setInfoEnabled(mavenLogger.isInfoEnabled());
		config.setWarnEnabled(mavenLogger.isWarnEnabled());
		config.setErrorEnabled(mavenLogger.isErrorEnabled());
		
		configuration = config;
	}

	public LoggingConfigurationI getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public void debug(CharSequence content)
	{
		this.mavenLogger.debug(content);
	}

	@Override
	public void debug(CharSequence content, Throwable error)
	{
		this.mavenLogger.debug(content, error);
	}

	@Override
	public void debug(Throwable error)
	{
		this.mavenLogger.debug(error);
	}

	@Override
	public void info(CharSequence content)
	{
		this.mavenLogger.info(content);
	}
	
	@Override
	public void info(CharSequence content, Throwable error)
	{
		this.mavenLogger.info(content, error);
	}

	@Override
	public void info(Throwable error)
	{
		this.mavenLogger.info(error);
	}

	@Override
	public void warn(CharSequence content)
	{
		this.mavenLogger.warn(content);
	}

	@Override
	public void warn(CharSequence content, Throwable error)
	{
		this.mavenLogger.warn(content, error);
	}

	@Override
	public void warn(Throwable error)
	{
		this.mavenLogger.warn(error);
	}

	@Override
	public void error(CharSequence content)
	{
		this.mavenLogger.error(content);
	}

	@Override
	public void error(CharSequence content, Throwable error)
	{
		this.mavenLogger.error(content, error);
	}

	@Override
	public void error(Throwable error)
	{
		this.mavenLogger.error(error);
	}

}
