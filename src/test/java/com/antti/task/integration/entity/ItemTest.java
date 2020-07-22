package com.antti.task.integration.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.antti.task.entity.Category;
import com.antti.task.entity.Item;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemTest {

    private final static String PUB_DATE_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    public void testItemSave(){
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants")
                .setPubDate(new Date());
        
        entityManager.persist(item);
        entityManager.flush();
        
        item = entityManager.find(Item.class, item.getId());
        
        assertEquals( 
            "RSS Solutions for Restaurants", 
            item.getTitle()
        );
        assertNotNull(item.getCreatedDate());
    }
    
    @Test
    public void testItemWithPubDateSave(){
        String pubDateStr = "2004-10-19T13:38:55.000-0400";
        Date pubDate = getPubDateByDateStr(pubDateStr);

        Item item = (new Item())
                .setTitle("RSS Solutions for Restaurants")
                .setPubDate(pubDate);
        
        Item savedItem = entityManager.persist(item);
        entityManager.flush();
        
        Long itemId = savedItem.getId();
        item = entityManager.find(Item.class, itemId);
        
        assertEquals(
            pubDate.getTime(), 
            item.getPubDate().getTime()
        );
    }
    
    @Test
    public void testItemWithCategorySave(){
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants")
                .setPubDate(new Date());
        
        Category category = new Category();
        category.setName("Computers/Software/Internet/Site Management/Content Management")
                .setDomain("http://www.dmoz.com");
        entityManager.persist(category);
        
        item.setCategory(category);
        
        entityManager.persist(item);
        entityManager.flush();
        
        Item loadedItem = entityManager.find(Item.class, item.getId());
        category = loadedItem.getCategory();

        assertEquals(
            "Computers/Software/Internet/Site Management/Content Management", 
            category.getName()
        );
        
        category.setName("Something else");
        
        entityManager.persist(loadedItem);
        entityManager.flush();
        
        loadedItem = entityManager.find(Item.class, item.getId());
        
        assertEquals(
            "Something else", 
            loadedItem.getCategory().getName()
        );
    }

    @Test
    public void testDeletingItemWillNotDeleteCategory(){
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants")
                .setPubDate(new Date());
        
        Category category = new Category();
        category.setName("Test category")
                .setDomain("http://www.dmoz.com");
        entityManager.persist(category);
        
        item.setCategory(category);
        
        entityManager.persist(item);
        entityManager.flush();
        
        Long categoryId = category.getId();
        Long itemId = item.getId();
        
        entityManager.remove(item);
        entityManager.flush();
        
        Category loadedCategory = entityManager.find(Category.class, categoryId);
        Item loadedItem = entityManager.find(Item.class, itemId);
        
        assertNull(loadedItem);
        assertEquals(
            "Test category", 
            loadedCategory.getName()
        );
    }
    
    @Test(expected = Exception.class)
    public void testInvalidItemSave() {
        Item item = (new Item())
                .setTitle("")
                .setLink("a link")
                .setComments("No comments");
        
        entityManager.persist(item);
        entityManager.flush();
    }
    
    @Test(expected = Exception.class)
    public void testItemSaveWithoutPubDate() {
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants");
        
        entityManager.persist(item);
        entityManager.flush();
    }
    
    private Date getPubDateByDateStr(String pubDateStr){
        DateFormat sdf = new SimpleDateFormat(PUB_DATE_DATE_TIME_FORMAT);
        sdf.setLenient(false);
        
        try {
            return sdf.parse(pubDateStr);
        }catch(ParseException pe){
            return null;
        }
    }
}