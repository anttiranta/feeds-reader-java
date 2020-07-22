package com.antti.task.item.importing.transport.mapper;

import com.antti.task.dto.CategoryDto;
import com.antti.task.dto.ItemDto;
import java.util.Date;
import com.antti.task.util.Strings;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class AbstractMapper {
    
    protected Element data;
    
    public ItemDto map(Element itemData) throws Exception {
        data = itemData;
        
        ItemDto itemDto = new ItemDto();
        itemDto.setTitle(getTitle())
             .setDescription(getDescription())
             .setLink(getLink())
             .setPubDate(getPubDate())
             .setComments(getComments())
             .setCategory(getCategory());

        return itemDto;
    }
    
    abstract public String getTitle() throws Exception;
    
    abstract public String getDescription();
    
    abstract public String getLink();
    
    abstract public String getComments();
    
    abstract public Date getPubDate() throws Exception;
    
    abstract public CategoryDto getCategory() throws Exception;
    
    protected String getTextValue(String tagName) {
        String dataValue = null;
        
        Node node = data.getElementsByTagName(tagName).item(0);
        
        if (node != null){
            dataValue = node.getTextContent();
            
            if (!Strings.isNullOrEmpty(dataValue)) {
                return dataValue;
            }
        }
        return null;
    }
}
