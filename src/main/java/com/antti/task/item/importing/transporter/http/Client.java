package com.antti.task.item.importing.transporter.http;

public interface Client {
    
    public void setTimeout(int value);
    public void get(String uri) throws Exception;
    public Object getBody();
    public int getStatus();
    public String getResponseMessage();
    public boolean isSuccess();
}
