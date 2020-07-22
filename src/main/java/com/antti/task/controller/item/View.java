package com.antti.task.controller.item;

import com.antti.task.core.Translator;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.antti.task.core.message.Manager;

@Controller
public class View extends Edit {
    
    @Autowired
    public View (
        Builder itemBuilder,
        Manager messageManager,
        Translator translator
    ) {
        super(itemBuilder, messageManager, translator);
    }
    
    @Override
    @GetMapping("/view")
    public String execute(HttpServletRequest request, Model model) {
        String view = super.execute(request, model);
        
        if (isRedirect(view)) {
            return view;
        }
        return "item-view";
    }
    
    private boolean isRedirect(String str) {
        return str.startsWith("redirect:");
    }
}
