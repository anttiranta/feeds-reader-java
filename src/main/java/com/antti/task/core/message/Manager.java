package com.antti.task.core.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Manager implements ManagerInterface {

    public static final String DEFAULT_GROUP = "default";
 
    @Autowired
    protected HttpSession session;
 
    private final Logger logger = Logger.getLogger("com.antti.task");
 
    protected String defaultGroup;
 
    protected boolean hasMessages = false;
    
    @Autowired
    private Factory messageFactory;
    
    public Manager(){
        this.defaultGroup = DEFAULT_GROUP;
    }
    
    public Manager(String defaultGroup){
        this.defaultGroup = defaultGroup;
    }
    
    @Override
    public List<MessageInterface> getMessages(String group, boolean clear) {
        String preparedGroup = this.prepareGroup(group);
        
        if (this.session.getAttribute(preparedGroup) == null) {
            this.session.setAttribute(preparedGroup, new ArrayList<>());
        }

        if (clear == true) {
            List<MessageInterface> messages = (List<MessageInterface>)this.session.getAttribute(preparedGroup);
            this.session.removeAttribute(preparedGroup);
            return messages;
        }
        
        return (List<MessageInterface>)session.getAttribute(preparedGroup);
    }

    @Override
    public List<MessageInterface> getMessages(String group) {
        return this.getMessages(group, false);
    }
    
    @Override
    public List<MessageInterface> getMessages(boolean clear) {
        return this.getMessages(null, clear);
    }

    @Override
    public String getDefaultGroup() {
        return this.defaultGroup;
    }
    
    protected String prepareGroup(String group)
    {
        return group != null && !group.isEmpty() ? group : this.defaultGroup;
    }

    @Override
    public ManagerInterface addMessage(MessageInterface message, String group) {
        this.hasMessages = true;
        this.getMessages(group).add(message);
        return this;
    }

    @Override
    public ManagerInterface addMessages(List<MessageInterface> messages, String group) {
        for (MessageInterface message : messages){
            this.addMessage(message, group);
        }
        return this;
    }

    @Override
    public ManagerInterface addMessages(List<MessageInterface> messages) {
        return this.addMessages(messages, null);
    }

    @Override
    public ManagerInterface addError(String message, String group) {
        this.addMessage(
            this.createMessage(MessageInterface.TYPE_ERROR).setText(message),
            group
        );
        return this;
    }

    @Override
    public ManagerInterface addError(String message) {
        return this.addError(message, null);
    }

    @Override
    public ManagerInterface addNotice(String message, String group) {
        this.addMessage(
            this.createMessage(MessageInterface.TYPE_NOTICE).setText(message),
            group
        );
        return this;
    }

    @Override
    public ManagerInterface addNotice(String message) {
        return this.addNotice(message, null);
    }

    @Override
    public ManagerInterface addSuccess(String message, String group) {
        this.addMessage(
            this.createMessage(MessageInterface.TYPE_NOTICE).setText(message),
            group
        );
        return this;
    }

    @Override
    public ManagerInterface addSuccess(String message) {
        return this.addSuccess(message, null);
    }

    @Override
    public ManagerInterface addException(Exception exception, String alternativeText, String group) {
        String message = String.format(
                "Exception message: %s%s Trace: %s", 
                exception.getMessage(), 
                "\n",
                Arrays.toString(exception.getStackTrace())
        );
        this.logger.error(message);
 
        if (alternativeText != null) {
            this.addError(alternativeText, group);
        } else {
            this.addError(exception.getMessage(), group);
        }
        return this;
    }

    @Override
    public ManagerInterface addException(Exception exception, String alternativeText) {
        return this.addException(exception, alternativeText, null);
    }

    @Override
    public ManagerInterface addException(Exception exception) {
        return this.addException(exception, null, null);
    }

    @Override
    public MessageInterface createMessage(String type, String identifier) {
        return this.messageFactory.create(type).setIdentifier(
                identifier != null && !identifier.isEmpty()
                ? MessageInterface.DEFAULT_IDENTIFIER
                : identifier
             );
    }
    
    @Override
    public MessageInterface createMessage(String type) {
        return this.createMessage(type, null);
    }
}
