package com.antti.task.integration.service.item;

import com.antti.task.entity.Category;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import com.antti.task.repository.CategoryRepository;
import com.antti.task.repository.ItemRepository;
import com.antti.task.service.item.Copier;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Test;
import com.antti.task.service.item.Save;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SaveTest {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private Save subject;
    
    @Before
    public void setup() {
        subject = new Save(
            itemRepository,
            categoryRepository,
            new Copier()
        );
    }
    
    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
    }
    
    @Test
    public void testSaveWithNewItem() throws EntityNotFoundException, CouldNotSaveException {
        Calendar cal = Calendar.getInstance();
        cal.set(2004, 10, 26, 14, 03, 25);
        cal.setTimeZone(TimeZone.getTimeZone("-05:00"));
        cal.clear(Calendar.MILLISECOND);
        
        Date expectedPubDate = cal.getTime();
        
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants")
             .setPubDate(expectedPubDate);
        
        Category category = new Category();
        category.setName("Computers/Software/Internet/Site Management/Content Management")
             .setDomain("http://www.dmoz.com");
        item.setCategory(category);
        
        subject.save(item);
        
        Item loadedItem = entityManager.find(Item.class, item.getId());
        
        assertNotNull(loadedItem);
        assertEquals(expectedPubDate, loadedItem.getPubDate());
        assertNotNull(loadedItem.getCategory());
    }
    
    @Test
    public void testSaveWithExistingItem() throws EntityNotFoundException, CouldNotSaveException {
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants")
                .setPubDate(new Date());
        
        Category category = new Category();
        category.setName("Original name")
                .setDomain("http://www.dmoz.com");
        entityManager.persist(category);
        
        item.setCategory(category);
        
        entityManager.persist(item);
        entityManager.flush();
        
        item.setTitle("Something something");
        item.setCategory(null);
        
        subject.save(item);
        
        item = entityManager.find(Item.class, item.getId());
        
        assertEquals("Something something", item.getTitle());
        assertNull(item.getCategory());
    }
}
