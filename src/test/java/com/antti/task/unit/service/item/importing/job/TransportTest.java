package com.antti.task.unit.service.item.importing.job;

import com.antti.task.dto.ItemDto;
import com.antti.task.item.importing.Transporter;
import com.antti.task.service.item.importing.job.Transport;
import com.antti.task.service.item.importing.job.transport.mapper.ItemMapper;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TransportTest {
    
    private Transport subject;
    private ItemMapper mapperMock;
    private Transporter transporterMock;
    
    @Before
    public void setup() {
        mapperMock = mock(ItemMapper.class);
        transporterMock = mock(Transporter.class);
        
        subject = new Transport(mapperMock, transporterMock);
    }
    
    @Test
    public void testProcess() throws Exception {
        String url = "https://www.feedforall.com/sample-feed.xml";
        Document sampleXmlDoc = getSampleXmlDocument();

        when(transporterMock.readData(url)).thenReturn(sampleXmlDoc);
        when(mapperMock.map((Element)notNull())).thenReturn(new ItemDto());
        
        List<ItemDto> result = subject.process(url);
        
        assertTrue(result.size() == 2);
        
        verify(transporterMock, times(1)).readData(url);
        verify(mapperMock, times(2)).map((Element)notNull());
    }
    
    private Document getSampleXmlDocument() throws Exception {
        Document document = (DocumentBuilderFactory.newInstance()).newDocumentBuilder()
                    .newDocument();
        
        Element root = document.createElement("channel");
        
        for (int i = 0; i < 2; i++) {
            Element item = document.createElement("item");
        
            Element title = document.createElement("title");
            title.appendChild(document.createTextNode("Some title " + i));
            item.appendChild(title);

            Element description = document.createElement("description");
            description.appendChild(document.createTextNode("Short description " + i));
            item.appendChild(description);

            Element link = document.createElement("link");
            link.appendChild(document.createTextNode(
                    "http://www.feedforall.com/feedforall-partners" + i + ".htm"
            ));
            item.appendChild(link);

            root.appendChild(item);
        }

        document.appendChild(root);
        
        return document;
    }
}
