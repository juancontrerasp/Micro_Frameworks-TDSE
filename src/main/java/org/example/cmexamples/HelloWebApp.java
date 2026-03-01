package org.example.cmexamples;

import org.example.utilities.HttpServer;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.example.utilities.HttpServer.get;

public class HelloWebApp {

    public static void main(String[] args) throws IOException, URISyntaxException {
        get("/hello", (req, res) -> "hello " + req.getValues("name"));
        get("/pi", (req, res) -> "PI = " + Math.PI);
        HttpServer.start();
    }
}
