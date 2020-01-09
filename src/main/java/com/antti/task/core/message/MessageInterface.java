package com.antti.task.core.message;

public interface MessageInterface {

    public static final String DEFAULT_IDENTIFIER = "default_message_identifier";
    public static final String TYPE_ERROR = "error";
    public static final String TYPE_WARNING = "warning";
    public static final String TYPE_NOTICE = "notice";
    public static final String TYPE_SUCCESS = "success";

    public String getType();

    public String getText();

    public MessageInterface setText(String text);

    public MessageInterface setIdentifier(String identifier);

    public String getIdentifier();

    public MessageInterface setIsSticky(boolean isSticky);

    public boolean getIsSticky();
}
