package com.antti.task.item.service.importing.item;

import java.util.Locale;

public class FormatRequestBuilder {

    private String toFormat;
    private String fromFormat;
    private Locale locale;
    
    public FormatRequestBuilder fromFormat(String fromFormat){
        this.fromFormat = fromFormat;
        return this;
    }
    
    public FormatRequestBuilder toFormat(String toFormat){
        this.toFormat = toFormat;
        return this;
    }
    
    public FormatRequestBuilder usingLocale(Locale locale){
        this.locale = locale;
        return this;
    }
    
    public FormatRequest build(){
        this.validate();
        
        FormatRequest fr = new FormatRequest();
        fr.setFromFormat(fromFormat);
        fr.setLocale(locale);
        fr.setToFormat(toFormat);
        return fr;
    }
    
    private void validate(){
        if (this.locale == null || this.toFormat == null || this.fromFormat == null) {
            throw new IllegalArgumentException("Format request has not been initialized properly.");
        }
    }
}
