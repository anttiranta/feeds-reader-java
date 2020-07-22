package com.antti.task.unit.core;

import com.antti.task.core.PaginationCounter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PaginationCounterTest {
    
    private PaginationCounter paginationCounter;
    private Page page;
    private HttpServletRequest request;
    private Pageable pageable;
    
    @Before
    public void setup() {
        page = mock(Page.class);
        pageable = mock(Pageable.class);
        request = mock(HttpServletRequest.class);
        paginationCounter = new PaginationCounter(request, page);
        
        when(page.getPageable()).thenReturn(pageable);
    }
    
    @Test
    public void testGetFirstNum() {
        when(pageable.getPageSize()).thenReturn(5);
        when(page.getTotalElements()).thenReturn((long)15);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        assertEquals(1, (long)paginationCounter.getFirstNum());

        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("2");
        paginationCounter = new PaginationCounter(request, page);
        
        assertEquals(6, (long)paginationCounter.getFirstNum());

        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("3");
        paginationCounter = new PaginationCounter(request, page);
        
        assertEquals(11, (long)paginationCounter.getFirstNum());
    }
    
    @Test
    public void testGetLastNum() {
        when(pageable.getPageSize()).thenReturn(5);
        when(page.getTotalElements()).thenReturn((long)15);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        assertEquals(5, (long)paginationCounter.getLastNum());

        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("2");
        paginationCounter = new PaginationCounter(request, page);
        
        assertEquals(10, (long)paginationCounter.getLastNum());

        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("3");
        paginationCounter = new PaginationCounter(request, page);
        
        assertEquals(15, (long)paginationCounter.getLastNum());
    }
    
    @Test
    public void testPaginationWithoutRounding() {
        when(pageable.getPageSize()).thenReturn(5);
        when(page.getTotalElements()).thenReturn((long)15);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        assertEquals(3, (long)paginationCounter.getLastPageNum());
        assertEquals(1, (long)paginationCounter.getFirstPageNum());
        assertEquals(2,(long)paginationCounter.getNextPageNum());
    }
    
    @Test
    public void testPaginationWithRounding() {
        when(pageable.getPageSize()).thenReturn(4);
        when(page.getTotalElements()).thenReturn((long)15);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        List<Integer> pages = paginationCounter.getPages();
        
        assertEquals(4, (long)paginationCounter.getLastPageNum());
        assertEquals(4, pages.size());
        
        int[] expectedPages = {1,2,3,4};
        for (int i : expectedPages) {
            assertTrue(pages.contains(i));
        }
    }
    
    @Test
    public void testCurrentNextAndPrevPage() {
        when(pageable.getPageSize()).thenReturn(5);
        when(page.getTotalElements()).thenReturn((long)15);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        assertEquals(1, (int)paginationCounter.getCurrentPage());
        assertEquals(2, (int)paginationCounter.getNextPageNum());
        assertEquals(0, (int)paginationCounter.getPreviousPageNum());
        assertTrue(paginationCounter.isFirstPage());
        
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("2");
        paginationCounter = new PaginationCounter(request, page);
        
        assertEquals(2, (int)paginationCounter.getCurrentPage());
        assertEquals(3, (int)paginationCounter.getNextPageNum());
        assertEquals(1, (int)paginationCounter.getPreviousPageNum());
        assertFalse(paginationCounter.isLastPage());
        assertFalse(paginationCounter.isFirstPage());
        
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("3");
        paginationCounter = new PaginationCounter(request, page);
        
        assertEquals(3, (int)paginationCounter.getCurrentPage());
        assertEquals(4, (int)paginationCounter.getNextPageNum());
        assertTrue(paginationCounter.isLastPage());
        assertFalse(paginationCounter.isFirstPage());
        
        List<Integer> pages = paginationCounter.getPages();
        
        int[] expectedPages = {1,2,3};
        for (int i : expectedPages) {
            assertTrue(pages.contains(i));
        }
    }
    
    @Test
    public void testIsCurrentPage() {
        when(pageable.getPageSize()).thenReturn(5);
        when(page.getTotalElements()).thenReturn((long)15);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        assertTrue(paginationCounter.isPageCurrent(1));
        assertFalse(paginationCounter.isPageCurrent(2));
        
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("2");
        paginationCounter = new PaginationCounter(request, page);
        
        assertTrue(paginationCounter.isPageCurrent(2));
        assertFalse(paginationCounter.isPageCurrent(1));
        assertFalse(paginationCounter.isPageCurrent(3));
        
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("3");
        paginationCounter = new PaginationCounter(request, page);
        
        assertTrue(paginationCounter.isPageCurrent(3));
        assertFalse(paginationCounter.isPageCurrent(2));
    }
    
    @Test
    public void testLastPageWithOddTotalRecordsNum() {
        when(pageable.getPageSize()).thenReturn(5);
        when(page.getTotalElements()).thenReturn((long)6);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        assertEquals(2, (int)paginationCounter.getLastPageNum());
        
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("2");
        paginationCounter = new PaginationCounter(request, page);
        
        assertEquals(2, (int)paginationCounter.getLastPageNum());
    }
    
    @Test
    public void testGetPagesReturnsList() {
        when(pageable.getPageSize()).thenReturn(100);
        when(page.getTotalElements()).thenReturn((long)201);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        
        List<Integer> pages = paginationCounter.getPages();
        
        assertEquals(3, (long)paginationCounter.getLastPageNum());
        assertEquals(3, pages.size());
        
        int[] expectedPages = {1,2,3};
        for (int i : expectedPages) {
            assertTrue(pages.contains(i));
        }
    }
    
    @Test
    public void testPaginationReturnsOnlyAllowedNumberOfPages() {
        when(page.getTotalElements()).thenReturn((long)100);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");
        when(pageable.getPageSize()).thenReturn(3);
        
        List<Integer> pages = paginationCounter.getPages();
        
        assertEquals(34, (long)paginationCounter.getLastPageNum());
        assertEquals(5, pages.size());
        
        int[] expectedPages = {1,2,3,4,5};
        for (int i : expectedPages) {
            assertTrue(pages.contains(i));
        }
        
        int[] nonIncludedPages  = {6, 15, 30};
        for (int i : nonIncludedPages ) {
            assertFalse(pages.contains(i));
        }
    }
    
    @Test
    public void testPaginationWithCurrentPageInTheMiddleOfList() {
        when(page.getTotalElements()).thenReturn((long)100);
        when(pageable.getPageSize()).thenReturn(5);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("12");
        
        List<Integer> pages = paginationCounter.getPages();
        
        assertEquals(1, (long)paginationCounter.getFirstPageNum());
        assertEquals(20, (long)paginationCounter.getLastPageNum());
        assertEquals(11, (long)paginationCounter.getPreviousPageNum());
        assertEquals(13, (long)paginationCounter.getNextPageNum());
        assertEquals(5, pages.size());
        
        int[] expectedPages = {10, 11, 12, 13, 14};
        for (int i : expectedPages) {
            assertTrue(pages.contains(i));
        }
    }
    
    @Test
    public void testPaginationWithCurrentPageNearTheEndOfList() {
        when(page.getTotalElements()).thenReturn((long)100);
        when(pageable.getPageSize()).thenReturn(5);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("14");
        
        List<Integer> pages = paginationCounter.getPages();
        
        int[] expectedPages = {12, 13, 14, 15, 16};
        for (int i : expectedPages) {
            assertTrue(pages.contains(i));
        }
        
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("18");
        paginationCounter = new PaginationCounter(request, page);
        
        pages = paginationCounter.getPages();
        
        expectedPages = new int[] {16,17,18,19,20};
        for (int i : expectedPages) {
            assertTrue(pages.contains(i));
        }
    }
    
    @Test
    public void testReturnsCorrectLimits() {
        Map<Integer, Integer> limits = paginationCounter.getAvailableLimit();
        
        int[] expectedLimits = {5,10,20,50};
        for (int i : expectedLimits) {
            assertTrue(limits.containsKey(i));
            assertTrue(limits.containsValue(i));
        }
    }
    
    @Test
    public void testLimitWithAllowedLimits() {
        // Test that limiting works as intended
        when(page.getTotalElements()).thenReturn((long)100);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");

        when(request.getParameter(paginationCounter.getLimitVarName())).thenReturn("10");
        assertEquals(10, (int)paginationCounter.getLimit());
        
        when(request.getParameter(paginationCounter.getLimitVarName())).thenReturn("20");
        assertEquals(20, (int)paginationCounter.getLimit());
    }
    
    @Test
    public void testLimitWithBiggerThanAllowedLimits() {
        // Test that user can't set limit different that what we allow
        when(page.getTotalElements()).thenReturn((long)100);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("1");

        when(request.getParameter(paginationCounter.getLimitVarName())).thenReturn("34");
        assertEquals(5, (int)paginationCounter.getLimit());
        
        when(request.getParameter(paginationCounter.getLimitVarName())).thenReturn("60");
        assertEquals(5, (int)paginationCounter.getLimit());
    }
    
    @Test
    public void testInvalidCurrentPage() {
        when(page.getTotalElements()).thenReturn((long)100);
        when(pageable.getPageSize()).thenReturn(5);
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn("21");
        
        assertEquals(21, (long)paginationCounter.getCurrentPage());
    }
    
    @Test
    public void testGetCurrentPageReturnsPageOneIfRequestHasNoPageNumberParameter() {
        when(request.getParameter(paginationCounter.getPageVarName())).thenReturn(null);
        assertEquals(1, (long)paginationCounter.getCurrentPage());
    }
}
