package com.antti.task.api.v1.category;

import java.util.List;
import com.antti.task.entity.Category;
import com.antti.task.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetAllCategories 
{
    @Autowired
    private CategoryRepository categoryRepository;
    
    @GetMapping("/api/v1/category/categories")
    public List<Category> getAll()
    {
        return this.categoryRepository.findAll();
    }
}
