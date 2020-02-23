package com.antti.task.unit.entity;

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
    public void testItemSave()
    {
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants");
        
        Item savedItem = this.entityManager.persist(item);
        this.entityManager.flush();
        
        Long itemId = savedItem.getId();
        
        item = this.entityManager.find(Item.class, itemId);
        
        assertEquals(
            "Item title is invalid.", 
            "RSS Solutions for Restaurants", 
            item.getTitle()
        );
    }
    
    @Test
    public void testItemWithPubDateSave()
    {
        String pubDateStr = "2004-10-19T13:38:55.000-0400";
        Date pubDate = this.getPubDateByDateStr(pubDateStr);

        Item item = (new Item())
                .setTitle("RSS Solutions for Restaurants")
                .setPubDate(pubDate);
        
        Item savedItem = this.entityManager.persist(item);
        this.entityManager.flush();
        
        Long itemId = savedItem.getId();
        item = this.entityManager.find(Item.class, itemId);
        
        assertEquals(
            "Item published date is invalid.", 
            pubDate.getTime(), 
            item.getPubDate().getTime()
        );
    }
    
    @Test
    public void testItemWithCategorySave()
    {
        Item item = new Item();
        item.setTitle("RSS Solutions for Restaurants");
        
        Category category = new Category();
        category.setName("Computers/Software/Internet/Site Management/Content Management")
                .setDomain("http://www.dmoz.com");
        
        item.setCategory(category);
        
        Item savedItem = this.entityManager.persist(item);
        this.entityManager.flush();
        
        Long itemId = savedItem.getId();
        
        Item loadedItem = this.entityManager.find(Item.class, itemId);
        category = loadedItem.getCategory();

        assertEquals(
            "Item category is invalid.", 
            "Computers/Software/Internet/Site Management/Content Management", 
            category.getName()
        );
    }
    
    private Date getPubDateByDateStr(String pubDateStr)
    {
        DateFormat sdf = new SimpleDateFormat(PUB_DATE_DATE_TIME_FORMAT);
        sdf.setLenient(false);
        
        try {
            return sdf.parse(pubDateStr);
        }catch(ParseException pe){
            return null;
        }
    }
}