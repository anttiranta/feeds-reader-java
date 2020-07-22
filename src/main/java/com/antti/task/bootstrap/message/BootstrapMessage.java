package com.antti.task.bootstrap.message;

import com.antti.task.core.message.Message;

/**
 * Wrapper class which provides a mechanism to display the message with Bootstrap
 * styling in front end.
 */
public class BootstrapMessage implements Message {
    
    public static final String TYPE_BS_ERROR = "danger";
    public static final String TYPE_BS_WARNING = "warning";
    public static final String TYPE_BS_NOTICE = "info";
    public static final String TYPE_BS_SUCCESS = "success";
    
    private Message source;
    
    public BootstrapMessage(Message source) {
        if (source instanceof BootstrapMessage) {
            throw new IllegalArgumentException("Can't wrap around itself.");
        }
        this.source = source;
    }
    
    @Override
    public String getType() {
        switch (source.getType()) {
             case Message.TYPE_ERROR:
                return TYPE_BS_ERROR;
             case Message.TYPE_SUCCESS:
                return TYPE_BS_SUCCESS;
             case Message.TYPE_NOTICE:
                return TYPE_BS_NOTICE;
             default:
                 throw new RuntimeException("Unknown message type.");
        }
    }

    @Override
    public String getText() {
        return source.getText();
    }

    @Override
    public Message setText(String text) {
        return source.setText(text);
    }

    @Override
    public Message setIdentifier(String identifier) {
        return source.setIdentifier(identifier);
    }

    @Override
    public String getIdentifier() {
        return source.getIdentifier();
    }

    @Override
    public Message setIsSticky(boolean isSticky) {
        return source.setIsSticky(isSticky);
    }

    @Override
    public boolean getIsSticky() {
        return source.getIsSticky();
    }
}
