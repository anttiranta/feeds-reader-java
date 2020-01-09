package com.antti.task.controller.item.index.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class FilterBuilder {

    public Map<String, String> build(HttpServletRequest request) {
        Map<String, String> filters = new HashMap<>();

        String searchWord = request.getParameter("search_word") != null
                ? request.getParameter("search_word")
                : null;
        String searchSubject = request.getParameter("search_subject") != null
                ? request.getParameter("search_subject")
                : null;

        if (searchWord != null && searchSubject != null) {
            searchWord = searchWord.trim();
            String[] stopWords = new String[]{"a", "an", "and", "are", "as", "at",
                "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or",
                "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will",
                "with", "..."};

            if (!searchWord.isEmpty() && !Arrays.asList(stopWords).contains(searchWord)) {
                filters.put(searchSubject, searchWord);
            }
        }
        return filters;
    }
}
