package com.antti.task.util.integration;

import org.springframework.stereotype.Component;

@Component
public class ItemImportHelper {

    public String normalizeUrl(String url) {
        return (url != null
                && !url.startsWith("http://")
                && !url.startsWith("https://"))
                ? "http://" + url
                : url;
    }
}
