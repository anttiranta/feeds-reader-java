package com.antti.task.controller.item;

import com.antti.task.core.Translator;
import javax.servlet.http.HttpServletRequest;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.antti.task.core.message.Manager;

@Controller
public class Delete extends com.antti.task.controller.Item {
    
    private final Manager messageManager;
    private final com.antti.task.service.item.Delete deleteService;
    private final Translator translator;
    
    @Autowired
    public Delete(
        Manager messageManager,
        @Qualifier("com.antti.task.service.item.Delete") com.antti.task.service.item.Delete deleteService,
        Translator translator
    ) {
        this.messageManager = messageManager;
        this.deleteService = deleteService;
        this.translator = translator;
    }

    @GetMapping("/delete")
    public String execute(Model model, HttpServletRequest request) {
        long itemId = getItemId(request);

        if (itemId > 0) {
            try {
                deleteService.deleteById(itemId);
                
                messageManager.addSuccess(translator.trans("item.delete_success"));
            } catch (EntityNotFoundException enfe) {
                messageManager.addException(enfe);
            } catch (Exception e) {
                messageManager.addError(translator.trans("item.delete_failed")); 
            }
        }

        return "redirect:/";
    }
}
