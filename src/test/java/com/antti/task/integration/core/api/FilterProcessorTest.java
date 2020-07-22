package com.antti.task.integration.core.api;

import com.antti.task.core.api.Filter;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import com.antti.task.core.api.FilterProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import com.antti.task.core.api.SearchCriteria;

public class FilterProcessorTest {
 
    private FilterProcessor subject;
    private SearchCriteria searchCriteriaMock;
    
    @Before
    public void setup() {
        searchCriteriaMock = mock(SearchCriteria.class);
        subject = new FilterProcessor();
    }
    
    @Test
    public void testProcess() {
        String initialQueryStr = "select i from item i where 1=2";
        
        Filter filterOneMock = mock(Filter.class);
        when(filterOneMock.getField()).thenReturn("firstFilterField");
        when(filterOneMock.getConditionType()).thenReturn("like");
        
        Filter filterTwoMock = mock(Filter.class);
        when(filterTwoMock.getField()).thenReturn("other.FilterField");
        when(filterTwoMock.getConditionType()).thenReturn(">");
        
        Filter filterThreeMock = mock(Filter.class);
        when(filterThreeMock.getField()).thenReturn("thirdField");
        when(filterThreeMock.getConditionType()).thenReturn(null);

        List<Filter> filters = new ArrayList<>(Arrays.asList(filterOneMock, filterTwoMock, filterThreeMock));
        when(searchCriteriaMock.getFilters()).thenReturn(filters);
        
        StringBuilder sb = new StringBuilder(initialQueryStr);
        
        subject.process(searchCriteriaMock, sb);
        
        String expectedSql = initialQueryStr 
                + " and firstFilterField like :firstFilterField"
                + " and other.FilterField > :other_FilterField"
                + " and thirdField = :thirdField";
        
        assertEquals(expectedSql, sb.toString());
    }
}
