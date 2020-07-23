package com.antti.task.unit.controller.item;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;
import com.antti.task.controller.item.Save;
import com.antti.task.core.Translator;
import com.antti.task.core.message.Manager;
import com.antti.task.core.message.Message;
import com.antti.task.entity.Item;
import com.antti.task.exception.CouldNotSaveException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class SaveTest {
    
    private Manager messageManagerMock;
    private Translator translatorMock;
    private com.antti.task.service.item.Save saveServiceMock;
    private Logger loggerMock;
    private Item itemMock;
    private BindingResult bindingResultMock;
    private Model modelMock;
    private Save subject;
    
    @Before
    public void setUp(){
        messageManagerMock = mock(Manager.class);
        translatorMock = mock(Translator.class);
        saveServiceMock = mock(com.antti.task.service.item.Save.class);
        loggerMock = mock(Logger.class);
        itemMock = mock(Item.class);
        bindingResultMock = mock(BindingResult.class);
        modelMock = mock(Model.class);
        
        subject = new Save(
             messageManagerMock,
             translatorMock,
             saveServiceMock,
             loggerMock
        );
    }
    
    @Test
    public void testExecute() throws Exception {
        when(bindingResultMock.hasErrors()).thenReturn(false);
        when(itemMock.getId()).thenReturn((long)1);
        when(translatorMock.trans("item.save_success")).thenReturn("Success");
        
        String result = subject.execute(itemMock, bindingResultMock, modelMock);
        assertEquals("redirect:/", result);
        
        verify(saveServiceMock, times(1)).save(itemMock);
        verify(messageManagerMock, times(1)).addSuccess("Success");
    }
    
    @Test
    public void testExecuteWithExistingItemThatHasNotPassedValidation() {
        List<Message> emptyList = new ArrayList<>();
        
        when(bindingResultMock.hasErrors()).thenReturn(true);
        when(itemMock.getId()).thenReturn((long)1);
        when(itemMock.getTitle()).thenReturn("some title");
        when(messageManagerMock.getMessages(true)).thenReturn(emptyList);
        
        String result = subject.execute(itemMock, bindingResultMock, modelMock);
        assertEquals("item-form", result);
        
        verify(modelMock, times(1)).addAttribute("title", "some title" + " (ID: 1)");
        verify(modelMock, times(1)).addAttribute("messages", emptyList);
    }
    
    @Test
    public void testExecuteReturnsUserBackToItemFormIfSavingFails() throws Exception {
        List<Message> emptyList = new ArrayList<>();
        CouldNotSaveException exception = new CouldNotSaveException("Item yuck!");
        
        when(bindingResultMock.hasErrors()).thenReturn(false);
        when(itemMock.getId()).thenReturn(null);
        when(translatorMock.trans("item.new_item")).thenReturn("new item");
        when(translatorMock.trans("item.save_exception")).thenReturn("Save failed.");
        when(messageManagerMock.getMessages(true)).thenReturn(emptyList);
        when(saveServiceMock.save(itemMock)).thenThrow(exception);
        
        String result = subject.execute(itemMock, bindingResultMock, modelMock);
        assertEquals("item-form", result);
        
        verify(messageManagerMock, times(1)).addError("Save failed.");
        verify(loggerMock, times(1)).error("Item yuck!");
        verify(modelMock, times(1)).addAttribute("title", "new item");
        verify(modelMock, times(1)).addAttribute("messages", emptyList);
    }
}
