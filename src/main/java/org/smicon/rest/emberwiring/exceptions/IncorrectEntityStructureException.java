package org.smicon.rest.emberwiring.exceptions;

public class IncorrectEntityStructureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8007280458975741547L;

	public IncorrectEntityStructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectEntityStructureException(String message) {
		super(message);
	}

}
