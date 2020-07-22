package com.antti.task.controller.item;

import com.antti.task.core.Translator;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import com.antti.task.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.antti.task.core.message.Manager;

@Controller
public class NewAction extends com.antti.task.controller.Item {

    private final Builder itemBuilder;
    private final Manager messageManager;
    private final Translator translator;
    
    @Autowired
    public NewAction (
        Builder itemBuilder,
        Manager messageManager,
        Translator translator
    ) {
        this.itemBuilder = itemBuilder;
        this.messageManager = messageManager;
        this.translator = translator;
    }
    
    @GetMapping("/new") 
    public String execute(HttpServletRequest request, Model model) {
        Item item = itemBuilder.build(request);
        
        model.addAttribute("item", item);
        
        model.addAttribute("title", translator.trans("item.new_item"));
        model.addAttribute("messages", messageManager.getMessages(true));
        
        return "item-form";
    }
}
