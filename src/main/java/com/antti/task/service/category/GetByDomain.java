package com.antti.task.service.category;

import com.antti.task.entity.Category;
import com.antti.task.repository.CategoryRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetByDomain {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Transactional
    public Category execute(String domain) {
        return this.categoryRepository.findByDomain(domain);
    }
}
