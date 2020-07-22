package com.antti.task.service.item.importing.job;

import com.antti.task.dto.ItemDto;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import com.antti.task.item.importing.Transporter;
import com.antti.task.service.item.importing.job.transport.mapper.ItemMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

@Service
public class Transport {

    private final ItemMapper mapper;
    private final Transporter transporter;
    private Logger logger;

    @Autowired
    public Transport(
        ItemMapper mapper,
        Transporter transporter
    ) {
        this(mapper, transporter, Logger.getLogger("com.antti.task"));
    }
    
    public Transport(
        ItemMapper mapper,
        Transporter transporter,
        Logger logger
    ) {
        this.mapper = mapper;
        this.transporter = transporter;
        this.logger = logger;
    }

    public List<ItemDto> process(String url) {
        logger.info("Sending request to " + url);

        Document data = transporter.readData(url);

        logger.info(
             "Received response for sent request to " + url + ": " + data.toString()
        );

        return processResponseData(data);
    }

    private List<ItemDto> processResponseData(Document data) {
        List<ItemDto> result = new ArrayList<>();

        if (data == null) {
            return result;
        }

        data.getDocumentElement().normalize();

        NodeList nodeList = data.getElementsByTagName("item");

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            ItemDto item = mapItemData((Element)node);
            if (item != null) {
                result.add(item);
            }
            
            logItemProcessing((Element)node, item);
        }

        return result;
    }

    private ItemDto mapItemData(Element item) {
        try {
            return mapper.map(item);
        } catch (Exception exception) {
            logger.error(
                 "Error while mapping item data: " + exception.getMessage() + " " + 
                 "Data: " + item.toString()
            );
        }
        
        return null;
    }
    
    private void logItemProcessing(Element element, ItemDto item) {
        StringBuilder sb = new StringBuilder("Processing item with title: ");
        
        try {
            sb.append(mapper.getTitle());
        } catch (Exception e) {
            sb.append("(unable to get title)");
        }
        
        sb.append("mappedItem: ");
        sb.append(item != null ? item.toString() : "");
        sb.append("itemData: ");
        sb.append(element.toString());

        logger.debug(sb.toString());
    }
}
