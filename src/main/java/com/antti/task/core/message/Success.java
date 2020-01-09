package com.antti.task.core.message;

public class Success extends AbstractMessage {

    @Override
    public String getType() {
        return MessageInterface.TYPE_SUCCESS;
    }
}
