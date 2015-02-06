package org.smicon.rest.instances.logging.silentLogger;

import org.smicon.rest.data.logging.LoggingConfigurationI;
import org.smicon.rest.instances.logging.LoggerI;

public class SilentLogger
implements
LoggerI
{

	@Override
	public LoggingConfigurationI getConfiguration()
	{
		return null;
	}

	@Override
	public void debug(CharSequence content)
	{}

	@Override
	public void debug(CharSequence content, Throwable error)
	{}

	@Override
	public void debug(Throwable error)
	{}

	@Override
	public void info(CharSequence content)
	{}

	@Override
	public void info(CharSequence content, Throwable error)
	{}

	@Override
	public void info(Throwable error)
	{}

	@Override
	public void warn(CharSequence content)
	{}

	@Override
	public void warn(CharSequence content, Throwable error)
	{}

	@Override
	public void warn(Throwable error)
	{}

	@Override
	public void error(CharSequence content)
	{}

	@Override
	public void error(CharSequence content, Throwable error)
	{}

	@Override
	public void error(Throwable error)
	{}

}
