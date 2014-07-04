/*
 * (C) 2014 haui
 */
package haui.apirecorder.exception;

/**
 * @author haui
 *
 */
public class RecordPlaybackException extends RuntimeException {

    private static final long serialVersionUID = -4734459417581822887L;

    public RecordPlaybackException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordPlaybackException(String message) {
        super(message);
    }

}
