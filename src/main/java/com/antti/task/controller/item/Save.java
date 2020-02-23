package com.antti.task.controller.item;

import java.util.Optional;
import javax.validation.Valid;
import com.antti.task.core.Translator;
import com.antti.task.core.message.ManagerInterface;
import com.antti.task.entity.Item;
import com.antti.task.exception.LocalizedException;
import com.antti.task.repository.ItemRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Save extends com.antti.task.controller.Item {

    @Autowired
    private ManagerInterface messageManager;
    
    @Autowired
    private Translator translator;
    
    @Autowired
    private ItemRepository itemRepository;
    
    private final Logger logger = LogManager.getLogger("com.antti.task");

    /**
     * @param item
     * @param bindingResult
     * @param model
     * @return 
     * @todo: translate strings
     */
    @PostMapping("/save")
    public String execute(
            @Valid @ModelAttribute Item item,
            BindingResult bindingResult,
            Model model
    ) {
        boolean hasErrors = bindingResult.hasErrors();
        boolean isItemNew = item.getId() == null;

        if (!hasErrors) {
            try {
                Item existingItem = this.initItem(item);
                this.copyValues(item, existingItem);
                
                this.itemRepository.save(existingItem);
                this.messageManager.addSuccess(this.translator.trans("item.save_success"));
            } catch (Exception e) {
                this.messageManager.addError(this.translator.trans("item.save_exception"));
                this.logger.error(e.getMessage());
                
                hasErrors = true;
            }
        }
        
        if (hasErrors){
            if (isItemNew) {
                model.addAttribute("title", "New Item");
            } else {
                String resultPageTitle = item.getId() != null ? item.getTitle() + " (ID: " + item.getId() + ")" : "Items"; 
                model.addAttribute("title", resultPageTitle);
            }
            model.addAttribute("messages", this.messageManager.getMessages(true));
            
            return "item-form";
        }
        return "redirect:/";
    }
    
    private Item initItem(Item item) throws LocalizedException
    {
        Long id = item.getId();
        
        if (id != null) {
            Optional<com.antti.task.entity.Item> value = this.itemRepository.findById(id);
            if (!value.isPresent()) {
                throw new LocalizedException("This item doesn't exist."); // TODO: translate
            }
            return value.get();
        }
        
        return item;
    }
    
    /**
     * @param item
     * @param existingItem
     * @return
     * @TODO: move this part of code someplace else
     */
    private void copyValues(Item item, Item existingItem)
    {
        if (item == existingItem) {
            return;
        }
        
        existingItem.setTitle(item.getTitle());
        existingItem.setDescription(item.getDescription());
        existingItem.setLink(item.getLink());
        existingItem.setCategory(item.getCategory());
        existingItem.setComments(item.getComments());
        existingItem.setPubDate(item.getPubDate());
    }
}