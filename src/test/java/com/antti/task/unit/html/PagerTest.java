package com.antti.task.unit.html;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import com.antti.task.html.Pager;
import com.antti.task.core.UrlBuilder;
import com.antti.task.core.PaginationCounter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PagerTest {
    
    private Pager subject;
    private UrlBuilder urlBuilderMock;
    private PaginationCounter pCounterMock;
    
    @Before
    public void setup() {
        urlBuilderMock = mock(UrlBuilder.class);
        pCounterMock = mock(PaginationCounter.class);
        
        subject = new Pager(
            pCounterMock,
            urlBuilderMock
        );
    }
    
    @Test
    public void testCanRender() {
        assertFalse(subject.canRender());
        
        when(pCounterMock.getTotalNum()).thenReturn((long)0);
        assertFalse(subject.canRender());
        
        when(pCounterMock.getTotalNum()).thenReturn((long)1);
        when(pCounterMock.getLastPageNum()).thenReturn(2);
        assertTrue(subject.canRender());
        
        when(pCounterMock.getLastPageNum()).thenReturn(1);
        when(pCounterMock.getCurrentPage()).thenReturn(2);
        when(pCounterMock.getLastPageNum()).thenReturn(1);
        assertTrue(subject.canRender());
    }
    
    @Test
    public void testCanRenderPreviousPageUrl() {
        when(pCounterMock.isFirstPage()).thenReturn(true);
        assertFalse(subject.canRenderPreviousPageUrl());
        
        when(pCounterMock.isFirstPage()).thenReturn(false);
        assertTrue(subject.canRenderPreviousPageUrl());
    }
    
    @Test
    public void testCanRenderNextPageUrl() {
        when(pCounterMock.isLastPage()).thenReturn(true);
        assertFalse(subject.canRenderNextPageUrl());
        
        when(pCounterMock.isLastPage()).thenReturn(false);
        assertTrue(subject.canRenderNextPageUrl());
    }
    
    @Test
    public void testCanRenderLeftEllipsisPageUrl() {
        when(pCounterMock.getLastPageNum()).thenReturn(10);
        when(pCounterMock.getDisplayPages()).thenReturn(5);
        when(pCounterMock.getPages()).thenReturn(
             IntStream.rangeClosed(4, 9).boxed().collect(Collectors.toList())
        );
        when(pCounterMock.getFirstPageNum()).thenReturn(1);
        
        assertTrue(subject.canRenderLeftEllipsisPageUrl());
    }
    
    @Test
    public void testCanRenderRightEllipsisPageUrl() {
        when(pCounterMock.getLastPageNum()).thenReturn(10)
                .thenReturn(10);
        when(pCounterMock.getDisplayPages()).thenReturn(5);
        when(pCounterMock.getPages()).thenReturn(
             IntStream.rangeClosed(2, 7).boxed().collect(Collectors.toList())
        );
        
        assertTrue(subject.canRenderRightEllipsisPageUrl());
    }
}
