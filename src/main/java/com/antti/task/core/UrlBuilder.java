package com.antti.task.core;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UrlBuilder
{
    public String getUrl(HttpServletRequest request, Map<String, String[]> params)
    {
        String routePath = this.getActionPath(request);
        
        if(!params.isEmpty()) {
            Iterator<Map.Entry<String, String[]>> itr = params.entrySet().iterator(); 
            boolean first = true;
            
            while(itr.hasNext()) 
            {
                Map.Entry<String, String[]> entry = itr.next();
                
                String[] values = entry.getValue();
                for (String value : values) {
                    if (value == null || value.equals("")) {
                        continue;
                    }
                    routePath += (first ? "?" : "&");
                    routePath += (entry.getKey() + "=" + value);
                }
                
                if (first)
                    first = false;
            }
        }
        
        return routePath;
    }
    
    protected String getActionPath(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();

        String originalPath = url.toString();
		
        int startPos = originalPath.lastIndexOf("/") + 1;
        
        return originalPath.substring(0, startPos);
    }
}
