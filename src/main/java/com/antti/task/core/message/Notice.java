package com.antti.task.core.message;

public class Notice extends AbstractMessage {

    public Notice () {
        super();
    }
    
    public Notice (String text) {
        super(text);
    }
    
    @Override
    public String getType() {
        return Message.TYPE_NOTICE;
    }
}
