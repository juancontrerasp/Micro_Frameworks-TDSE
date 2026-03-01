package org.example.utilities;

import java.net.MalformedURLException;
import java.net.URL;

public class URLParser {

    private URL myURL;

    public static void main(String[] args) throws MalformedURLException {
        URL myURL = new URL("http://ldbn.is.escuelaing.edu.co:8084/respuestasexamenarep.txt?val9&t=8&r=6#respuestas");
        System.out.println("Protocol: " +  myURL.getProtocol());
        System.out.println("Authority: " + myURL.getAuthority());
        System.out.println("Host: " + myURL.getHost());
        System.out.println("Port : " + myURL.getPort());
        System.out.println("Path: " + myURL.getPath());
        System.out.println("Query: " + myURL.getQuery());
        System.out.println("File: "+ myURL.getFile());
        System.out.println("Ref: " + myURL.getRef());
    }
    public String getProtocol(){
        return myURL.getProtocol();
    }
}
