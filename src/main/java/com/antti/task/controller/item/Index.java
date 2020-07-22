package com.antti.task.controller.item;

import com.antti.task.core.PaginationCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.antti.task.core.Translator;
import com.antti.task.core.api.Filter;
import com.antti.task.core.api.FilterBuilder;
import com.antti.task.entity.Item;
import com.antti.task.html.Pager;
import com.antti.task.html.PagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.antti.task.core.api.SearchCriteriaBuilder;
import com.antti.task.service.item.ItemListService;
import java.util.Map.Entry;
import com.antti.task.bootstrap.message.MessageListMapper;
import com.antti.task.core.message.Manager;

@Controller
public class Index {
    
    private final Manager messageManager;
    private final Translator translator;
    private final SearchCriteriaBuilder searchCriteriaBuilder;
    private final FilterBuilder filterBuilder;
    private final ItemListService itemListService;
    private final PagerFactory pagerFactory;

    private static final String PAGE_VAR_NAME = "p";
    private static final String LIMIT_VAR_NAME = "limit";
    private static final String SEARCH_WORD_VAR_NAME = "search_word";
    private static final String SEARCH_FIELD_VAR_NAME = "search_subject";
    private static final int DEFAULT_LIMIT = 5;

    private final Map<String, String> fieldMapping = new HashMap<String, String>() {
        {
            put("category_name", "c.name");
            put("title", "i.title");
        }
    };

    private final List<String> allowedFilters = new ArrayList<String>() {
        {
            add("category_name");
            add("title");
        }
    };
    
    @Autowired
    public Index(
        Manager messageManager,
        Translator translator,
        SearchCriteriaBuilder searchCriteriaBuilder,
        FilterBuilder filterBuilder,
        ItemListService itemListService,
        PagerFactory pagerFactory
    ) {
        this.messageManager = messageManager;
        this.translator = translator;
        this.searchCriteriaBuilder = searchCriteriaBuilder;
        this.filterBuilder = filterBuilder;
        this.itemListService = itemListService;
        this.pagerFactory = pagerFactory;
    }

    @RequestMapping("/")
    public String execute(Model model, HttpServletRequest request) {
        Pager<Item> pager = null;

        searchCriteriaBuilder.setFilters(getFilters(request))
                .setPageSize(getLimit(request))
                .setCurrentPage(getOffset(request));

        try {
            pager = pagerFactory.create(
                    new PaginationCounter(
                            request,
                            itemListService.getItemList(searchCriteriaBuilder.create())
                    ).setPageVarName(PAGE_VAR_NAME)
                            .setLimitVarName(LIMIT_VAR_NAME)
            );

            model.addAttribute("items", pager.getPaginationCounter().getPage().getContent());
        } catch (Exception e) {
            messageManager.addError(translator.trans("item.fething_list_failed"));
            model.addAttribute("items", new ArrayList<Item>());
        }

        model.addAttribute("pager", pager);
        model.addAttribute("messages", MessageListMapper.map(messageManager.getMessages(true)));
        model.addAttribute("title", translator.trans("item.all_items"));

        return "index";
    }

    private Integer getLimit(HttpServletRequest request) {
        Integer parsedLimit = null;
        try {
            parsedLimit = Integer.parseInt(request.getParameter(LIMIT_VAR_NAME));
        } catch (NumberFormatException e) {
            parsedLimit = DEFAULT_LIMIT;
        }

        return parsedLimit;
    }

    private int getOffset(HttpServletRequest request) {
        return request.getParameter(PAGE_VAR_NAME) != null
                ? Integer.parseInt(request.getParameter(PAGE_VAR_NAME))
                : 1;
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        List<Filter> filters = new ArrayList<>();
        String searchSubject = null;
        String searchWord = null;

        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (entry.getKey().equals(SEARCH_WORD_VAR_NAME)) {
                searchWord = entry.getValue()[0];
            }
            if (entry.getKey().equals(SEARCH_FIELD_VAR_NAME)) {
                searchSubject = entry.getValue()[0];
            }
        }

        if (searchSubject != null && searchWord != null) {
            if (allowedFilters.contains(searchSubject)
                    && fieldMapping.containsKey(searchSubject)) {

                filters.add(filterBuilder.setField(fieldMapping.get(searchSubject))
                        .setConditionType("like")
                        .setValue(searchWord)
                        .create()
                );
            }
        }

        return filters;
    }
}
