package org.smicon.rest.logging.silentLogger;

import org.smicon.rest.logging.LoggerI;
import org.smicon.rest.logging.LoggingConfigurationI;

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
