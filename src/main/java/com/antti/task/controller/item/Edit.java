package com.antti.task.controller.item;

import javax.servlet.http.HttpServletRequest;
import com.antti.task.core.Translator;
import com.antti.task.core.message.ManagerInterface;
import org.springframework.stereotype.Controller;
import com.antti.task.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Edit extends com.antti.task.controller.Item {

    @Autowired
    private Builder itemBuilder;
    
    @Autowired
    private ManagerInterface messageManager;
    
    @Autowired
    private Translator translator;
    
    @GetMapping("/edit") 
    public String execute(HttpServletRequest request, Model model)
    {
        long itemId = this.getItemId(request);
        Item item = this.itemBuilder.build(request);
        
        if (itemId > 0 && item.getId() == null) {
            this.messageManager.addError(this.translator.trans("item.item_does_not_exist"));
            return "redirect:/";
        } else if (itemId == 0) {
            this.messageManager.addError(this.translator.trans("item.invalid_id", 0));
            return "redirect:/";
        }
        
        model.addAttribute("item", item);
        
        String resultPageTitle = itemId > 0 ? item.getTitle() + " (ID: " + itemId + ")" : "Items"; // TODO: translate
        model.addAttribute("title", resultPageTitle);
        
        model.addAttribute("messages", this.messageManager.getMessages(true));
        
        return "item-form";
    }
}
