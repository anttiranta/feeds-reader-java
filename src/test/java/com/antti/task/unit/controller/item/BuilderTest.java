package com.antti.task.unit.controller.item;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import com.antti.task.controller.item.Builder;
import com.antti.task.entity.Item;
import com.antti.task.repository.ItemRepository;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.junit.Before;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BuilderTest {

    private ItemRepository itemRepository;
    private Builder builder;
    
    @Before
    public void setUp(){
        this.itemRepository = createMock(ItemRepository.class);
        this.builder = new Builder(this.itemRepository);
    }
    
    @Test
    public void testBuildWithProperId()
    {
        Long itemId = Long.valueOf(1);
        Item item = (new Item()).setId(itemId);
        
        HttpServletRequest request = createMock(HttpServletRequest.class);
        
        expect(request.getParameter("id")).andReturn(String.valueOf(itemId));
        expectLastCall().times(2);
        
        expect(this.itemRepository.findById(itemId))
            .andReturn(Optional.of(item));

        replay(request);
        replay(this.itemRepository);
        
        Item buildResult = this.builder.build(request);
        
        assertEquals(
            "Id of the built item is invalid.", 
            itemId, 
            buildResult.getId()
        );
        
        verify(this.itemRepository); 
        verify(request); 
    }
    
    @Test
    public void testBuildWithInvalidId()
    {
        Long itemId = Long.valueOf(2);
        Item item = new Item();
        
        HttpServletRequest request = createMock(HttpServletRequest.class);
        
        expect(request.getParameter("id")).andReturn(String.valueOf(itemId));
        expectLastCall().times(2);
        
        expect(this.itemRepository.findById(itemId))
            .andReturn(Optional.of(item));

        replay(request);
        replay(this.itemRepository);
        
        Item buildResult = this.builder.build(request);
        
        assertEquals(
            "Returned new item has id value even thought it is not supposed to have one.", 
            null, 
            buildResult.getId()
        );
        
        verify(this.itemRepository); 
        verify(request); 
    }
}
