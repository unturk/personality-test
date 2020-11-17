package com.galaksiya.exceptions;

public class ServiceFailureException extends Exception {

	private final String errorMessage;

	public ServiceFailureException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	/**
	 * @see #errorMessage
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

}
