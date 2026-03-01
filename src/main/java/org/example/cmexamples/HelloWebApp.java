package org.example.cmexamples;

import static org.example.utilities.HttpServer.get;

public class HelloWebApp {

    public static void main(String[] args){
        get("/hello", (req, res) -> "hello world!");
        get("/pi", (req, res) -> "PI = " + Math.PI);
    }
}
