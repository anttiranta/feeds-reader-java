package com.antti.task.core.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchCriteriaBuilder {
    
    private final FilterBuilder filterBuilder;
    private List<Filter> filters = new ArrayList<>();
    private List<SortOrder> sortOrders = new ArrayList<>();
    private Integer pageSize;
    private Integer currentPage;
    
    @Autowired
    public SearchCriteriaBuilder(FilterBuilder filterBuilder) {
        this.filterBuilder = filterBuilder;
    }
    
    public SearchCriteriaImpl create() {
        SearchCriteriaImpl sc = (new SearchCriteriaImpl())
                .setFilters(filters)
                .setSortOrders(sortOrders)
                .setPageSize(pageSize)
                .setCurrentPage(currentPage);
        
        clean();
        return sc;
    }
    
    public SearchCriteriaBuilder addFilters(Filter filter) {
        filters.add(filter);
        return this;
    }
    
    public SearchCriteriaBuilder addFilters(String field, String value, String conditionType) {
        addFilters(
            filterBuilder.setField(field)
                .setValue(value)
                .setConditionType(conditionType)
                .create()
        );
        return this;
    }
    
    public SearchCriteriaBuilder addFilters(String field, String value) {
        return addFilters(field, value, "=");
    }
    
    public SearchCriteriaBuilder setFilters(List<Filter> filters) {
        this.filters = filters;
        return this;
    }
    
    public SearchCriteriaBuilder addSortOrder(SortOrder sortOrder) {
        sortOrders.add(sortOrder);
        return this;
    }
    
    public SearchCriteriaBuilder setSortOrders(List<SortOrder> sortOrders) {
        this.sortOrders = sortOrders;
        return this;
    }
    
    public SearchCriteriaBuilder setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }
    
    public SearchCriteriaBuilder setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }
    
    private void clean() {
        filters = new ArrayList<>();
        sortOrders = new ArrayList<>();
        pageSize = null;
        currentPage = null;
    }
}
    
