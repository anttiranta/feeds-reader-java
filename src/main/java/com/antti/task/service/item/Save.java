package com.antti.task.service.item;

import javax.transaction.Transactional;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import com.antti.task.repository.CategoryRepository;
import com.antti.task.repository.ItemRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.antti.task.service.item.Save")
public class Save {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final Copier copier;
    
    @Autowired
    public Save(
        ItemRepository itemRepository,
        CategoryRepository categoryRepository,
        Copier copier
    ) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.copier = copier;
    }

    @Transactional
    public Item save(Item item) throws EntityNotFoundException, CouldNotSaveException {
        boolean isNewItem = item.getId() == null;
        Item existingItem = initItem(item);

        try {
            if (!isNewItem && existingItem.getId() != null) {
                copier.copy(item, existingItem);
            }
            
            saveCategory(existingItem);
            
            itemRepository.save(existingItem);
        } catch (Exception e) {
            throw new CouldNotSaveException(
                 "Could not save item: " + e.getMessage()
            );
        }

        return existingItem;
    }

    private Item initItem(Item item) throws EntityNotFoundException {
        Long id = item.getId();
        
        if (id != null) {
            Optional<com.antti.task.entity.Item> value = itemRepository.findById(id);
            if (!value.isPresent()) {
                throw new EntityNotFoundException("This item doesn't exist.");
            }
            return value.get();
        }

        return item;
    }
    
    private void saveCategory(Item item) {
        if (item.getCategory() != null) {
            boolean isNewCategory = item.getCategory().getId() == null;
            
            if (isNewCategory) {
                categoryRepository.save(item.getCategory());
            }
        }
    }
}
