package com.antti.task.unit.core.message;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.antti.task.core.message.Error;
import com.antti.task.core.message.Message;

public class ErrorTest {
    
    private Error subject;
    
    @Before
    public void setup() {
        subject = new Error();
    }
    
    @Test
    public void testGetType() {
        assertEquals(Message.TYPE_ERROR, subject.getType());
    }
}
