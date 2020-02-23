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

    @Autowired
    private ItemRepository itemRepository;

    private final Logger logger
            = LogManager.getLogger("com.antti.task");

    /**
     * Default constructor
     */
    public Builder() {}

    public Builder(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item build(HttpServletRequest request) {
        Long itemId = this.getItemId(request);

        if (itemId != null) {
            Optional<Item> value = this.itemRepository.findById(itemId);
            if (value.isPresent()) {
                return value.get();
            } else {
                this.logger.error("This item doesn't exist.");
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
