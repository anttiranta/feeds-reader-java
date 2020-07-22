package com.antti.task.unit.core.message;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.antti.task.core.message.Notice;
import com.antti.task.core.message.Message;

public class NoticeTest {
    
    private Notice subject;
    
    @Before
    public void setup() {
        subject = new Notice();
    }
    
    @Test
    public void testGetType() {
        assertEquals(Message.TYPE_NOTICE, subject.getType());
    }
}
