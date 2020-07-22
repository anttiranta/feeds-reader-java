package com.antti.task.core.api;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class FilterBuilder {
    
    private static final String KEY_FIELD = "field";
    private static final String KEY_VALUE = "value";
    private static final String KEY_CONDITION_TYPE = "condition_type";
    
    private final Map<String, String> data = new HashMap<>();
    
    public Filter create() {
        Filter filter = new Filter(
                data.get(KEY_FIELD), 
                data.get(KEY_CONDITION_TYPE), 
                data.get(KEY_VALUE)
        );
        data.clear();
        return filter;
    }
    
    public FilterBuilder setField(String field) {
        data.put(KEY_FIELD, field);
        return this;
    }
    
    public FilterBuilder setValue(String value) {
        data.put(KEY_VALUE, value);
        return this;
    }
    
    public FilterBuilder setConditionType(String conditionType) {
        validateConditionTypeIsNotBlank(conditionType);
        data.put(KEY_CONDITION_TYPE, conditionType);
        return this;
    }
    
    private void validateConditionTypeIsNotBlank(String conditionType) {
        if (conditionType.isBlank()) {
            throw new IllegalArgumentException(
                 "The condition tyoe has to be specified as a string, got empty string."
            );
        }
    }
}
