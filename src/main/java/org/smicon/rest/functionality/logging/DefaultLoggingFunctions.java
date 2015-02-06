package org.smicon.rest.functionality.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class DefaultLoggingFunctions
{

	public static final Logger debugLogger = Logger.getLogger("default debug logger");
	public static final Logger infoLogger = Logger.getLogger("default info logger");
	public static final Logger warnLogger = Logger.getLogger("default warn logger");
	public static final Logger errorLogger = Logger.getLogger("default error logger");
	
	public static final boolean isInitialised = init();
	
	private static boolean init()
	{
		debugLogger.setLevel(Level.FINEST);
		infoLogger.setLevel(Level.INFO);
		warnLogger.setLevel(Level.WARNING);
		errorLogger.setLevel(Level.SEVERE);
		return true;
	}
	
	public static void debug(CharSequence content)
	{
		log(infoLogger, Level.INFO, content);
	}

	public static void debug(CharSequence content, Throwable error)
	{
		log(debugLogger, Level.FINEST, content, error);
	}

	public static void debug(Throwable error)
	{
		log(debugLogger, Level.FINEST, error);
	}

	public static void info(CharSequence content)
	{
		log(infoLogger, Level.INFO, content);
	}

	public static void info(CharSequence content, Throwable error)
	{
		log(infoLogger, Level.INFO, content, error);
	}

	public static void info(Throwable error)
	{
		log(infoLogger, Level.INFO, error);
	}

	public static void warn(CharSequence content)
	{
		log(warnLogger, Level.WARNING, content);
	}

	public static void warn(CharSequence content, Throwable error)
	{
		log(warnLogger, Level.WARNING, content, error);
	}

	public static void warn(Throwable error)
	{
		log(warnLogger, Level.WARNING, error);
	}

	public static void error(CharSequence content)
	{
		log(errorLogger, Level.SEVERE, content);
	}

	public static void error(CharSequence content, Throwable error)
	{
		log(errorLogger, Level.SEVERE, content, error);
	}

	public static void error(Throwable error)
	{
		log(errorLogger, Level.SEVERE, error);
	}
	
	public static void log(Logger logger, Level logLevel, CharSequence content)
	{
		logger.log(logLevel, content.toString());
	}

	public static void log(Logger logger, Level logLevel, CharSequence content, Throwable error)
	{
		logger.log(logLevel, content.toString(), error);
	}

	public static void log(Logger logger, Level logLevel, Throwable error)
	{
		log(logger, logLevel, "", error);
	}
	
}
