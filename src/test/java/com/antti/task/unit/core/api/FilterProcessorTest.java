package com.antti.task.unit.core.api;

import com.antti.task.core.api.Filter;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import com.antti.task.core.api.FilterProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.TypedQuery;
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
        TypedQuery queryMock = mock(TypedQuery.class);

        Filter filterOneMock = mock(Filter.class);
        when(filterOneMock.getField()).thenReturn("firstFilterField");
        when(filterOneMock.getConditionType()).thenReturn("like");
        when(filterOneMock.getValue()).thenReturn("firstFilterFieldValue");
        
        Filter filterTwoMock = mock(Filter.class);
        when(filterTwoMock.getField()).thenReturn("other.FilterField");
        when(filterTwoMock.getConditionType()).thenReturn(">");
        when(filterTwoMock.getValue()).thenReturn("otherFilterFieldValue");

        List<Filter> filters = new ArrayList<>(Arrays.asList(filterOneMock, filterTwoMock));
        when(searchCriteriaMock.getFilters()).thenReturn(filters);
        
        subject.process(searchCriteriaMock, queryMock);
        
        verify(filterOneMock, times(1)).getField();
        verify(filterOneMock, times(1)).getConditionType();
        verify(filterOneMock, times(1)).getValue();
        
        verify(filterTwoMock, times(1)).getField();
        verify(filterTwoMock, times(1)).getConditionType();
        verify(filterTwoMock, times(1)).getValue();
        
        verify(queryMock, times(1)).setParameter("firstFilterField", "firstFilterFieldValue%");
        verify(queryMock, times(1)).setParameter("other_FilterField", "otherFilterFieldValue");
    }
}
