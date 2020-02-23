package com.antti.task.item.service.importing;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.antti.task.service.item.Save;
import com.antti.task.entity.Category;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import com.antti.task.item.service.importing.item.DateFormatter;
import com.antti.task.item.service.importing.item.FormatRequestBuilder;
import com.antti.task.service.category.GetByDomain;
import com.antti.task.util.integration.ItemImportHelper;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class Processor {

    @Autowired
    @Qualifier("com.antti.task.service.item.Save")
    private Save saveService;

    @Autowired
    private ItemImportHelper itemImportHelper;
    
    @Autowired
    private GetByDomain getByDomainService;

    @Transactional
    public boolean processAll(List<Map<String, Object>> itemsData) {
        boolean isSuccessful = true;
        for (Map<String, Object> itemData : itemsData) {
            isSuccessful = (this.processItem(itemData) && isSuccessful);
            // TODO: log failed items
        }

        return isSuccessful;
    }

    protected boolean processItem(Map<String, Object> itemData) {
        Item item = new Item();
        item.setTitle((String) itemData.get("title"));

        if (itemData.containsKey("description")) {
            item.setDescription((String) itemData.get("description"));
        }
        if (itemData.containsKey("link")) {
            item.setLink((String) itemData.get("link"));
        }
        if (itemData.containsKey("comments")) {
            item.setComments((String) itemData.get("comments"));
        }
        if (itemData.containsKey("pubDate")) {
            item.setPubDate(
                (new DateFormatter(
                    (new FormatRequestBuilder())
                            .fromFormat("EEE, dd MMM yyyy HH:mm:ss Z")
                            .toFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                            .usingLocale(new Locale(Locale.ENGLISH.getLanguage()))
                            .build()
                )).format((String)itemData.get("pubDate"))
            );
        }

        item = this.mapCategory(itemData, item);

        return this.saveItem(item);
    }

    protected boolean saveItem(Item item) {
        try {
            this.saveService.save(item);
            return true;
        } catch (CouldNotSaveException cnse) {
            return false;
        }
    }

    protected Item mapCategory(Map<String, Object> itemData, Item item) {
        if (itemData.containsKey("category_domain")) {
            String domain = this.itemImportHelper.normalizeUrl(
                (String)itemData.get("category_domain")
            );
            
            Category category = this.getByDomainService.execute(domain);
            if (category == null) {
                category = new Category();
                category.setDomain(domain);
            }
            category.setName((String)itemData.get("category"));

            item.setCategory(category);
        }
        return item;
    }
}
