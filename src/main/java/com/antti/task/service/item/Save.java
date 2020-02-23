package com.antti.task.service.item;

import javax.transaction.Transactional;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import com.antti.task.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.antti.task.service.item.Save")
public class Save {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public Item save(Item item) throws CouldNotSaveException {
        try {
            this.prepareItem(item);
            this.itemRepository.save(item);
        } catch (Exception e) {
            throw new CouldNotSaveException(
                    "Could not save item: " + e.getMessage()
            );
        }

        return item;
    }

    private void prepareItem(Item item) {
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
}
