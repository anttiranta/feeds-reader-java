package com.antti.task.core.api;

public class Filter {
    
    private final String field;
    private final String conditionType;
    private final String value;
    
    public Filter (String field, String conditionType, String value) {
        this.field = field;
        this.conditionType = conditionType;
        this.value = value;
    }
    
    public String getField() {
        return field;
    }
    
    public String getConditionType() {
        return conditionType;
    }
    
    public String getValue() {
        return value;
    }
}
