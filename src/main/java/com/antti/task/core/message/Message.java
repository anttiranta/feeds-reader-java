package com.antti.task.core.message;

public interface Message {

    public static final String DEFAULT_IDENTIFIER = "default_message_identifier";
    public static final String TYPE_ERROR = "error";
    public static final String TYPE_NOTICE = "notice";
    public static final String TYPE_SUCCESS = "success";

    public String getType();

    public String getText();

    public Message setText(String text);

    public Message setIdentifier(String identifier);

    public String getIdentifier();

    public Message setIsSticky(boolean isSticky);

    public boolean getIsSticky();
}
