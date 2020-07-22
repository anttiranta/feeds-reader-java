package com.antti.task.core.message;

public class Error extends AbstractMessage {

    public Error () {
        super();
    }
    
    public Error (String text) {
        super(text);
    }
    
    @Override
    public String getType() {
        return Message.TYPE_ERROR;
    }
}
