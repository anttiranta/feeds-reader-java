package com.antti.task.repository;

import com.antti.task.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> 
{
    Category findByDomain(String domain);
}
