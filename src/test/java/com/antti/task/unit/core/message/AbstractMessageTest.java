package com.antti.task.unit.core.message;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.antti.task.core.message.AbstractMessage;

public class AbstractMessageTest {
    
    private AbstractMessage subject;
    
    @Before
    public void setup() {
        subject = new TestingMessage();
    }
    
    @Test
    public void testSetTextGetText() {
        subject.setText("");
        assertEquals("", subject.getText());
        
        subject.setText("some text");
        assertEquals("some text", subject.getText());
    }
    
    @Test
    public void testSetIdentifierGetIdentifier() {
        subject.setIdentifier("");
        assertEquals("", subject.getIdentifier());
        
        subject.setIdentifier("some identifier");
        assertEquals("some identifier", subject.getIdentifier());
    }
    
    @Test
    public void testSetIsStickyGetIsSticky() {
        assertFalse(subject.getIsSticky());
        
        subject.setIsSticky(true);
        assertTrue(subject.getIsSticky());
    }
}
