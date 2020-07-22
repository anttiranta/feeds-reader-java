package com.antti.task.unit.core.message;

import com.antti.task.core.message.AbstractMessage;

public class TestingMessage extends AbstractMessage {

    public final String TYPE_TESTING = "testing";
    
    @Override
    public String getType() {
        return TYPE_TESTING;
    }
    
}
