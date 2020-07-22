package com.antti.task.item.importing.processor.mapper;

import com.antti.task.dto.CategoryDto;
import com.antti.task.dto.ItemDto;
import com.antti.task.entity.Category;
import com.antti.task.entity.Item;
import com.antti.task.item.importing.Helper;
import com.antti.task.repository.CategoryRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    
    private final CategoryRepository categoryRepository;
    private final Helper itemImportHelper;
    
    @Autowired
    public CategoryMapper(
        CategoryRepository categoryRepository,
        Helper itemImportHelper
    ) {
        this.categoryRepository = categoryRepository;
        this.itemImportHelper = itemImportHelper;
    }
    
    @Transactional
    public Category map(ItemDto itemDto, Item item) {
        CategoryDto categoryDto = itemDto.getCategory();
        
        if (categoryDto != null && isValidDomain(categoryDto.getDomain())) {
            String domain = itemImportHelper.normalizeUrl(categoryDto.getDomain());
            categoryDto.setDomain(domain);
            
            Category category = categoryRepository.findByDomain(domain);
            if (category == null) {
                category = prepareNewCategory(categoryDto);
            }
            
            item.setCategory(category);
            
            return category;
        }
        
        return null;
    }
    
    private boolean isValidDomain(String domain) {
        return domain != null && !domain.isEmpty();
    }
    
    private Category prepareNewCategory(CategoryDto categoryDto) {
        Category category = new Category();
        assignCategoryValues(categoryDto, category);
        return category;
    }
    
    private void assignCategoryValues(CategoryDto from, Category to) {
        to.setName(from.getName())
             .setDomain(from.getDomain());
    }
}
