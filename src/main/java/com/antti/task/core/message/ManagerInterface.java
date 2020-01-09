package com.antti.task.core.message;

import java.util.List;

public interface ManagerInterface 
{
    public List<MessageInterface> getMessages(String group, boolean clear);
    
    public List<MessageInterface> getMessages(String group);
    
    public List<MessageInterface> getMessages(boolean clear);
 
    public String getDefaultGroup();
 
    public ManagerInterface addMessage(MessageInterface message, String group);
 
    public ManagerInterface addMessages(List<MessageInterface> messages, String group);
     
    public ManagerInterface addMessages(List<MessageInterface> messages);
 
    public ManagerInterface addError(String message, String group);
    
    public ManagerInterface addError(String message);
 
    public ManagerInterface addNotice(String message, String group);
    
    public ManagerInterface addNotice(String message);
 
    public ManagerInterface addSuccess(String message, String group);
    
    public ManagerInterface addSuccess(String message);
 
    public ManagerInterface addException(Exception exception, String alternativeText, String group);
    
    public ManagerInterface addException(Exception exception, String alternativeText);
    
    public ManagerInterface addException(Exception exception);
 
    public MessageInterface createMessage(String type, String identifier);
    
    public MessageInterface createMessage(String type);
}
