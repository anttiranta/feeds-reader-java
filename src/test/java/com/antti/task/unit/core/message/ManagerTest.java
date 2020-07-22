package com.antti.task.unit.core.message;

import org.junit.Before;
import org.junit.Test;
import com.antti.task.core.message.Factory;
import com.antti.task.core.message.ManagerImpl;
import com.antti.task.core.message.Error;
import com.antti.task.core.message.Success;
import com.antti.task.core.message.Notice;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.antti.task.core.message.Message;

public class ManagerTest {
    
    private final static String DEFAULT_MESSAGE = "My message";
    private final static String DEFAULT_MESSAGES_GROUP = "group";
    
    private ManagerImpl subject;
    private HttpSession sessionMock;
    private Factory factoryMock;
    private Message messageMock;
    private Logger loggerMock;
    
    @Before
    public void setup() {
        sessionMock = mock(HttpSession.class);
        factoryMock = mock(Factory.class);
        messageMock = mock(Message.class);
        loggerMock = mock(Logger.class);
        
        subject = new ManagerImpl(
             sessionMock,
             factoryMock,
             loggerMock
        );
    }
    
    @Test
    public void testGetDefaultGroup() {
        assertEquals(ManagerImpl.DEFAULT_GROUP, subject.getDefaultGroup());
    }
    
    @Test
    public void testGetMessages() {
        String messagesGroup = "my_group";
        
        List<Message> expectedMessages = new ArrayList<>();
        
        when(sessionMock.getAttribute(messagesGroup)).thenReturn(null)
                .thenReturn(expectedMessages);
        
        assertEquals(expectedMessages, subject.getMessages(messagesGroup, false));
        
        verify(sessionMock, times(1)).setAttribute(messagesGroup, new ArrayList<>());
        verify(sessionMock, times(2)).getAttribute(messagesGroup);
    }
    
    @Test
    public void testGetMessagesWithClear() {
        List<Message> expectedMessages = new ArrayList<>();
        
        when(sessionMock.getAttribute(ManagerImpl.DEFAULT_GROUP)).thenReturn(null)
                .thenReturn(expectedMessages);
        
        assertEquals(expectedMessages, subject.getMessages(true));
        
        verify(sessionMock, times(1)).setAttribute(ManagerImpl.DEFAULT_GROUP, new ArrayList<>());
        verify(sessionMock, times(2)).getAttribute(ManagerImpl.DEFAULT_GROUP);
        verify(sessionMock, times(1)).removeAttribute(ManagerImpl.DEFAULT_GROUP);
    }
    
    @Test
    public void testAddErrorMessage() {
        assertFalse(subject.hasMessages());
        
        initAddMessageMocks(Message.TYPE_ERROR, 
             DEFAULT_MESSAGE, 
             DEFAULT_MESSAGES_GROUP
        );

        subject.addError(DEFAULT_MESSAGE, DEFAULT_MESSAGES_GROUP);
        
        assertTrue(subject.hasMessages());
        verify(sessionMock, times(2)).getAttribute(DEFAULT_MESSAGES_GROUP);
    }
    
    @Test
    public void testAddSuccessMessage() {
        assertFalse(subject.hasMessages());
        
        initAddMessageMocks(Message.TYPE_SUCCESS, 
             DEFAULT_MESSAGE, 
             DEFAULT_MESSAGES_GROUP
        );

        subject.addSuccess(DEFAULT_MESSAGE, DEFAULT_MESSAGES_GROUP);
        
        assertTrue(subject.hasMessages());
        verify(sessionMock, times(2)).getAttribute(DEFAULT_MESSAGES_GROUP);
    }
    
    @Test
    public void testAddNoticeMessage() {
        assertFalse(subject.hasMessages());
        
        initAddMessageMocks(Message.TYPE_NOTICE, 
             DEFAULT_MESSAGE, 
             DEFAULT_MESSAGES_GROUP
        );

        subject.addNotice(DEFAULT_MESSAGE, DEFAULT_MESSAGES_GROUP);
        
        assertTrue(subject.hasMessages());
        verify(sessionMock, times(2)).getAttribute(DEFAULT_MESSAGES_GROUP);
    }
    
    @Test
    public void testAddExceptionWithAlternativeText() {
        String exceptionMessage = "error happened!";
        String alternativeText = "alternative text";
        Exception exception = new Exception(exceptionMessage);
        
        Error messageErrorMock = mock(Error.class);
        
        when(factoryMock.create(Message.TYPE_ERROR))
             .thenReturn(messageErrorMock);
        when(messageErrorMock.setIdentifier(Message.DEFAULT_IDENTIFIER))
             .thenReturn(messageErrorMock);
        when(messageErrorMock.setText(alternativeText))
             .thenReturn(messageErrorMock);
        
        when(sessionMock.getAttribute(ManagerImpl.DEFAULT_GROUP))
            .thenReturn(new ArrayList<>())
            .thenReturn(new ArrayList<>());
        
        assertEquals(subject, subject.addException(exception, alternativeText));
        assertTrue(subject.hasMessages());
        
        verify(factoryMock, times(1)).create(Message.TYPE_ERROR);
        verify(messageErrorMock, times(1)).setText(alternativeText);
        verify(sessionMock, times(2)).getAttribute(ManagerImpl.DEFAULT_GROUP);
        
        verify(loggerMock, times(1)).error(replicateErrorMessage(exception));
    }
    
    @Test
    public void testAddMessages() {
        List<Message> messageCollection = new ArrayList<>();
        
        Message errorMessage = mock(Error.class);
        Message noticeMessage = mock(Notice.class);
        Message successMessage = mock(Success.class);
        
        when(sessionMock.getAttribute(ManagerImpl.DEFAULT_GROUP))
            .thenReturn(messageCollection)
            .thenReturn(messageCollection);
        
        subject.addMessages(
             Arrays.asList(errorMessage, noticeMessage, successMessage)
        );
        assertTrue(subject.hasMessages());
        
        assertEquals(errorMessage, messageCollection.get(0));
        assertEquals(noticeMessage, messageCollection.get(1));
        assertEquals(successMessage, messageCollection.get(2));
    }
    
    private void initAddMessageMocks(
         String messageType, 
         String message, 
         String messagesGroup
    ) {
        when(factoryMock.create(messageType))
             .thenReturn(messageMock);
        when(messageMock.setIdentifier(Message.DEFAULT_IDENTIFIER))
             .thenReturn(messageMock);
        when(messageMock.setText(message))
             .thenReturn(messageMock);
        when(sessionMock.getAttribute(messagesGroup))
            .thenReturn(new ArrayList<>())
            .thenReturn(new ArrayList<>());
    }
    
    private String replicateErrorMessage(Exception exception) {
        return String.format(
                "Exception message: %s%s Trace: %s", 
                exception.getMessage(), 
                "\n",
                Arrays.toString(exception.getStackTrace())
        );
    }
}
