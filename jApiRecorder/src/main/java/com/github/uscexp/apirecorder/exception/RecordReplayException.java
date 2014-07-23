/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder.exception;

/**
 * @author haui
 *
 */
public class RecordReplayException extends RuntimeException {

    private static final long serialVersionUID = -4734459417581822887L;

    public RecordReplayException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordReplayException(String message) {
        super(message);
    }

}
