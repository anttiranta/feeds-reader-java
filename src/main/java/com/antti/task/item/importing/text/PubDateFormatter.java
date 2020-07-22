package com.antti.task.item.importing.text;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.stereotype.Component;

@Component
public class PubDateFormatter {
    
    public static final DateTimeFormatter PUB_DATE_DATE_TIME;
    static {
        Map<Long, String> dow = new HashMap<>();
        dow.put(1L, "Mon");
        dow.put(2L, "Tue");
        dow.put(3L, "Wed");
        dow.put(4L, "Thu");
        dow.put(5L, "Fri");
        dow.put(6L, "Sat");
        dow.put(7L, "Sun");
        Map<Long, String> moy = new HashMap<>();
        moy.put(1L, "Jan");
        moy.put(2L, "Feb");
        moy.put(3L, "Mar");
        moy.put(4L, "Apr");
        moy.put(5L, "May");
        moy.put(6L, "Jun");
        moy.put(7L, "Jul");
        moy.put(8L, "Aug");
        moy.put(9L, "Sep");
        moy.put(10L, "Oct");
        moy.put(11L, "Nov");
        moy.put(12L, "Dec");
        PUB_DATE_DATE_TIME = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .parseLenient()
                .optionalStart()
                .appendText(DAY_OF_WEEK, dow)
                .appendLiteral(", ")
                .optionalEnd()
                .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(' ')
                .appendText(MONTH_OF_YEAR, moy)
                .appendLiteral(' ')
                .appendValue(YEAR, 4)  // 2 digit year not handled
                .appendLiteral(' ')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalEnd()
                .appendLiteral(' ')
                .appendPattern("XXXX")
                .parseStrict()
                .toFormatter();
    }
    
    private DateTimeFormatter formatter;
    
    public PubDateFormatter() {
        this(PUB_DATE_DATE_TIME);
    }       
    
    public PubDateFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }
    
    public Date parse(String pubDate) {
        OffsetDateTime dt = OffsetDateTime.parse(
             pubDate,
             formatter
        );
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(dt.getOffset().getId()));
        cal.set(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), dt.getSecond());
        cal.clear(Calendar.MILLISECOND);

        return cal.getTime();
    }
}
