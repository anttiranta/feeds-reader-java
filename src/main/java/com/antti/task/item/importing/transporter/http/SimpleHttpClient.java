package com.antti.task.item.importing.transporter.http;

import java.net.HttpURLConnection;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SimpleHttpClient implements Client {

    private Object responseBody;
    private int responseStatus = 0;
    private String responseMessage = "";
    private int timeout = 300;
    private boolean doTrustToCertificates;
    
    public SimpleHttpClient() {
        this(false);
    }
    
    public SimpleHttpClient(boolean doTrustToCertificates) {
        this.doTrustToCertificates = doTrustToCertificates;
    }
    
    @Override
    public void setTimeout(int value) {
        this.timeout = value;
    }
    
    @Override
    public void get(String uri) throws Exception {
        makeGetRequest(uri);
    }
    
    private void makeGetRequest(String uri) throws Exception {
        makeGetRequest(uri, new HashMap<>());
    }     
    
    private void makeGetRequest(String uri, Map<String, String[]> params) throws Exception {
        try {
            if (doTrustToCertificates) {
                // ugly hack, just here to get the app work
                doTrustToCertificates();
            }
            
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(timeout * 1000);
            con.setRequestMethod("GET");
            
            responseStatus = con.getResponseCode();
            responseMessage = con.getResponseMessage();
            responseBody = url.getContent();
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public Object getBody() {
        return responseBody;
    }
    
    @Override
    public int getStatus() {
        return responseStatus;
    }
    
    @Override
    public boolean isSuccess() {
        return responseStatus == 200;
    }
    
    @Override
    public String getResponseMessage() {
        return responseMessage;
    }
    
    private void doTrustToCertificates() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    return;
                }
                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    return;
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    return false;
                }
                return true;
            }
        };
        
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
}
