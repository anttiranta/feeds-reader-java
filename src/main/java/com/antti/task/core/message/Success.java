package com.antti.task.core.message;

public class Success extends AbstractMessage {

    public Success () {
        super();
    }
    
    public Success (String text) {
        super(text);
    }
    
    @Override
    public String getType() {
        return Message.TYPE_SUCCESS;
    }
}
