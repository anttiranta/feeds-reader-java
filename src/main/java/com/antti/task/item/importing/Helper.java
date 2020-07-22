package com.antti.task.item.importing;

import org.springframework.stereotype.Component;

@Component
public class Helper {

    public String normalizeUrl(String url) {
        return (url != null
                && !url.startsWith("http://")
                && !url.startsWith("https://"))
                ? "http://" + url
                : url;
    }
}
