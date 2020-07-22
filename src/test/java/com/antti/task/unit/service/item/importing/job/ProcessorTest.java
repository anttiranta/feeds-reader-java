package com.antti.task.unit.service.item.importing.job;

import com.antti.task.dto.ItemDto;
import com.antti.task.entity.Category;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import com.antti.task.service.item.Save;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import com.antti.task.service.item.importing.job.Processor;
import com.antti.task.service.item.importing.job.processor.mapper.CategoryMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessorTest {
    
    private Save saveServiceMock;
    private CategoryMapper categoryMapperMock;
    private Processor subject;
    
    @Before
    public void setup() {
        saveServiceMock = mock(Save.class);
        categoryMapperMock = mock(CategoryMapper.class);
        subject = new Processor(saveServiceMock, categoryMapperMock);
    }
    
    @Test
    public void testProcessAll() throws Exception {
        List<ItemDto> sampleItems = getSampleItems();
        
        when(categoryMapperMock.map((ItemDto)notNull(), (Item)notNull())).thenReturn(new Category());
        
        assertTrue(subject.processAll(sampleItems));
        
        verify(categoryMapperMock, times(1)).map(eq(sampleItems.get(0)), (Item)notNull());
        verify(categoryMapperMock, times(1)).map(eq(sampleItems.get(1)), (Item)notNull());
        verify(saveServiceMock, times(2)).save((Item)notNull());
    }
    
    @Test
    public void testProcessAllReturnsFalseIfPersistingItemFails() throws Exception {
        List<ItemDto> sampleItems = getSampleItems();
        
        when(categoryMapperMock.map((ItemDto)notNull(), (Item)notNull())).thenReturn(new Category());
        doThrow(CouldNotSaveException.class).when(saveServiceMock)
            .save(any(Item.class));
        
        assertFalse(subject.processAll(sampleItems));
    }

    @Test
    public void testProcessAllReturnsTrueIfItemsListIsEmpty() throws Exception {
        assertTrue(subject.processAll(new ArrayList<>()));
        
        verify(categoryMapperMock, never()).map(any(ItemDto.class), any(Item.class));
        verify(saveServiceMock, never()).save(any(Item.class));
    }
    
    private List<ItemDto> getSampleItems() {
        List<ItemDto> sampleItems = new ArrayList<>();
        
        ItemDto itemMock = mock(ItemDto.class);
        when(itemMock.getTitle()).thenReturn("Recommended Desktop Feed Reader Software");
        when(itemMock.getDescription()).thenReturn("Short description 1");
        when(itemMock.getLink()).thenReturn("http://www.feedforall.com/feedforall-partners.htm");
        when(itemMock.getComments()).thenReturn("http://www.dmoz.com");
        when(itemMock.getPubDate()).thenReturn(new Date());
        
        sampleItems.add(itemMock);
        
        ItemDto otherItemMock = mock(ItemDto.class);
        when(otherItemMock.getTitle()).thenReturn("RSS Solutions for Restaurants");
        when(otherItemMock.getDescription()).thenReturn("Short description 2");
        when(otherItemMock.getLink()).thenReturn("http://www.feedforall.com/restaurant.htm");
        when(otherItemMock.getComments()).thenReturn("http://www.dmoz.com");
        when(otherItemMock.getPubDate()).thenReturn(new Date());
        
        sampleItems.add(otherItemMock);
        
        return sampleItems;
    }
}
