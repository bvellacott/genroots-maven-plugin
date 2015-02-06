package org.smicon.rest.data.logging;

public class MutableLoggingConfiguration
implements
MutableLoggingConfigurationI
{
	
	boolean debugEnabled;
	boolean infoEnabled;
	boolean warnEnabled;
	boolean errorEnabled;
	
	public MutableLoggingConfiguration()
	{
		this.setDefaultValues();
	}
	
	private void setDefaultValues()
	{
		this.debugEnabled = LoggingConfigurationI.default_debug_enabled;
		this.infoEnabled = LoggingConfigurationI.default_info_enabled;
		this.warnEnabled = LoggingConfigurationI.default_warn_enabled;
		this.errorEnabled = LoggingConfigurationI.default_error_enabled;
	}

	@Override
	public LoggingConfigurationI getDefaultConfiguration()
	{
		return DefaultLoggingConfiguration.getInstance();
	}

	@Override
	public boolean isDebugEnabled()
	{
		return this.debugEnabled;
	}

	@Override
	public void setDebugEnabled(boolean debugEnabled)
	{
		this.debugEnabled = debugEnabled;
	}

	@Override
	public boolean isInfoEnabled()
	{
		return this.infoEnabled;
	}

	@Override
	public void setInfoEnabled(boolean infoEnabled)
	{
		this.infoEnabled = infoEnabled;
	}

	@Override
	public boolean isWarnEnabled()
	{
		return this.warnEnabled;
	}

	@Override
	public void setWarnEnabled(boolean warnEnabled)
	{
		this.warnEnabled = warnEnabled;
	}

	@Override
	public boolean isErrorEnabled()
	{
		return this.errorEnabled;
	}

	@Override
	public void setErrorEnabled(boolean errorEnabled)
	{
		this.errorEnabled = errorEnabled;
	}
	
}
