package com.antti.task.controller.item;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import com.antti.task.core.message.ManagerInterface;
import com.antti.task.entity.Item;
import com.antti.task.exception.LocalizedException;
import com.antti.task.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Delete extends com.antti.task.controller.Item {
    
    @Autowired
    private ManagerInterface messageManager;
    
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/delete")
    public String execute(Model model, HttpServletRequest request) {
        long itemId = this.getItemId(request);

        if (itemId > 0) {
            try {
                Optional<Item> value = this.itemRepository.findById(itemId);
                if (!value.isPresent()) {
                    throw new LocalizedException("This item doesn't exist.");
                }

                this.itemRepository.delete(value.get());
                
                this.messageManager.addSuccess("Item was successfully deleted.");
            } catch (LocalizedException le) {
                this.messageManager.addException(le);
            } catch (Exception e) {
                this.messageManager.addError("Something went wrong while trying to delete the item."); 
            }
        }

        return "redirect:/";
    }
}
