package com.antti.task.item.service.importing.item;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter 
{
    private FormatRequest formatRequest;
    
    public DateFormatter(FormatRequest formatRequest){
        this.formatRequest = formatRequest;
    }
    
    public Date format(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(
            this.formatRequest.getFromFormat(), 
            this.formatRequest.getLocale()
        );
        DateFormat sdf2 = new SimpleDateFormat(
            this.formatRequest.getToFormat(), 
            this.formatRequest.getLocale()
        );
        sdf.setLenient(false);
        sdf2.setLenient(false);

        try {
            Date parsedDate = sdf.parse(dateStr);
            return sdf2.parse(sdf2.format(parsedDate));
        } catch (ParseException e) {
            return null;
        }
    }
}
