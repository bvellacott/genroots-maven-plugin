package org.smicon.rest.instances.logging.basiclogger;

import org.smicon.rest.data.logging.DefaultLoggingConfiguration;
import org.smicon.rest.data.logging.LoggingConfigurationI;
import org.smicon.rest.functionality.logging.DefaultLoggingFunctions;
import org.smicon.rest.instances.logging.LoggerI;

public class BasicLogger
implements
LoggerI
{
	
	@Override
	public LoggingConfigurationI getConfiguration()
	{
		return DefaultLoggingConfiguration.getInstance();
	}	
	
	@Override
	public void debug(CharSequence content)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.debug(content);
	}

	@Override
	public void debug(CharSequence content, Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.debug(content, error);
	}

	@Override
	public void debug(Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.debug(error);
	}

	@Override
	public void info(CharSequence content)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.info(content);
	}

	@Override
	public void info(CharSequence content, Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.info(content, error);
	}

	@Override
	public void info(Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.info(error);
	}

	@Override
	public void warn(CharSequence content)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.warn(content);
	}

	@Override
	public void warn(CharSequence content, Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.warn(content, error);
	}

	@Override
	public void warn(Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.warn(error);
	}

	@Override
	public void error(CharSequence content)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.error(content);
	}

	@Override
	public void error(CharSequence content, Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.error(content, error);
	}

	@Override
	public void error(Throwable error)
	{
		if(this.getConfiguration().isDebugEnabled())
			DefaultLoggingFunctions.error(error);
	}

}
