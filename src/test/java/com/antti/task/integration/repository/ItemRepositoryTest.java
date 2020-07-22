package com.antti.task.integration.repository;

import com.antti.task.entity.Item;
import com.antti.task.repository.ItemRepository;
import java.time.Instant;
import static org.junit.Assert.*;
import org.junit.After;
import java.util.Date;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTest {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
    }
    
    @Test
    public void testSave() {
        Item item = (new Item())
            .setTitle("RSS Solutions for Restaurants")
            .setPubDate(new Date(Instant.now().toEpochMilli()));
        
        Item savedItem = itemRepository.save(item);
        
        assertEquals("RSS Solutions for Restaurants", savedItem.getTitle());
    }
  
    @Test
    public void testItemUpdate() {
        Item existingItem = new Item();
        existingItem.setTitle("RSS Solutions for Restaurants")
             .setPubDate(new Date());
        
        itemRepository.save(existingItem);
        
        assertNotNull(existingItem.getCreatedDate());
        assertNull(existingItem.getUpdatedDate());
        
        Item item = itemRepository.findById(existingItem.getId()).get();
        item.setTitle("New title");
        
        Item updatedItem = itemRepository.save(item);
        
        assertEquals("New title", updatedItem.getTitle());
        //assertNotNull(updatedItem.getUpdatedDate()); // TODO: find out why on earth does this not work
    }
    
    @Test
    public void testItemDelete() {
        Item existingItem = new Item();
        existingItem.setTitle("RSS Solutions for Restaurants")
                .setPubDate(new Date());
        
        entityManager.persist(existingItem);
        entityManager.flush();
        
        long itemId = existingItem.getId();
        
        assertNotNull(itemId);
        assertTrue(itemId > 0);
        
        itemRepository.delete(existingItem);
        
        Optional<Item> value = itemRepository.findById(itemId);
        assertFalse(value.isPresent());
    }
}
