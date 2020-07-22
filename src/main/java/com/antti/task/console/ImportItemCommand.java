package com.antti.task.console;

import com.antti.task.dto.ItemDto;
import com.antti.task.service.item.importing.job.Processor;
import com.antti.task.service.item.importing.job.Transport;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ImportItemCommand {

    private Logger logger; 
    private Processor itemProcessor;
    private Transport transport;
    private UrlArgumentNormalizer urlArgumentNormalizer;
    
    @Autowired
    public ImportItemCommand(
         Processor itemProcessor,
         Transport transport,
         UrlArgumentNormalizer urlArgumentNormalizer
    ) {
        this(
           itemProcessor,
           transport,
           urlArgumentNormalizer, 
           LogManager.getLogger(ImportItemCommand.class.getName())
        );
    }
    
    public ImportItemCommand(
        Processor itemProcessor,
        Transport transport,
        UrlArgumentNormalizer urlArgumentNormalizer, 
        Logger logger
    ) {
        this.itemProcessor = itemProcessor;
        this.transport = transport;
        this.urlArgumentNormalizer = urlArgumentNormalizer;
        this.logger = logger;
    }

    @ShellMethod("Performs an import for specific items by specified URL(s)")
    public void importItem(String url) {
        boolean isSuccess = true;
        
        String[] urls = urlArgumentNormalizer.normalizeUrlArgument(url);
        
        logger.info("Starting import...");

        for(String urlToProcess : urls) {
            List<ItemDto> data = transport.process(urlToProcess);
            
            if (!data.isEmpty()) {
                isSuccess = itemProcessor.processAll(data);
            }
        }

        logger.info("Import: " + (isSuccess ? "was successful" : "failed"));
    }
}