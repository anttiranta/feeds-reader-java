package com.antti.task.service.item.importing.job.transport.mapper;

import com.antti.task.dto.CategoryDto;
import java.util.Date;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.antti.task.util.Strings;
import com.antti.task.item.importing.text.PubDateFormatter;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper extends AbstractMapper {

    private PubDateFormatter pubDateFormatter;
    
    public ItemMapper() {
        this(new PubDateFormatter());
    }
    
    public ItemMapper(PubDateFormatter pubDateFormatter) {
        this.pubDateFormatter = pubDateFormatter;
    }
    
    @Override
    public String getTitle() throws Exception {
        String title = getTextValue("title");
        
        if(Strings.isNullOrEmpty(title)) {
            throw new Exception("Title cannot be empty.");
        }
        return title;
    }

    @Override
    public String getDescription() {
        return getTextValue("description");
    }

    @Override
    public String getLink() {
        return getTextValue("link");
    }

    @Override
    public String getComments() {
        return getTextValue("comments");
    }

    @Override
    public Date getPubDate() throws Exception {
        String pubDate = getTextValue("pubDate");
        
        if (Strings.isNullOrEmpty(pubDate)) {
            throw new Exception("Published date cannot be empty.");
        }
        
        return pubDateFormatter.parse(pubDate);
    }

    @Override
    public CategoryDto getCategory() throws Exception {
        Node categoryNode = data.getElementsByTagName("category").item(0);
        if (categoryNode == null) {
            return null;
        }
        
        String categoryName = getTextValue("category"); 
        if (Strings.isNullOrEmpty(categoryName)) {
            return null;
        }
        
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(categoryName);
        categoryDto.setDomain(getCategoryDomain(categoryNode));
        
        return categoryDto;
    }
    
    private String getCategoryDomain(Node categoryNode) throws Exception {
        String categoryDomain = null;
        
        NamedNodeMap attributes = categoryNode.getAttributes();
        for (int index = 0; index < attributes.getLength(); index++) {
            Node node = attributes.item(index);
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                Attr attribute = (Attr) node;

                if (attribute.getName().equals("domain")) {
                    categoryDomain = attribute.getValue();
                }
            }
        }
        
        if (Strings.isNullOrEmpty(categoryDomain)) {
            throw new Exception("Category domain cannot be empty.");
        }
        return categoryDomain;
    }
}