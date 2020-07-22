package com.antti.task.service.item;

import com.antti.task.entity.Item;
import com.antti.task.repository.ItemRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.antti.task.service.item.Delete")
public class Delete {
    
    private final ItemRepository itemRepository;
    
    @Autowired
    public Delete(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    @Transactional
    public void delete(Item item) {
        itemRepository.delete(item);
    }
    
    @Transactional
    public void deleteById(long id) throws EntityNotFoundException {
        Optional<Item> value = itemRepository.findById(id);
        if (!value.isPresent()) {
             throw new EntityNotFoundException("This item doesn't exist.");
        }
        
        delete(value.get());
    }
}
