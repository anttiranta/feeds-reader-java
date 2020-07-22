package com.antti.task.console;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UrlArgumentNormalizer {
    
    /**
     * Normalizing data for url argument.
     *
     * Expecting argument to be either items separated by commas 
     * in a form "url1","url2","url3".
     *
     * @param argument
     * @return
     */
    public String[] normalizeUrlArgument(String argument) 
    {
        Set<String> urlList = new HashSet<>(); 
        for (String url : argument.split(",")){
            urlList.add(url.trim());
        }
        return urlList.toArray(new String[urlList.size()]);
    }
}
