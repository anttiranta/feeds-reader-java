package com.antti.task.api.v1.category;

import com.antti.task.dto.CategoryDto;
import java.util.List;
import com.antti.task.dto.mapper.CategoryMapper;
import com.antti.task.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetAllCategories 
{
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public GetAllCategories(
        CategoryRepository categoryRepository,
        @Qualifier("com.antti.task.dto.mapper.CategoryMapper") CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    
    @GetMapping("/api/v1/category/categories")
    public List<CategoryDto> getAll(){
        return categoryMapper.map(categoryRepository.findAll());
    }
}
