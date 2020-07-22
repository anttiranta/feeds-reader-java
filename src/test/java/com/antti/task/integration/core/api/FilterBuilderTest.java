package com.antti.task.integration.core.api;

import com.antti.task.core.api.Filter;
import org.junit.Test;
import org.junit.Before;
import com.antti.task.core.api.FilterBuilder;
import static org.junit.Assert.*;

public class FilterBuilderTest {
    
    private FilterBuilder subject;
    
    @Before
    public void setup() {
        subject = new FilterBuilder();
    }
    
    @Test
    public void testCreate() {
        Filter filter = subject.setField("field")
                    .setConditionType("like")
                    .setValue("searchValue")
                    .create();
        
        assertEquals("field", filter.getField());
        assertEquals("like", filter.getConditionType());
        assertEquals("searchValue", filter.getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithBlankConditionType() {
        subject.setField("otherField")
                .setConditionType(" ")
                .setValue("otherSearchValue")
                .create();
    }
}
