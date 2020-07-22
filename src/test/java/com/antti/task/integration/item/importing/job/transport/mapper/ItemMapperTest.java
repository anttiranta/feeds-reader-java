package com.antti.task.integration.item.importing.job.transport.mapper;

import com.antti.task.dto.ItemDto;
import com.antti.task.service.item.importing.job.transport.mapper.ItemMapper;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilderFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ItemMapperTest {
    
    private Document document;
    private ItemMapper subject;
    
    @Before
    public void setup() throws Exception {
        subject = new ItemMapper();
        document = (DocumentBuilderFactory.newInstance()).newDocumentBuilder()
                    .newDocument();
    }
    
    @Test
    public void testMapWithSimpleItem() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2004, 10, 26, 14, 03, 25);
        cal.setTimeZone(TimeZone.getTimeZone("-05:00"));
        cal.clear(Calendar.MILLISECOND);
        
        Date expectedDate = cal.getTime();
        
        Element itemData = createItemElementFromMap(new HashMap<String, String>() {{
            put("title", "Recommended Desktop Feed Reader Software");
            put("description", "Short description 1");
            put("link", "http://www.feedforall.com/feedforall-partners.htm");
            put("pubDate", "Tue, 26 Oct 2004 14:03:25 -0500");
            put("comments", "");
            put("category", "");
        }});
        
        ItemDto mappedItem = subject.map(itemData);
        
        assertEquals("Recommended Desktop Feed Reader Software", mappedItem.getTitle());
        assertEquals("Short description 1", mappedItem.getDescription());
        assertEquals("http://www.feedforall.com/feedforall-partners.htm", mappedItem.getLink());
        assertEquals(expectedDate, mappedItem.getPubDate());
        assertNull(mappedItem.getComments());
        assertNull(mappedItem.getCategory());
    }
    
    @Test
    public void testMapWithItemThatHasCategory () throws Exception {
        Element itemData = createItemElementFromMap(new HashMap<String, String>() {{
            put("title", "RSS Solutions for Restaurants");
            put("description", "Short description 2");
            put("link", "http://www.feedforall.com/restaurant.htm");
            put("pubDate", "Tue, 19 Oct 2004 11:09:11 -0400");
            put("comments", "http://www.feedforall.com/forum");
        }});
        itemData.appendChild(createCategoryElement("Some category", "www.dmoz.com"));
        
        ItemDto mappedItem = subject.map(itemData);
        
        assertEquals("http://www.feedforall.com/forum", mappedItem.getComments());
        assertEquals("Some category", mappedItem.getCategory().getName());
        assertEquals("www.dmoz.com", mappedItem.getCategory().getDomain());
    }
    
    @Test(expected = Exception.class)
    public void testMapWithItemMissingTitleAndPubDate() throws Exception {
        Element itemData = createItemElementFromMap(new HashMap<String, String>() {{
            put("description", "Short description 1");
            put("link", "http://www.feedforall.com/restaurant.htm");
            put("pubDate", "");
        }});
        
        subject.map(itemData);
    }
    
    private Element createItemElementFromMap(Map<String, String> values) throws Exception {
        Element item = document.createElement("item");
        
        for (Map.Entry<String,String> entry : values.entrySet()) {
            Element element = document.createElement(entry.getKey());
            element.appendChild(document.createTextNode(entry.getValue()));
            
            item.appendChild(element);
        }
        return item;
    }
    
    private Element createCategoryElement(String name, String domain) {
        Element category = document.createElement("category");
        category.appendChild(document.createTextNode(name));
        
        Attr attr = document.createAttribute("domain");
        attr.setValue(domain);
        category.setAttributeNode(attr);
        
        return category;
    }
}
