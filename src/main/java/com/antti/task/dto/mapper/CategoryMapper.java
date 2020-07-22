package com.antti.task.dto.mapper;

import com.antti.task.dto.CategoryDto;
import com.antti.task.entity.Category;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component(value = "com.antti.task.dto.mapper.CategoryMapper")
public class CategoryMapper {
    
    public CategoryDto map(Category category) {
        CategoryDto catDto = new CategoryDto();
        catDto.setId(category.getId())
           .setName(category.getName())
           .setDomain(category.getDomain());
        
        return catDto;
    }
    
    public List<CategoryDto> map(List<Category> categories) {
        return categories.stream()
                .map(category -> map(category))
                .collect(Collectors.toList());
    }
}
