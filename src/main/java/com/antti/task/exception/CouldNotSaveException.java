package com.antti.task.exception;

public class CouldNotSaveException extends ClientSafeException {

    public CouldNotSaveException(Throwable cause) {
        super(cause);
    }

    public CouldNotSaveException(String message) {
        super(message);
    }
}
