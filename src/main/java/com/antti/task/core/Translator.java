package com.antti.task.core;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class Translator {

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private HttpServletRequest request;

    public String trans(String str, Object... args) 
    {
        Locale locale = null;
        if(this.request != null && this.localeResolver != null) {
            locale = this.localeResolver.resolveLocale(this.request);
        } else {
            locale = this.getDefaultLocale();
        }

        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        return String.format(messages.getString(str), args);
    }
    
    private Locale getDefaultLocale()
    {
        return new Locale("en", "US");
    }
}
