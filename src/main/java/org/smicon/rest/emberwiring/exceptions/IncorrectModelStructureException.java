package org.smicon.rest.emberwiring.exceptions;

public class IncorrectModelStructureException
extends
Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncorrectModelStructureException()
	{}

	public IncorrectModelStructureException(String message)
	{
		super(message);
	}

	public IncorrectModelStructureException(Throwable cause)
	{
		super(cause);
	}

	public IncorrectModelStructureException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public IncorrectModelStructureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
