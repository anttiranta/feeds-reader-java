package com.antti.task.unit.core.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import com.antti.task.core.api.PageRequestBuilder;
import com.antti.task.core.api.SortOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Order;
import com.antti.task.core.api.SearchCriteria;

public class PageRequestBuilderTest {
    
    private PageRequestBuilder subject;
    private SearchCriteria searchCriteriaMock;
    
    @Before
    public void setup() {
        searchCriteriaMock = mock(SearchCriteria.class);
        subject = new PageRequestBuilder();
    }
    
    @Test
    public void testBuild() {
        when(searchCriteriaMock.getCurrentPage()).thenReturn(22);
        when(searchCriteriaMock.getPageSize()).thenReturn(33);
        when(searchCriteriaMock.getSortOrders()).thenReturn(new ArrayList<>());

        PageRequest pageRequest = subject.build(searchCriteriaMock);
        
        assertSame(22 - 1, pageRequest.getPageNumber());
        assertSame(33, pageRequest.getPageSize());
    }

     @Test
    public void testBuildWithSortOrders() {
        SortOrder sortOrderMock = mock(SortOrder.class);
        when(sortOrderMock.getField()).thenReturn("test_field");
        when(sortOrderMock.getDirection()).thenReturn(SortOrder.SORT_DESC);

        List<SortOrder> sortOrders = new ArrayList<>(Arrays.asList(sortOrderMock));
        
        when(searchCriteriaMock.getCurrentPage()).thenReturn(10);
        when(searchCriteriaMock.getPageSize()).thenReturn(50);
        when(searchCriteriaMock.getSortOrders()).thenReturn(sortOrders);
        
        PageRequest pageRequest = subject.build(searchCriteriaMock);
        
        Order order = pageRequest.getSort().getOrderFor("test_field");
        
        assertNotNull(order);
        assertTrue(order.getDirection().isDescending());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testItValidatesADirectionAssignedDuringBuild() {
        subject.build(searchCriteriaMock);
    }
}
