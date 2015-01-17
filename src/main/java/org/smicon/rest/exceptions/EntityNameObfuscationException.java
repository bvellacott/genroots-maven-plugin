package org.smicon.rest.exceptions;

public class EntityNameObfuscationException
extends
Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8247648638486619513L;

	public EntityNameObfuscationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public EntityNameObfuscationException(String message)
	{
		super(message);
	}

}
