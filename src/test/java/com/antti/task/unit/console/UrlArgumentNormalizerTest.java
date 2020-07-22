package com.antti.task.unit.console;

import static org.junit.Assert.*;
import org.junit.Test;
import com.antti.task.console.UrlArgumentNormalizer;
import org.junit.Before;

public class UrlArgumentNormalizerTest {
    
    private UrlArgumentNormalizer subject;
    
    @Before
    public void setup() {
        subject = new UrlArgumentNormalizer();
    }
    
    @Test
    public void testNormalizeUrlArgument() {
        String[] urls = subject.normalizeUrlArgument("https://www.feedforall.com/sample.xml");
        
        assertSame(1, urls.length);
        assertArrayEquals(new String[] {"https://www.feedforall.com/sample.xml"}, urls);
    }
    
    @Test
    public void testNormalizeUrlArgumentWithUrlsSeparatedByCommas() {
        String[] urls = subject.normalizeUrlArgument(
             "https://www.feedforall.com/sample.xml,https://www.feedforall.com/sample-feed.xml"
        );
        
        assertSame(2, urls.length);
        assertArrayEquals(
            new String[] {"https://www.feedforall.com/sample-feed.xml","https://www.feedforall.com/sample.xml"}, 
            urls
        );
    }
}
