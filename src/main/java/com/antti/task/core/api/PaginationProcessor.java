package com.antti.task.core.api;

import javax.persistence.TypedQuery;

public class PaginationProcessor {
    
    public static final int MAX_ENTRIES_RETURN_AMOUNT = 200;
    
    public void process(SearchCriteria searchCriteria, TypedQuery<Object> query) {
        query.setMaxResults(getPageSize(searchCriteria));
        
        Integer currentPage = getCurPage(searchCriteria);
        if (currentPage != null) {
            query.setFirstResult(currentPage);
        }
    }
    
    private int getPageSize(SearchCriteria searchCriteria){
        Integer pageSize = searchCriteria.getPageSize();
        
        if (pageSize == null || pageSize < 1) {
            pageSize = 0;
        }
        if (pageSize > MAX_ENTRIES_RETURN_AMOUNT) {
            pageSize = MAX_ENTRIES_RETURN_AMOUNT;
        }
        
        return pageSize;
    }
    
    private Integer getCurPage(SearchCriteria searchCriteria){
        Integer result = null;
        Integer currentPage = searchCriteria.getCurrentPage();
        
        if (currentPage != null && currentPage > 0) {
            result = (currentPage -1) * getPageSize(searchCriteria);
        }
        return result;
    }
}