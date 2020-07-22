package com.antti.task.integration.core.api;

import com.antti.task.core.api.SortOrder;
import com.antti.task.core.api.Filter;
import com.antti.task.core.api.FilterBuilder;
import org.junit.Test;
import static org.junit.Assert.*;
import com.antti.task.core.api.SearchCriteriaImpl;
import com.antti.task.core.api.SearchCriteriaBuilder;

public class SearchCriteriaBuilderTest {
    
    @Test
    public void testCreate() {
        SearchCriteriaBuilder subject = new SearchCriteriaBuilder(new FilterBuilder());
        
        SearchCriteriaImpl searchCriteria = subject.addFilters(new Filter("field", "=", "value"))
                .addSortOrder(new SortOrder("other_field", SortOrder.SORT_DESC))
                .setPageSize(50)
                .setCurrentPage(44)
                .create();
        
        assertSame(1, searchCriteria.getSortOrders().size());
        assertSame(1, searchCriteria.getFilters().size());
        assertSame(44, searchCriteria.getCurrentPage());
        assertSame(50, searchCriteria.getPageSize());
    }
}
