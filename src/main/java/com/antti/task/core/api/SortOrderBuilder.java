package com.antti.task.core.api;

import org.springframework.stereotype.Component;

@Component
public class SortOrderBuilder {

    private String field;
    private String direction;
    
    public SortOrder create() {
        SortOrder sortOrder = new SortOrder(field, direction);
        clean();
        return sortOrder;
    }
    
    public SortOrderBuilder setField(String field) {
        this.field = field;
        return this;
    }

    public SortOrderBuilder setDirection(String direction) {
        this.direction = direction;
        return this;
    }
    
    public SortOrderBuilder setAscendingDirection() {
        this.direction = SortOrder.SORT_ASC;
        return this;
    }
    
    public SortOrderBuilder setDescendingDirection() {
        this.direction = SortOrder.SORT_DESC;
        return this;
    }

    private void clean() {
        field = null;
        direction = null;
    }
}
