package com.antti.task.html;

import com.antti.task.core.PaginationCounter;
import com.antti.task.core.UrlBuilder;
import org.springframework.stereotype.Component;

@Component
public class PagerFactory {
    
    public Pager create(PaginationCounter paginationCounter) {
        return new Pager(paginationCounter);
    }
    
    public Pager create(PaginationCounter paginationCounter, UrlBuilder urlBuilder) {
        return new Pager(paginationCounter, urlBuilder);
    }
}
