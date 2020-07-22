package com.antti.task.service.item;

import com.antti.task.entity.Item;
import org.springframework.stereotype.Service;

@Service
public class Copier {
    
    public void copy(Item from, Item to) {
        to.setTitle(from.getTitle());
        to.setDescription(from.getDescription());
        to.setLink(from.getLink());
        to.setComments(from.getComments());
        to.setPubDate(from.getPubDate());
        to.setCategory(from.getCategory());
    }
}
