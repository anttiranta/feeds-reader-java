package com.antti.task.core.message;

import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class Factory 
{
    protected String[] types = new String[] {
        Message.TYPE_ERROR,
        Message.TYPE_NOTICE,
        Message.TYPE_SUCCESS
    };
    
    public Message create(String type) {
        return create(type, null);
    }
    
    public Message create(String type, String text) {
        if (type == null || !Arrays.asList(types).contains(type)) {
            throw new IllegalArgumentException("Wrong message type");
        }
 
        Message message = null;
        switch(type){
            case Message.TYPE_ERROR:
                message = new Error();
                break;
            case Message.TYPE_NOTICE:
                message = new Notice();
                break;
            case Message.TYPE_SUCCESS:
                message = new Success();
                break;
        }
        
        if (text != null && !text.isEmpty()) {
            message.setText(text);
        }
        
        if (message == null) {
            String className = type.toUpperCase();
            throw new IllegalArgumentException(className + " doesn't implement MessageInterface");
        }
        return message;
     }
}
