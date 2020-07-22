package com.antti.task.integration.bootstrap.message;

import org.junit.Test;
import static org.junit.Assert.*;
import com.antti.task.bootstrap.message.BootstrapMessage;
import com.antti.task.core.message.Error;
import com.antti.task.core.message.Notice;
import com.antti.task.core.message.Success;
import com.antti.task.unit.core.message.TestingMessage;
import com.antti.task.core.message.Message;

public class BootstrapMessageTest {
    
    @Test
    public void testGetType() {
        Message subject = new BootstrapMessage(new Error());
        assertEquals(BootstrapMessage.TYPE_BS_ERROR, subject.getType());
        
        subject = new BootstrapMessage(new Notice());
        assertEquals(BootstrapMessage.TYPE_BS_NOTICE, subject.getType());
        
        subject = new BootstrapMessage(new Success());
        assertEquals(BootstrapMessage.TYPE_BS_SUCCESS, subject.getType());
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetTypeThrowsExceptionIfInvalidTypeIsGiven() {
        Message subject = new BootstrapMessage(new TestingMessage());
        subject.getType();
    }
    
    @Test
    public void testSetTextGetText() {
        Message subject = new BootstrapMessage(new Success());
        subject.setText("");
        
        assertEquals("", subject.getText());
        
        subject.setText("some text");
        assertEquals("some text", subject.getText());
    }
    
    @Test
    public void testSetIdentifierGetIdentifier() {
        Message subject = new BootstrapMessage(new Success());
        subject.setIdentifier("");
        assertEquals("", subject.getIdentifier());
        
        subject.setIdentifier("some identifier");
        assertEquals("some identifier", subject.getIdentifier());
    }
    
    @Test
    public void testSetIsStickyGetIsSticky() {
        Message subject = new BootstrapMessage(new Success());
        assertFalse(subject.getIsSticky());
        
        subject.setIsSticky(true);
        assertTrue(subject.getIsSticky());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExceptionIfTryingToWrapAnotherBootstrapMessage() 
    {
        Message subject = new BootstrapMessage(
             new BootstrapMessage(new Error())
        );
    }
}
