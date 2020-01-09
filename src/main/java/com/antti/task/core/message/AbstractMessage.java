package com.antti.task.core.message;

import java.util.List;

public abstract class AbstractMessage implements MessageInterface {

    protected String text;
 
    protected String identifier;
 
    protected boolean isSticky = false;
 
    protected List<String> data;
    
    public AbstractMessage(){
    }
    
    public AbstractMessage(String text){
        this.text = text;
    }
    
    @Override
    abstract public String getType();

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public MessageInterface setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public MessageInterface setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public MessageInterface setIsSticky(boolean isSticky) {
        this.isSticky = isSticky;
        return this;
    }

    @Override
    public boolean getIsSticky() {
        return this.isSticky;
    }
}
