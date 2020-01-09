package com.antti.task.controller.item;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import com.antti.task.controller.item.index.search.FilterBuilder;
import com.antti.task.core.message.ManagerInterface;
import com.antti.task.entity.Item;
import com.antti.task.html.Pager;
import com.antti.task.service.item.ItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.antti.task.core.Translator;

@Controller
public class Index {

    @Autowired
    private ItemList itemList;
    
    @Autowired
    private ManagerInterface messageManager;
    
    @Autowired
    private FilterBuilder filterBuilder;
    
    @Autowired
    private Translator translator;
    
    @RequestMapping("/")
    public String execute(Model model, HttpServletRequest request) {
        Pager pager = this.getPager(request);
        
        model.addAttribute("pager", pager); 
        model.addAttribute("items", pager.getPage().getContent());
        
        model.addAttribute("messages", this.messageManager.getMessages(true));
        model.addAttribute("title", this.translator.trans("item.all_items"));
        
        return "index";
    }
    
    private Pager getPager(HttpServletRequest request)
    {
        Pager<Item> pager = new Pager<>(request);
        
        String orderBy = request.getParameter("order_by") != null ? request.getParameter("order_by") : null;
        Sort.Direction order = request.getParameter("order") != null 
                && Arrays.asList(new String[]{"ASC", "DESC"}).contains(request.getParameter("order")) 
                ? Sort.Direction.fromString(request.getParameter("order"))
                : Sort.Direction.DESC;
        
        int startPage = pager.getCurrentPage();
        startPage = startPage > 0 ? startPage - 1 : startPage;
        
        PageRequest pageable = null;
        if (orderBy != null) {
            pageable = PageRequest.of(startPage, pager.getLimit(), order, orderBy);
        } else {
            pageable = PageRequest.of(startPage, pager.getLimit());
        }
        
        pager.setPage(this.itemList.getList(
                this.filterBuilder.build(request), 
                pageable
        ));
        return pager;
    }
}