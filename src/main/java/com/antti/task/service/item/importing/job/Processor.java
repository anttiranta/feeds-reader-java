package com.antti.task.service.item.importing.job;

import com.antti.task.dto.ItemDto;
import com.antti.task.entity.Category;
import java.util.List;
import com.antti.task.service.item.Save;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.antti.task.service.item.importing.job.processor.mapper.CategoryMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class Processor {

    private final Save saveService;
    private final CategoryMapper categoryMapper;
    private Logger logger;

    @Autowired
    public Processor(
        @Qualifier("com.antti.task.service.item.Save") Save saveService,
        CategoryMapper categoryMapper
    ) {
        this(saveService, categoryMapper, Logger.getLogger("com.antti.task"));
    }
    
    public Processor(
        @Qualifier("com.antti.task.service.item.Save") Save saveService,
        CategoryMapper categoryMapper,
        Logger logger
    ) {
        this.saveService = saveService;
        this.categoryMapper = categoryMapper;
        this.logger = logger;
    }
    
    public boolean processAll(List<ItemDto> items) {
        int failedCount = 0;
        
        logger.info("Started processing items");
        
        for (ItemDto itemDto : items) {
            boolean isSuccessful = processItem(itemDto);
            if (!isSuccessful) {
                failedCount++;
            }
        }
        
        if (failedCount > 0) {
            logger.error("Failed to process " + failedCount + " items");
        }

        return failedCount == 0;
    }

    private boolean processItem(ItemDto itemDto) {
        Item item = new Item();
        
        try {
            mapCategory(itemDto, item);
            assignItemValues(itemDto, item);
        } catch (Exception e) {
             logger.error("Category mapping failed. Title: " + itemDto.getTitle());
             return false;
        }

        return saveItem(item);
    }

    @Transactional
    private boolean saveItem(Item item) {
        try {
            saveService.save(item);
            return true;
        } catch (CouldNotSaveException cnse) {
            return false;
        }
    }

    private void mapCategory(ItemDto itemDto, Item item) {
        Category category = categoryMapper.map(itemDto, item);
        
        if (category != null) {
            item.setCategory(category);
        } else {
            logger.error("Category mapping failed. Title: " + itemDto.getTitle());
        }
    }
    
    private void assignItemValues(ItemDto from, Item to) {
        to.setTitle(from.getTitle())
             .setDescription(from.getDescription())
             .setLink(from.getLink())
             .setComments(from.getComments())
             .setPubDate(from.getPubDate());
    }
}
