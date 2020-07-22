package com.antti.task.unit.core.message;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.antti.task.core.message.Success;
import com.antti.task.core.message.Message;

public class SuccessTest {
    
    private Success subject;
    
    @Before
    public void setup() {
        subject = new Success();
    }
    
    @Test
    public void testGetType() {
        assertEquals(Message.TYPE_SUCCESS, subject.getType());
    }
}
