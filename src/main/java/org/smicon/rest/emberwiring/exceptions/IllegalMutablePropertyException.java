package org.smicon.rest.emberwiring.exceptions;

public class IllegalMutablePropertyException
extends
Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalMutablePropertyException(String message)
	{
		super(message);
	}

	public IllegalMutablePropertyException(Throwable cause)
	{
		super(cause);
	}

}
