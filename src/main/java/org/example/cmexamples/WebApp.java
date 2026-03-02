package org.example.cmexamples;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.example.utilities.HttpServer.get;
import static org.example.utilities.HttpServer.path;
import static org.example.utilities.HttpServer.start;
import static org.example.utilities.HttpServer.staticfiles;

/**
 * Example application demonstrating how to develop a web application
 * using the micro web framework.
 *
 * Static files are served from src/main/resources/webroot (e.g. index.html).
 * REST endpoints are available under the /App prefix:
 *   http://localhost:35000/App/hello?name=Pedro
 *   http://localhost:35000/App/pi
 *   http://localhost:35000/index.html
 */
public class WebApp {

    public static void main(String[] args) throws IOException, URISyntaxException {
        staticfiles("/webroot");
        path("/App");
        get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
        start();
    }
}
