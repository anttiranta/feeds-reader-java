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
public class ManagerImpl implements Manager {

    public static final String DEFAULT_GROUP = "default";

    protected String defaultGroup;
    protected boolean hasMessages = false;
    protected HttpSession session;
    private Logger logger;
    private Factory messageFactory;
    
    @Autowired
    public ManagerImpl(
        HttpSession session,
        Factory messageFactory
    ) {
        this(
            session, 
            messageFactory, 
            Logger.getLogger("com.antti.task"), 
            DEFAULT_GROUP
        );
    }
    
    public ManagerImpl(
        HttpSession session,
        Factory messageFactory,
        Logger logger
    ) {
        this(session, messageFactory, logger, DEFAULT_GROUP);
    }
    
    public ManagerImpl(
        HttpSession session,
        Factory messageFactory,
        Logger logger,
        String defaultGroup
    ) {
        this.session = session;
        this.messageFactory = messageFactory;
        this.logger = logger;
        this.defaultGroup = defaultGroup;
    }

    @Override
    public List<Message> getMessages(String group, boolean clear) {
        String preparedGroup = prepareGroup(group); 
       
        if (session.getAttribute(preparedGroup) == null) {
            session.setAttribute(preparedGroup, new ArrayList<>());
        }

        if (clear == true) {
            List<Message> messages = (List<Message>)session.getAttribute(preparedGroup);
            session.removeAttribute(preparedGroup);
            return messages;
        }
        
        return (List<Message>)session.getAttribute(preparedGroup);
    }

    @Override
    public List<Message> getMessages(String group) {
        return getMessages(group, false);
    }
    
    @Override
    public List<Message> getMessages(boolean clear) {
        return getMessages(null, clear);
    }

    @Override
    public String getDefaultGroup() {
        return defaultGroup;
    }
    
    protected String prepareGroup(String group){
        return group != null && !group.isEmpty() ? group : defaultGroup;
    }

    @Override
    public Manager addMessage(Message message, String group) {
        hasMessages = true;
        getMessages(group).add(message);
        return this;
    }

    @Override
    public Manager addMessages(List<Message> messages, String group) {
        for (Message message : messages){
            addMessage(message, group);
        }
        return this;
    }

    @Override
    public Manager addMessages(List<Message> messages) {
        return addMessages(messages, null);
    }

    @Override
    public Manager addError(String message, String group) {
        addMessage(
            createMessage(Message.TYPE_ERROR).setText(message),
            group
        );
        return this;
    }

    @Override
    public Manager addError(String message) {
        return addError(message, null);
    }

    @Override
    public Manager addNotice(String message, String group) {
        addMessage(
            createMessage(Message.TYPE_NOTICE).setText(message),
            group
        );
        return this;
    }

    @Override
    public Manager addNotice(String message) {
        return addNotice(message, null);
    }

    @Override
    public Manager addSuccess(String message, String group) {
        addMessage(
            createMessage(Message.TYPE_SUCCESS).setText(message),
            group
        );
        return this;
    }

    @Override
    public Manager addSuccess(String message) {
        return addSuccess(message, null);
    }

    @Override
    public Manager addException(Exception exception, String alternativeText, String group) {
        String message = String.format(
                "Exception message: %s%s Trace: %s", 
                exception.getMessage(), 
                "\n",
                Arrays.toString(exception.getStackTrace())
        );
        logger.error(message);
 
        if (alternativeText != null) {
            addError(alternativeText, group);
        } else {
            addError(exception.getMessage(), group);
        }
        return this;
    }

    @Override
    public Manager addException(Exception exception, String alternativeText) {
        return addException(exception, alternativeText, null);
    }

    @Override
    public Manager addException(Exception exception) {
        return addException(exception, null, null);
    }
    
    public boolean hasMessages() {
        return hasMessages;
    }

    @Override
    public Message createMessage(String type, String identifier) {
        return messageFactory.create(type).setIdentifier(identifier != null && !identifier.isEmpty()
                ? identifier
                : Message.DEFAULT_IDENTIFIER
             );
    }
    
    @Override
    public Message createMessage(String type) {
        return createMessage(type, null);
    }
}
