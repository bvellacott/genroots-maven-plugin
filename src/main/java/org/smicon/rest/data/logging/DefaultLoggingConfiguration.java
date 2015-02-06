package org.smicon.rest.data.logging;

public class DefaultLoggingConfiguration
implements
LoggingConfigurationI
{
	
	private static final DefaultLoggingConfiguration instance = new DefaultLoggingConfiguration();
	
	public static DefaultLoggingConfiguration getInstance()
	{
		return instance;
	}
	
	@Override
	public LoggingConfigurationI getDefaultConfiguration()
	{
		return this;
	}

	@Override
	public boolean isDebugEnabled()
	{
		return LoggingConfigurationI.default_debug_enabled;
	}

	@Override
	public boolean isInfoEnabled()
	{
		return LoggingConfigurationI.default_info_enabled;
	}

	@Override
	public boolean isWarnEnabled()
	{
		return LoggingConfigurationI.default_warn_enabled;
	}

	@Override
	public boolean isErrorEnabled()
	{
		return LoggingConfigurationI.default_error_enabled;
	}

}
