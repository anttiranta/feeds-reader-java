package com.antti.task.unit.core.message;

import com.antti.task.core.message.Factory;
import com.antti.task.core.message.Success;
import com.antti.task.core.message.Error;
import com.antti.task.core.message.Notice;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.antti.task.core.message.Message;

public class FactoryTest {
    
    private Factory subject;
    
    @Before
    public void setup() {
        subject = new Factory();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongType() {
        subject.create("type", "text");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullType() {
        subject.create(null);
    }
    
    @Test
    public void testSuccessfulCreateMessage() {
        Message message = subject.create("success", "text");
        
        assertEquals(Success.class, message.getClass());
        assertEquals("text", message.getText());
        assertEquals(Message.TYPE_SUCCESS, message.getType());
    }
    
    @Test
    public void testCreateErrorMessageWithoutText() {
        Message message = subject.create("error");
        
        assertEquals(Error.class, message.getClass());
        assertEquals(null, message.getText());
    }
    
    @Test
    public void testCreateNoticeMessage() {
        Message message = subject.create("notice", "something else");
        assertEquals(Notice.class, message.getClass());
    }
}
