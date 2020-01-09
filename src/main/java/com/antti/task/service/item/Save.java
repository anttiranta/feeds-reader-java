package com.antti.task.service.item;

import javax.transaction.Transactional;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import com.antti.task.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="nfq.asia.task.service.item.Save")
public class Save {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Transactional
    public Item save(Item item) throws CouldNotSaveException {
        try {
            this.itemRepository.save(item);
        } catch (Exception e) {
            throw new CouldNotSaveException(
                "Could not save item: " + e.getMessage()
            );
        }
        
        return item;
    }
}
