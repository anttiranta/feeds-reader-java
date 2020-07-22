package com.antti.task.core.api;

import java.util.Arrays;

public class SortOrder {

    private String field;
    private String direction;
    
    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";
    
    public SortOrder () {
    }
    
    public SortOrder (String field, String direction) {
        if (direction != null) {
            validateDirection(direction);
            direction = normalizeDirectionInput(direction);
        }
        if (field != null) {
            validateField(field);
        }
        
        this.field = field;
        this.direction = direction;
    }
    
    public String getField() {
        return field;
    }

    public SortOrder setField(String field) {
        validateField(field);
        this.field = field;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public SortOrder setDirection(String direction) {
        validateDirection(direction);
        this.direction = normalizeDirectionInput(direction);
        return this;
    }
    
    private void validateDirection(String direction) {
        validateDirectionIsNotNull(direction);
        validateDirectionIsAscOrDesc(direction);
    }
    
    private void validateDirectionIsNotNull(String direction) {
        if (direction == null) {
            throw new IllegalArgumentException(
                 "The sort order has to be specified as a string, got null."
            );
        }
    }
    
    private void validateDirectionIsAscOrDesc(String direction) {
        String normalizedDirection = normalizeDirectionInput(direction);
        if (!(Arrays.asList(SORT_ASC, SORT_DESC).contains(normalizedDirection))) {
            throw new IllegalArgumentException(
                 "The sort order has to be specified as " + SORT_ASC + " " + 
                 "for ascending order or " + SORT_DESC + " for descending order."
            );
        }
    }
    
    private String normalizeDirectionInput(String direction) {
        return direction.toUpperCase();
    }
    
    private void validateField(String field) {
        boolean isValid = field != null && !field.isBlank();
        isValid = isValid && !field.matches("/[^a-z0-9\\_]") 
                && !field.matches("^.*[\\(\\)].*$");
        
        if (!isValid) {
            throw new IllegalArgumentException(
                 "Sort order field " + field + " contains restricted symbols"   
            );
        }
    }
}
