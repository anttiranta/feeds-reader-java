package com.antti.task.unit.item.importing;

import com.antti.task.item.importing.Helper;
import static org.junit.Assert.*;
import org.junit.Test;

public class HelperTest {
    
    @Test
    public void testNormalizeUrl() {
        Helper subject = new Helper();
        String result = subject.normalizeUrl("www.something.com");
        
        assertEquals("http://www.something.com", result);
    }
}
