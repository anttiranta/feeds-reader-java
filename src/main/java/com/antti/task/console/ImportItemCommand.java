package com.antti.task.console;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.antti.task.item.service.importing.Processor;
import com.antti.task.item.service.importing.Transport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ImportItemCommand {

    private Set<String> urls;
    
    private final Logger logger = 
            LogManager.getLogger(ImportItemCommand.class.getName());
    
    @Autowired
    private Processor itemProcessor;
    
    @Autowired
    public ImportItemCommand() {
    }

    @ShellMethod("Performs an import for specific items by specified URL(s)")
    public void importItem(String url) 
    {
        this.urls = this.normalizeUrlArgument(url);
        
        this.logger.info("Starting import...");
        
        Set<String> errors = new HashSet<>();
        
        for(String urlToProcess : this.urls) {
            Transport transport = new Transport();
            List<Map<String, Object>> data = transport.process(urlToProcess);
            
            if (!data.isEmpty()) {
                if(!this.itemProcessor.processAll(data)){
                    errors.add(url);
                }
            }
        }

        this.printResults(errors);
    }
    
    private void printResults(Set<String> errors)
    {
        this.logger.info("Finished with " + errors.size() + " errors");
        
        if (!errors.isEmpty()) {
            String failedUrls = "";
            for(String error : errors){
               failedUrls += (error + ",");
            }
            this.logger.info("Failed urls: " + failedUrls);
        }
        
        this.logger.info("Result: " + (errors.isEmpty() ? "SUCCESS" : "FAIL"));
    }

    /**
     * Normalizing data for url argument.
     *
     * Expecting argument to be items in a form "url1","url2","url3". 
     *
     * @param argument
     * @return
     */
    protected Set<String> normalizeUrlArgument(String argument) 
    {
        Set<String> urlList = new HashSet<>(); 
        for (String url : Arrays.asList(argument.split(","))){
            urlList.add(url.trim());
        }
        return urlList;
    }
}