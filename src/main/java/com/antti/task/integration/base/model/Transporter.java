package com.antti.task.integration.base.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
import org.springframework.stereotype.Component;

@Component
public class Transporter {

    private void doTrustToCertificates() throws Exception 
    {
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
            public boolean verify(String urlHostName, SSLSession session) 
            {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    //TODO: this.log.warning("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private Object getResponseData(String url) throws MalformedURLException, IOException, Exception
    {
        try {
            this.doTrustToCertificates();
            URL urlObject = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)urlObject.openConnection();
            
            if (conn.getResponseCode() != 200){
                throw new IOException("Server returned invalid response code.");
            }
            return urlObject.openStream();
        } catch (MalformedURLException mue){
            // TODO: log error
            throw mue;
        } catch (IOException ioe){
            // TODO: log error
            throw ioe;
        } catch (Exception e){
            // TODO: log error
            throw e;
        }
    }
    
    public Object transport(String url) throws Exception
    {
        return this.getResponseData(url);
    } 
}
