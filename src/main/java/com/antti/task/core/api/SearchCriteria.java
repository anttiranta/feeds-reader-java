package com.antti.task.core.api;

import java.util.List;

public interface SearchCriteria {
    public List<Filter> getFilters();
    public SearchCriteriaImpl setFilters(List<Filter> filters);
    public List<SortOrder> getSortOrders();
    public SearchCriteriaImpl setSortOrders(List<SortOrder> sortOrders);
    public Integer getPageSize();
    public SearchCriteriaImpl setPageSize(Integer pageSize);
    public Integer getCurrentPage();
    public SearchCriteriaImpl setCurrentPage(Integer currentPage);
}
