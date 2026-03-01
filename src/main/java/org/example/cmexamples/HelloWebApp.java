package org.example.cmexamples;

import java.io.IOException;
import java.net.URISyntaxException;

import org.example.utilities.HttpServer;
import static org.example.utilities.HttpServer.get;
import static org.example.utilities.HttpServer.staticfiles;

public class HelloWebApp {

    public static void main(String[] args) throws IOException, URISyntaxException {
        staticfiles("webroot/public");
        get("/hello", (req, res) -> "hello " + req.getValues("name"));
        get("/pi", (req, res) -> "PI = " + Math.PI);
        HttpServer.start();
    }
}
