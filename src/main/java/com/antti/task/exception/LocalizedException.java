package com.antti.task.exception;

public class LocalizedException extends Exception {
    
    public LocalizedException(Throwable cause) {
        super(cause);
    }

    // TODO: should take translated string as param
    public LocalizedException(String message) {
        super(message);
    }
}
