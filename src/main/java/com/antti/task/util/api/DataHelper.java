package com.antti.task.util.api;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DataHelper 
{
    /**
     * Parse filters and format them to be applicable SQL filtration
     *
     * @param filters
     * @param fieldsMap Map of field names in format: 'field_name_in_filter' => 'field_name_in_db'
     * @return Map<String,String>
     */
    public Map<String,String> parseFilters(
            Map<String,String> filters, 
            Map<String,String> fieldsMap
    ){
        // Apply fields mapping
        if (fieldsMap != null && !fieldsMap.isEmpty()) {
            for(Map.Entry<String, String> entry : filters.entrySet()){
                String field = entry.getKey();
                String value = entry.getValue();
                
                if(fieldsMap.containsKey(field)) {
                    filters.remove(field);
                    String mappedField = fieldsMap.get(field);
                    filters.put(mappedField, value);
                }
            }
        }
        return filters;
    }
    
    public Map<String,String> parseFilters(
            Map<String,String> filters
    ){
        return this.parseFilters(filters, null);
    }
}
