package com.antti.task.core.api;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaImpl implements SearchCriteria {

    private List<Filter> filters = new ArrayList<>();
    private List<SortOrder> sortOrders;
    private Integer pageSize;
    private Integer currentPage;
    
    @Override
    public List<Filter> getFilters() {
        return filters;
    }
    
    @Override
    public List<SortOrder> getSortOrders() {
        return sortOrders;
    }
    
    @Override
    public Integer getPageSize() {
        return pageSize;
    }
    
    @Override
    public Integer getCurrentPage() {
        return currentPage;
    }
    
    @Override
    public SearchCriteriaImpl setFilters(List<Filter> filters) {
        this.filters = filters;
        return this;
    }

    @Override
    public SearchCriteriaImpl setSortOrders(List<SortOrder> sortOrders) {
        this.sortOrders = sortOrders;
        return this;
    }

    @Override
    public SearchCriteriaImpl setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public SearchCriteriaImpl setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }
}
