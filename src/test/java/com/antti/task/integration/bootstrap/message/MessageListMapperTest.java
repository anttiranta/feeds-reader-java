package com.antti.task.integration.bootstrap.message;

import org.junit.Test;
import static org.junit.Assert.*;
import com.antti.task.bootstrap.message.BootstrapMessage;
import com.antti.task.core.message.Error;
import com.antti.task.core.message.Success;
import com.antti.task.bootstrap.message.MessageListMapper;
import java.util.Arrays;
import java.util.List;

public class MessageListMapperTest {
    
    @Test
    public void testMap() {
        List<BootstrapMessage> messages = MessageListMapper.map(Arrays.asList(
             new Error("error text"), new Success("some text")
        ));
        assertEquals(2, messages.size());
    }
}
