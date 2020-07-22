package com.antti.task.controller.item;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import com.antti.task.entity.Item;
import com.antti.task.repository.ItemRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Builder {

    private ItemRepository itemRepository;
    private Logger logger;
    
    @Autowired
    public Builder(ItemRepository itemRepository) {
        this(itemRepository, LogManager.getLogger("com.antti.task"));
    }
    
    public Builder(
        ItemRepository itemRepository,
        Logger logger
    ) {
        this.itemRepository = itemRepository;
        this.logger = logger;
    }

    public Item build(HttpServletRequest request) {
        Long itemId = getItemId(request);

        if (itemId != null) {
            Optional<Item> value = itemRepository.findById(itemId);
            if (value.isPresent()) {
                return value.get();
            } else {
                logger.error("This item doesn't exist.");
                return new Item();
            }
        }

        return new Item();
    }

    private Long getItemId(HttpServletRequest request) {
        try {
            return request.getParameter("id") != null
                    ? Long.valueOf(request.getParameter("id"))
                    : null;
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
