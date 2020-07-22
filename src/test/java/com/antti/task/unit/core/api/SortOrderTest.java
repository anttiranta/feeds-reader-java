package com.antti.task.unit.core.api;

import com.antti.task.core.api.SortOrder;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SortOrderTest {
    
    private SortOrder subject;
    
    @Before
    public void setup() {
        subject = new SortOrder();
    }
    
    @Test
    public void testItReturnsNullIfNoOrderIsSet(){
        assertNull(subject.getDirection());
    }
    
    @Test
    public void testItReturnsTheCorrectValuesIfSortOrderIsSet() {
        subject.setDirection(SortOrder.SORT_ASC);
        assertEquals(SortOrder.SORT_ASC, subject.getDirection());
        
        subject.setDirection(SortOrder.SORT_DESC); 
        assertEquals(SortOrder.SORT_DESC, subject.getDirection());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testItThrowsAnExceptionIfAnInvalidSortOrderIsSet() {
        subject.setDirection("1");
    }
    
    @Test
    public void testTheSortDirectionCanBeSpecifiedCaseInsensitive() {
        subject.setDirection(SortOrder.SORT_ASC.toLowerCase());
        assertEquals(SortOrder.SORT_ASC, subject.getDirection());
        subject.setDirection(SortOrder.SORT_ASC.toUpperCase());
        assertEquals(SortOrder.SORT_ASC, subject.getDirection());
        
        subject.setDirection(SortOrder.SORT_DESC.toLowerCase()); 
        assertEquals(SortOrder.SORT_DESC, subject.getDirection());
        subject.setDirection(SortOrder.SORT_DESC.toUpperCase()); 
        assertEquals(SortOrder.SORT_DESC, subject.getDirection());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testItValidatesADirectionAssignedDuringInstantiation() {
        subject = new SortOrder(null, "not-asc-or-desc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateField() {
        subject = new SortOrder("invalid field (value)", null);
    }
}
