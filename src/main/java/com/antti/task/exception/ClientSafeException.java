package com.antti.task.exception;

public class ClientSafeException extends Exception {
    
    public ClientSafeException(Throwable cause) {
        super(cause);
    }

    public ClientSafeException(String message) {
        super(message);
    }
}
