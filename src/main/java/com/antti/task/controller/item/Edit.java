package com.antti.task.controller.item;

import com.antti.task.bootstrap.message.MessageListMapper;
import javax.servlet.http.HttpServletRequest;
import com.antti.task.core.Translator;
import org.springframework.stereotype.Controller;
import com.antti.task.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.antti.task.core.message.Manager;

@Controller
public class Edit extends com.antti.task.controller.Item {

    private final Builder itemBuilder;
    private final Manager messageManager;
    private final Translator translator;
    
    @Autowired
    public Edit (
        Builder itemBuilder,
        Manager messageManager,
        Translator translator
    ) {
        this.itemBuilder = itemBuilder;
        this.messageManager = messageManager;
        this.translator = translator;
    }
    
    @GetMapping("/edit") 
    public String execute(HttpServletRequest request, Model model) {
        long itemId = getItemId(request);
        Item item = itemBuilder.build(request);
        
        if (itemId > 0 && item.getId() == null) {
            messageManager.addError(translator.trans("item.item_does_not_exist"));
            return "redirect:/";
        } else if (itemId == 0) {
            messageManager.addError(translator.trans("item.invalid_id", 0));
            return "redirect:/";
        }
        
        String resultPageTitle = itemId > 0 
                ? item.getTitle() + " (ID: " + itemId + ")" 
                : translator.trans("items");
        
        model.addAttribute("item", item);
        model.addAttribute("title", resultPageTitle);
        model.addAttribute("messages", MessageListMapper.map(messageManager.getMessages(true)));
        
        return "item-form";
    }
}
