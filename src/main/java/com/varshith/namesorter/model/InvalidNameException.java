package com.varshith.namesorter.model;

/** Thrown when some text is not a valid {@link PersonName}. The message says why. */
public class InvalidNameException extends RuntimeException {

    public InvalidNameException(String message) {
        super(message);
    }

    public InvalidNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
