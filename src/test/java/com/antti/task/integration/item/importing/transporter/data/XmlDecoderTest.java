package com.antti.task.integration.item.importing.transporter.data;

import com.antti.task.item.importing.transporter.data.XmlDecoder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlDecoderTest {
    
    private XmlDecoder subject;
    
    @Before
    public void setup() {
        subject = new XmlDecoder();
    }
    
    @Test
    public void testDecode() throws Exception {
        InputStream data = new ByteArrayInputStream(getSampleXmlString().getBytes());

        Document document = subject.decode(data);
        document.normalizeDocument();
        
        NodeList nodeList = document.getElementsByTagName("item");
        assertTrue(nodeList.getLength() == 2);
    }
    
    private String getSampleXmlString() {
        return "<rss version=\"2.0\">\n" +
"        <channel>\n" +
"            <title>Sample Feed</title>\n" +
"            <description>Test description</description>\n" +
"            <item>\n" +
"                <title>Recommended Desktop Feed Reader Software</title>\n" +
"                <description>Short description 1</description>\n" +
"                <link>http://www.feedforall.com/feedforall-partners.htm</link>\n" +
"                <pubDate>Tue, 26 Oct 2004 14:03:25 -0500</pubDate>\n" +
"            </item>\n" +
"            <item>\n" +
"                <title>RSS Solutions for Restaurants</title>\n" +
"                <description>Short description 2</description>\n" +
"                <link>http://www.feedforall.com/restaurant.htm</link>\n" +
"                <category domain=\"www.dmoz.com\">Some category</category>\n" +
"                <comments>http://www.feedforall.com/forum</comments>\n" +
"                <pubDate>Tue, 19 Oct 2004 11:09:11 -0400</pubDate>\n" +
"            </item>\n" +
"        </channel>\n" +
"    </rss>";
    }
}
