package com.antti.task.core.message;

public class Notice extends AbstractMessage {

    @Override
    public String getType() {
        return MessageInterface.TYPE_NOTICE;
    }
}
