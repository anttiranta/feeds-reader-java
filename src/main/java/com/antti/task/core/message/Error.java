package com.antti.task.core.message;

public class Error extends AbstractMessage {

    @Override
    public String getType() {
        return MessageInterface.TYPE_ERROR;
    }
}
