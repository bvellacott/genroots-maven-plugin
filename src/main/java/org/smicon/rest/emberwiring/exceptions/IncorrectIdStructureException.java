package org.smicon.rest.emberwiring.exceptions;

public class IncorrectIdStructureException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -719550133668245561L;

	public IncorrectIdStructureException(String message) {
		super(message);
	}
	
	public IncorrectIdStructureException(String message, Exception e) {
		super(message, e);
	}
	
}
