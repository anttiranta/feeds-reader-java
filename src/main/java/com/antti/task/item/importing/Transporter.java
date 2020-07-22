package com.antti.task.item.importing;

import java.io.InputStream;
import com.antti.task.item.importing.transporter.data.XmlDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import com.antti.task.item.importing.transporter.http.Client;

@Component
public class Transporter {
    
    private final Client httpClient;
    private final XmlDecoder xmlDecoder;
    
    @Autowired
    public Transporter(
        Client httpClient,
        XmlDecoder xmlDecoder
    ){
        this.httpClient = httpClient;
        this.xmlDecoder = xmlDecoder;
    }
    
    public Document readData(String url) {
        try {
            httpClient.get(url);
            
            validateResponse();
            
            InputStream responseData = (InputStream)httpClient.getBody();
            
            return xmlDecoder.decode(responseData);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void validateResponse() throws Exception {
        if(!httpClient.isSuccess()) {
            throw new Exception(
                 "Responded with an error " + httpClient.getStatus() 
                 + "(" + httpClient.getResponseMessage() + ")"
            );
        }
    }
}
