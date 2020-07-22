package com.antti.task.core.message;

import java.util.List;

public interface Manager 
{
    public List<Message> getMessages(String group, boolean clear);
    
    public List<Message> getMessages(String group);
    
    public List<Message> getMessages(boolean clear);
 
    public String getDefaultGroup();
 
    public Manager addMessage(Message message, String group);
 
    public Manager addMessages(List<Message> messages, String group);
     
    public Manager addMessages(List<Message> messages);
 
    public Manager addError(String message, String group);
    
    public Manager addError(String message);
 
    public Manager addNotice(String message, String group);
    
    public Manager addNotice(String message);
 
    public Manager addSuccess(String message, String group);
    
    public Manager addSuccess(String message);
 
    public Manager addException(Exception exception, String alternativeText, String group);
    
    public Manager addException(Exception exception, String alternativeText);
    
    public Manager addException(Exception exception);
 
    public Message createMessage(String type, String identifier);
    
    public Message createMessage(String type);
}
