package com.antti.task.unit.controller.item;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import static org.junit.Assert.*;
import com.antti.task.controller.item.Builder;
import com.antti.task.entity.Item;
import com.antti.task.repository.ItemRepository;
import org.junit.Before;
import static org.mockito.Mockito.*;

public class BuilderTest {

    private ItemRepository itemRepository;
    private HttpServletRequest request;
    private Builder builder;
    
    @Before
    public void setUp(){
        itemRepository = mock(ItemRepository.class);
        request = mock(HttpServletRequest.class);
        builder = new Builder(itemRepository);
    }
    
    @Test
    public void testBuildWithProperId() {
        Long itemId = Long.valueOf(1);
        Item item = (new Item()).setId(itemId);

        when(request.getParameter("id")).thenReturn(String.valueOf(itemId));
        
        when(itemRepository.findById(itemId))
            .thenReturn(Optional.of(item));
        
        Item buildResult = builder.build(request);
        
        assertEquals(
            itemId, 
            buildResult.getId()
        );
        
        verify(request, times(2)).getParameter("id");
    }
    
    @Test
    public void testBuildWithInvalidId() {
        Long itemId = Long.valueOf(2);
        Item item = new Item();
        
        when(request.getParameter("id")).thenReturn(String.valueOf(itemId));
        
        when(itemRepository.findById(itemId))
            .thenReturn(Optional.of(item));
        
        Item buildResult = builder.build(request);
        
        assertEquals(
            null, 
            buildResult.getId()
        );
        
        verify(request, times(2)).getParameter("id");
    }
}
