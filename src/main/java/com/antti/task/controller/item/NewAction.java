package com.antti.task.controller.item;

import javax.servlet.http.HttpServletRequest;
import com.antti.task.core.message.ManagerInterface;
import org.springframework.stereotype.Controller;
import com.antti.task.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewAction extends com.antti.task.controller.Item {

    @Autowired
    Builder itemBuilder;
    
    @Autowired
    private ManagerInterface messageManager;
    
    @GetMapping("/new") 
    public String execute(HttpServletRequest request, Model model)
    {
        Item item = this.itemBuilder.build(request);
        
        model.addAttribute("item", item);
        
        model.addAttribute("title", "New Item"); // TODO: translate
        model.addAttribute("messages", this.messageManager.getMessages(true));
        
        return "item-form";
    }
}
