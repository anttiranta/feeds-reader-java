package com.antti.task.core.message;

import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class Factory 
{
    protected String[] types = new String[] {
        MessageInterface.TYPE_ERROR,
        MessageInterface.TYPE_NOTICE,
        MessageInterface.TYPE_SUCCESS
    };
    
    public MessageInterface create(String type) 
    {
        return this.create(type, null);
    }
    
    public MessageInterface create(String type, String text)
    {
        if (type == null || !Arrays.asList(this.types).contains(type)) {
            throw new IllegalArgumentException("Wrong message type");
        }
 
        MessageInterface message = null;
        switch(type){
            case MessageInterface.TYPE_ERROR:
                message = new Error();
                break;
            case MessageInterface.TYPE_NOTICE:
                message = new Notice();
                break;
            case MessageInterface.TYPE_SUCCESS:
                message = new Success();
                break;
        }
        
        if (text != null && !text.isEmpty()) {
            message.setText(text);
        }
        
        if (message == null) {
            String className = type.toUpperCase();
            throw new IllegalArgumentException (className + " doesn't implement MessageInterface");
        }
        return message;
     }
}
