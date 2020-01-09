package com.antti.task.item.service.importing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ItemMapper {

    private Element data;

    public Map<String, Object> map(Element item) {
        Map<String, Object> result = new HashMap<>();
        this.data = item;

        result.put("title", this.getTitle());
        result.put("description", this.getDescription());
        result.put("link", this.getLink());
        result.put("comments", this.getComments());
        result.put("pubDate", this.getPubDate());

        result.put("category", this.getCategory());
        result.put("category_domain", this.getCategoryDomain());

        return this.cleanResult(result);
    }

    public String getTitle() {
        return this.getTextValue("title");
    }

    public String getDescription() {
        return this.getTextValue("description");
    }

    public String getLink() {
        return this.getTextValue("link");
    }

    public String getComments() {
        return this.getTextValue("comments");
    }

    public String getPubDate() {
        return this.getTextValue("pubDate");
    }

    public String getCategory() {
        return this.getTextValue("category");
    }

    public String getCategoryDomain() {
        Node categoryNode = this.data.getElementsByTagName("category").item(0);
        if (categoryNode == null) {
            return null;
        }
        
        NamedNodeMap attributes = categoryNode.getAttributes();
        for (int index = 0; index < attributes.getLength(); index++) {
            Node node = attributes.item(index);
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                Attr attribute = (Attr) node;

                if (attribute.getName().equals("domain")) {
                    return attribute.getValue();
                }
            }
        }
        return null;
    }

    private String getTextValue(String tagName) {
        String dataValue = null;
        
        Node node = this.data.getElementsByTagName(tagName).item(0);
        
        if (node != null){
            dataValue = node.getTextContent();
            
            if (null != dataValue) {
                return dataValue;
            }
        }
        return dataValue;
    }

    protected Map<String, Object> cleanResult(Map<String, Object> data) {
        Iterator<Map.Entry<String, Object>> itr = data.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<String, Object> entry = itr.next();
            if (entry.getValue() == null){
                itr.remove();
            }
        }
        
        if (!data.containsKey("category_domain") 
                && data.containsKey("category")) {
            data.remove("category");
        }
        return data;
    }
}
