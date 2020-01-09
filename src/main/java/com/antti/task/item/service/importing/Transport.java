package com.antti.task.item.service.importing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import com.antti.task.integration.base.model.Transporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

@Component
public class Transport {

    @Autowired
    private Transporter transporter;
    
    private ItemMapper mapper;
    
    public List<Map<String, Object>> process(String url)
    {
        Document data = this.readData(url);
        return this.processXmlData(data);
    }
    
    public Document readData(String url)
    {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse((InputStream)this.getTransporter().transport(url));
        } catch (Exception e) {
            // TODO: log some error
            return null;
        }
    }
    
    public List<Map<String, Object>> processXmlData(Document data)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        
        if (data == null){
            return result;
        }
        
        data.getDocumentElement().normalize();
        
        NodeList nodeList = data.getElementsByTagName("item");
        
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element item = (Element)node;
                Map<String, Object> mappedData = this.mapItemData(item);

                Map<String, Object> preparedItem = this.prepareData(mappedData);
                if (preparedItem == null){
                    // TODO: log item failed
                    continue;
                }
                
                result.add(preparedItem);
            }
        }
        return result;
    }
    
    protected Map<String, Object> prepareData(Map<String, Object> data)
    {
        if (data.containsKey("title") == false) {
            return null;
        }
        return data;
    }
    
    protected Map<String, Object> mapItemData(Element item)
    {
        this.mapper = new ItemMapper();
        return mapper.map(item);
    }
    
    /**
     * TODO: remove and start using with di
     * @return
     */
    private Transporter getTransporter()
    {
        if(this.transporter == null){
            this.transporter = new Transporter();
        }
        return this.transporter;
    }
}
