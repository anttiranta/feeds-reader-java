package com.antti.task.controller.item;

import javax.validation.Valid;
import com.antti.task.core.Translator;
import com.antti.task.core.message.ManagerInterface;
import com.antti.task.entity.Item;
import javax.persistence.EntityNotFoundException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("com.antti.task.service.item.Save")
    private com.antti.task.service.item.Save saveService;
    
    private final Logger logger = LogManager.getLogger("com.antti.task");

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
                this.saveService.save(item);
                
                this.messageManager.addSuccess(this.translator.trans("item.save_success"));
            } catch (EntityNotFoundException enfe) {
                this.messageManager.addError(this.translator.trans("item.item_does_not_exist"));
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
}