package com.github.uscexp.apirecorder.exception;

public class ReplacementValueException extends Exception {

    private static final long serialVersionUID = 420389546650693722L;

    public ReplacementValueException(String message) {
        super(message);
    }

    public ReplacementValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
