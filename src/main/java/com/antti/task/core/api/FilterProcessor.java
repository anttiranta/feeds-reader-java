package com.antti.task.core.api;

import java.util.List;
import javax.persistence.TypedQuery;

public class FilterProcessor {

    public void process(SearchCriteria searchCriteria, TypedQuery<Object> query) {
        addFiltersToQuery(searchCriteria, query);
    }
    
    public void process(SearchCriteria searchCriteria, StringBuilder hqlB) {
        addFiltersToHql(searchCriteria, hqlB);
    }
    
    private void addFiltersToQuery(SearchCriteria searchCriteria, TypedQuery<Object> query) {
        List<Filter> filters = searchCriteria.getFilters();

        for (Filter filter : filters) {
            String field = filter.getField().replace(".", "_");

            if (filter.getConditionType().equals("like")) {
                query.setParameter(field, filter.getValue() + "%");
                continue;
            }
            query.setParameter(field, filter.getValue());
        }
    }

    private void addFiltersToHql(SearchCriteria searchCriteria, StringBuilder hqlB) {
        List<Filter> filters = searchCriteria.getFilters();

        String[] parts = hqlB.toString().split(" where ");
        if (parts.length < 2) {
            hqlB.append("where 1=1");
        }

        for (Filter filter : filters) {
            hqlB.append(" and ");

            String condition = filter.getConditionType() != null ? filter.getConditionType() : "=";
            String field = filter.getField().replace(".", "_");
            String sql = filter.getField() + " " + condition + " :" + field;

            hqlB.append(sql);
        }
    }
}
