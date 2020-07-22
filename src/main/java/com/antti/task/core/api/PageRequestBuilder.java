package com.antti.task.core.api;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageRequestBuilder {
    
    private final Map<String, Sort.Direction> map = new HashMap<String, Sort.Direction>() {
        {
            put(SortOrder.SORT_DESC, Sort.Direction.DESC);
            put(SortOrder.SORT_ASC, Sort.Direction.ASC);
        }
    };
    
    public PageRequest build(SearchCriteria searchCriteria) {
        if (!isValid(searchCriteria)) {
            throw new IllegalArgumentException("Given search criteria is not valid.");
        }
        
        if (!searchCriteria.getSortOrders().isEmpty()) {
            SortOrder sortOrder = searchCriteria.getSortOrders().get(0);
            
            if (sortOrder.getField() != null && sortOrder.getDirection() != null) {
                return PageRequest.of(
                         mapPage(searchCriteria.getCurrentPage()), 
                        searchCriteria.getPageSize(), 
                        map.get(sortOrder.getDirection()), 
                        sortOrder.getField());
            }
        }
        
        return PageRequest.of(
            mapPage(searchCriteria.getCurrentPage()),
            searchCriteria.getPageSize()
        );
    }
    
    private int mapPage(int page) {
        // PageRequest pages begins from zero so we substract one
        return page > 0 ? page -1 : page;
    }
    
    private boolean isValid(SearchCriteria searchCriteria) {
        return searchCriteria.getCurrentPage() != null 
                && searchCriteria.getPageSize() != null;
    }
}
