package com.antti.task.unit.entity;

import com.antti.task.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryTest {

    @Autowired
    private TestEntityManager entityManager;
    
    @Test(expected = Exception.class)
    public void testCategorySaveWithInvalidDomain()
    {
        Category category = new Category();
        category.setName("Computers/Software/Internet/Site Management/Content Management")
                .setDomain("www.dmoz.com");
        
        this.entityManager.persist(category);
        this.entityManager.flush();
    }
    
    @Test(expected = Exception.class)
    public void testSavingMultipleCategoriesWithSameDomain()
    {
        Category category = new Category();
        category.setName("Category 1")
                .setDomain("http://www.dmoz.com");
        
        Category category2 = new Category();
        category2.setName("Category 2")
                .setDomain("http://www.dmoz.com");
        
        this.entityManager.persist(category);
        this.entityManager.persist(category2);
        this.entityManager.flush();
    }
}
