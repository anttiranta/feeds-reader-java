package com.antti.task.exception;

public class CouldNotSaveException extends LocalizedException {

    public CouldNotSaveException(Throwable cause) {
        super(cause);
    }

    public CouldNotSaveException(String message) {
        super(message);
    }
}
