package com.example.gc15.message;

public class MalformedMessageException extends Exception {

	private static final long serialVersionUID = 3910258538098175457L;

	public MalformedMessageException() {
	}

	public MalformedMessageException(String message) {
		super(message);
	}

	public MalformedMessageException(Throwable cause) {
		super(cause);
	}

	public MalformedMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MalformedMessageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStrackTrace) {
		super(message, cause, enableSuppression, writableStrackTrace);
	}

}
