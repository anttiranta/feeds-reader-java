package com.antti.task.bootstrap.message;

import java.util.List;
import java.util.stream.Collectors;
import com.antti.task.core.message.Message;

public class MessageListMapper {
    
    public static List<BootstrapMessage> map(final List<Message> source) {
        return source.stream()
                .map(message -> new BootstrapMessage(message))
                .collect(Collectors.toList());
    }
}
