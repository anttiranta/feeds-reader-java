package com.antti.task.item.service.importing.item;

import java.util.Locale;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FormatRequest {

    private String toFormat;
    private String fromFormat;
    private Locale locale;
    
}
