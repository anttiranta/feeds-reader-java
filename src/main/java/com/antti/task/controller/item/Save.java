package com.antti.task.controller.item;

import javax.validation.Valid;
import com.antti.task.core.Translator;
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
import com.antti.task.bootstrap.message.MessageListMapper;
import com.antti.task.core.message.Manager;

@Controller
public class Save extends com.antti.task.controller.Item {

    private Manager messageManager;
    private Translator translator;
    private com.antti.task.service.item.Save saveService;
    private Logger logger;
    
    @Autowired
    public Save (
        Manager messageManager,
        Translator translator,
        @Qualifier("com.antti.task.service.item.Save") com.antti.task.service.item.Save saveService
    ){
        this(
            messageManager, 
            translator, 
            saveService, 
            LogManager.getLogger("com.antti.task")
        );
    }
    
    public Save (
        Manager messageManager,
        Translator translator,
        @Qualifier("com.antti.task.service.item.Save") com.antti.task.service.item.Save saveService,
        Logger logger
    ) {
        this.messageManager = messageManager;
        this.translator = translator;
        this.saveService = saveService;
        this.logger = logger;
    }

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
                saveService.save(item);
                
                messageManager.addSuccess(translator.trans("item.save_success"));
            } catch (EntityNotFoundException enfe) {
                messageManager.addError(translator.trans("item.item_does_not_exist"));
                hasErrors = true;
            } catch (Exception e) {
                messageManager.addError(translator.trans("item.save_exception"));
                logger.error(e.getMessage());
                
                hasErrors = true;
            }
        }
        
        if (hasErrors){
            if (isItemNew) {
                model.addAttribute("title", translator.trans("item.new_item"));
            } else {
                String resultPageTitle = item.getTitle() + " (ID: " + item.getId() + ")";
                model.addAttribute("title", resultPageTitle);
            }
            model.addAttribute("messages", MessageListMapper.map(messageManager.getMessages(true)));
            
            return "item-form";
        }
        return "redirect:/";
    }
}