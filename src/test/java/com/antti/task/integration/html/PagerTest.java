package com.antti.task.integration.html;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import com.antti.task.html.Pager;
import com.antti.task.core.PaginationCounter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public class PagerTest {
    
    private static final String TEST_URL = "www.test-site.com/";
    
    private Pager subject;
    private PaginationCounter pCounterMock;
    private HttpServletRequest requestMock;
    private Page pageMock;
    
    @Before
    public void setup() {
        pageMock = mock(Page.class);
        
        requestMock = mock(HttpServletRequest.class);
        when(requestMock.getRequestURL()).thenReturn(new StringBuffer(TEST_URL));
        
        pCounterMock = mock(PaginationCounter.class);
        when(pCounterMock.getRequest()).thenReturn(requestMock);
        when(pCounterMock.getPageVarName()).thenReturn("p");
        when(pCounterMock.getLimitVarName()).thenReturn("limit");
        when(pCounterMock.getPage()).thenReturn(pageMock);
        
        subject = new Pager(pCounterMock);
    }
    
    @Test
    public void testGetLeftEllipsisPageUrl() {
        when(requestMock.getParameterMap()).thenReturn(new HashMap<>());
        when(pCounterMock.getPages()).thenReturn(
             IntStream.rangeClosed(3, 5).boxed().collect(Collectors.toList())
        );
        assertEquals(TEST_URL + "?p=2", subject.getLeftEllipsisPageUrl());
    }
    
    @Test
    public void testGetRightEllipsisPageUrl() {
        Map<String, String[]> params = new HashMap<>();
        params.put("p", new String[]{"50"});

        when(requestMock.getParameterMap()).thenReturn(params);
        when(pCounterMock.getPages()).thenReturn(
             IntStream.rangeClosed(3, 5).boxed().collect(Collectors.toList())
        );
        assertEquals(TEST_URL + "?p=6", subject.getRightEllipsisPageUrl());
    }
    
    @Test
    public void testGetLimitUrl() {
        Map<String, String[]> params = new HashMap<>();
        params.put("p", new String[]{"3"});
        params.put("limit", new String[]{"50"});

        when(requestMock.getParameterMap()).thenReturn(params);
        when(pageMock.getTotalElements()).thenReturn((long)50);
        when(pCounterMock.getCurrentPage()).thenReturn(3);
        
        assertEquals(TEST_URL + "?p=3&limit=10", subject.getLimitUrl(10));
    }
    
    @Test
    public void testGetLimitUrlReturnsFirstPageIfGivenPageIsInvalid() {
        when(requestMock.getParameterMap()).thenReturn(new HashMap<>());
        when(pageMock.getTotalElements()).thenReturn((long)21);
        when(pCounterMock.getCurrentPage()).thenReturn(6);
        
        assertEquals(TEST_URL + "?p=1&limit=5", subject.getLimitUrl(5));
    }
}
