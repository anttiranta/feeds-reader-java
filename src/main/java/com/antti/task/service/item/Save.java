package com.antti.task.service.item;

import javax.transaction.Transactional;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import com.antti.task.repository.ItemRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.antti.task.service.item.Save")
public class Save {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public Item save(Item item) throws EntityNotFoundException, CouldNotSaveException {
        Item existingItem = this.initItem(item);
        
        this.copyValues(item, existingItem);

        try {
            this.prepareItemForSave(existingItem);

            this.itemRepository.save(existingItem);
        } catch (Exception e) {
            throw new CouldNotSaveException(
                    "Could not save item: " + e.getMessage()
            );
        }

        return item;
    }

    private Item initItem(Item item) throws EntityNotFoundException {
        Long id = item.getId();

        if (id != null) {
            Optional<com.antti.task.entity.Item> value = this.itemRepository.findById(id);
            if (!value.isPresent()) {
                throw new EntityNotFoundException("This item doesn't exist.");
            }
            return value.get();
        }

        return item;
    }

    private void prepareItemForSave(Item item) {
        if (item.getCategory() != null) {
            if (item.getCategory().getItems().contains(item)) {
                item.getCategory().getItems().set(
                        item.getCategory().getItems().indexOf(item), item
                );
            } else {
                item.getCategory().getItems().add(item);
            }
        }
    }

    private void copyValues(Item item, Item existingItem) {
        if (item.getId() == null && existingItem.getId() == null) {
            return;
        }
        
        existingItem.setTitle(item.getTitle());
        existingItem.setDescription(item.getDescription());
        existingItem.setLink(item.getLink());
        existingItem.setComments(item.getComments());
        existingItem.setPubDate(item.getPubDate());
        existingItem.setCategory(item.getCategory());
    }
}
