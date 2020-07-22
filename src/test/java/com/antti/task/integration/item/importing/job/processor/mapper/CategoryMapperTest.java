package com.antti.task.integration.item.importing.job.processor.mapper;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.antti.task.item.importing.Helper;
import com.antti.task.repository.CategoryRepository;
import com.antti.task.item.importing.processor.mapper.CategoryMapper;
import com.antti.task.dto.CategoryDto;
import com.antti.task.dto.ItemDto;
import com.antti.task.entity.Category;
import com.antti.task.entity.Item;

public class CategoryMapperTest {
    
    private CategoryRepository catRepositoryMock;
    private Helper helperMock;
    private CategoryMapper subject;
    
    @Before
    public void setup() {
        catRepositoryMock = mock(CategoryRepository.class);
        helperMock = mock(Helper.class);
        
        subject = new CategoryMapper(
            catRepositoryMock,
            helperMock
        );
    }
    
    @Test
    public void testMap() {
        String domain = "www.dmoz.com";
        
        String normalizedUrl = "http://" + domain;
        when(helperMock.normalizeUrl(domain)).thenReturn(normalizedUrl);
        
        when(catRepositoryMock.findByDomain(normalizedUrl))
             .thenReturn(null);
        
        Item itemMock = mock(Item.class);
        ItemDto itemDto = (new ItemDto()).setCategory(
            (new CategoryDto()).setName("Some category")
                 .setDomain(domain)
        );

        Category category = subject.map(itemDto, itemMock);
        
        assertEquals("Some category", category.getName());
        assertEquals(normalizedUrl, category.getDomain());
        
        verify(itemMock, times(1)).setCategory(category);
    }
    
    @Test
    public void testMapWillNotChangeCategoryValuesWhenExistingCategoryIsFound() {
        String domain = "www.google.com";
        
        String normalizedUrl = "http://" + domain;
        when(helperMock.normalizeUrl(domain)).thenReturn(normalizedUrl);
        
        Category existingCategory = (new Category())
            .setDomain("http://www.oldcategory.com")
            .setName("Existing category");
        
        when(catRepositoryMock.findByDomain(normalizedUrl))
             .thenReturn(existingCategory);
        
        Item itemMock = mock(Item.class);
        ItemDto itemDto = (new ItemDto()).setCategory(
            (new CategoryDto()).setName("Test 2")
                 .setDomain(domain)
        );

        Category category = subject.map(itemDto, itemMock);
        
        assertEquals("Existing category", category.getName());
        assertEquals("http://www.oldcategory.com", category.getDomain());
        
        verify(itemMock, times(1)).setCategory(existingCategory);
    }
    
    @Test
    public void testMapWithoutDomainValue() {
        Item itemMock = mock(Item.class);
        ItemDto itemDto = (new ItemDto()).setCategory(
            (new CategoryDto()).setName("Something")
        );
        
        Category category = subject.map(itemDto, itemMock);
        
        assertNull(category);
    }
}
