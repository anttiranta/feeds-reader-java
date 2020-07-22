 package com.antti.task.unit.core.api;

import org.junit.Test;
import com.antti.task.core.api.PaginationProcessor;
import javax.persistence.TypedQuery;
import static org.mockito.Mockito.*;
import com.antti.task.core.api.SearchCriteria;

public class PaginationProcessorTest {
    
    @Test
    public void testProcess() {
        PaginationProcessor subject = new PaginationProcessor();
        
        SearchCriteria searchCriteriaMock = mock(SearchCriteria.class);
        when(searchCriteriaMock.getCurrentPage()).thenReturn(3);
        when(searchCriteriaMock.getPageSize()).thenReturn(20);
        
        TypedQuery queryMock = mock(TypedQuery.class);
        
        subject.process(searchCriteriaMock, queryMock);
        
        verify(queryMock).setMaxResults(20);
        verify(queryMock).setFirstResult(40);
    }
}
