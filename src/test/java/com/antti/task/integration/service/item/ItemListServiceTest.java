package com.antti.task.integration.service.item;

import com.antti.task.core.api.FilterBuilder;
import com.antti.task.core.api.PageRequestBuilder;
import com.antti.task.core.api.SearchCriteriaBuilder;
import com.antti.task.entity.Category;
import com.antti.task.entity.Item;
import com.antti.task.repository.CategoryRepository;
import com.antti.task.repository.ItemRepository;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Test;
import com.antti.task.service.item.ItemListService;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemListServiceTest {
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    private SearchCriteriaBuilder searchCriteriaBuilder;
    private ItemListService subject;
    
    @Before
    public void setup() {
        searchCriteriaBuilder = new SearchCriteriaBuilder(new FilterBuilder());
        subject = new ItemListService(
            entityManager,
            new PageRequestBuilder()
        );
        
        addSampleItemsAndCategories();
    }
    
    @After
    public void tearDownClass() throws Exception {
        itemRepository.deleteAll();
    }
    
    @Test
    public void testGetItemList() {
        searchCriteriaBuilder.setFilters(new ArrayList<>())
                .setPageSize(5)
                .setCurrentPage(1);
        
        Page<Item> page1 = subject.getItemList(searchCriteriaBuilder.create());
        assertSame(5, page1.getContent().size());
        
        searchCriteriaBuilder.setFilters(new ArrayList<>())
                .setPageSize(5)
                .setCurrentPage(2);
        
        Page<Item> page2 = subject.getItemList(searchCriteriaBuilder.create());
        assertSame(1, page2.getContent().size());
    }
    
    @Test
    public void testGetItemListWithFilters() {
        searchCriteriaBuilder.setPageSize(5)
                .setCurrentPage(1)
                .addFilters("i.title", "RSS Solutions for Gover", "like");
        
        Page<Item> page = subject.getItemList(searchCriteriaBuilder.create());
        assertSame(1, page.getContent().size());
        
        searchCriteriaBuilder.setPageSize(5)
                .setCurrentPage(1)
                .addFilters("c.name", "Computers/Software", "like");
        
        page = subject.getItemList(searchCriteriaBuilder.create());
        assertSame(5, page.getContent().size());
    }
    
    private void addSampleItemsAndCategories() {
        Category category = new Category();
        category.setName("Computers/Software/Internet/Site " 
                + " Management/Content Management")
                .setDomain("http://www.dmoz.com");

        categoryRepository.save(category);

        Item item = (new Item())
                .setTitle("RSS Solutions for Restaurants")
                .setDescription("<b>FeedForAll </b>helps Restaurant's" 
                        + " communicate with customers.")
                .setLink("http://www.feedforall.com/restaurant.htm")
                .setCategory(category)
                .setComments("http://www.feedforall.com/forum")
                .setPubDate(new Date());

        itemRepository.save(item);

        item = (new Item())
                .setTitle("RSS Solutions for Schools and Colleges")
                .setDescription("FeedForAll helps Educational Institutions " 
                        + " communicate with students about school wide " 
                        + " activities, events, and schedules.")
                .setLink("http://www.feedforall.com/schools.htm")
                .setCategory(category)
                .setComments("http://www.feedforall.com/forum")
                .setPubDate(new Date());

        itemRepository.save(item);

        item = (new Item())
                .setTitle("RSS Solutions for Computer Service Companies")
                .setDescription("FeedForAll helps Computer Service Companies " 
                        + " communicate with clients about cyber security " 
                        + " and related issues.")
                .setLink("http://www.feedforall.com/computer-service.htm")
                .setCategory(category)
                .setComments("http://www.feedforall.com/forum")
                .setPubDate(new Date());

        itemRepository.save(item);

        item = (new Item())
                .setTitle("RSS Solutions for Governments")
                .setDescription("FeedForAll helps Governments communicate "
                     +"with the general public about positions on various issues, " 
                     + "and keep the community aware of changes in important legislative issues. " 
                     + "<b><i><br> </b></i><br> RSS uses Include:<br> <i>" 
                     + "Legislative Calendar<br> Votes<br> Bulletins</i>"
                ).setLink("http://www.feedforall.com/government.htm")
                .setCategory(category)
                .setComments("http://www.feedforall.com/forum")
                .setPubDate(new Date());

        itemRepository.save(item);

        item = (new Item())
                .setTitle("RSS Solutions for Politicians")
                .setDescription("FeedForAll helps Politicians communicate " 
                     + "with the general public about positions on various issues."
                ).setLink("http://www.feedforall.com/computer-service.htm")
                .setCategory(category)
                .setComments("http://www.feedforall.com/forum")
                .setPubDate(new Date());

        itemRepository.save(item);

        item = (new Item())
                .setTitle("RSS Solutions for Meteorologists")
                .setDescription("FeedForAll helps Meteorologists communicate with "
                     + "the general public about storm warnings and weather alerts."
                ).setLink("http://www.feedforall.com/computer-service.htm")
                .setCategory(null)
                .setComments("http://www.feedforall.com/forum")
                .setPubDate(new Date());

        itemRepository.save(item);
    }
}
