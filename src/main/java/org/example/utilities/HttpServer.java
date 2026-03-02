package org.example.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    static Map<String, Route> endPoints = new HashMap<>();
    private static String staticFilesLocation = null;
    private static String appPath = "";

    public static void staticfiles(String location) {
        staticFilesLocation = location;
    }

    public static void path(String prefix) {
        appPath = prefix;
    }

    public static void start() throws IOException, URISyntaxException {
        main(new String[]{});
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {


            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean isFirstLine = true;

            String reqPath = "";
            Request request = new Request(new java.util.HashMap<>());

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (isFirstLine) {
                    String[] firstLineTokens = inputLine.split(" ");
                    String method = firstLineTokens[0];
                    String uristr = firstLineTokens[1];
                    String protocolVersion = firstLineTokens[2];
                    URI requestedURI = new URI(uristr);
                    reqPath = requestedURI.getPath();
                    request = parseRequest(requestedURI);
                    System.out.println("Path: " + reqPath);
                    isFirstLine = false;
                }


                if (!in.ready()) {
                    break;
                }
            }

            Route route = endPoints.get(reqPath);

            if (route != null) {
                outputLine = "HTTP/1.1 200 OK\n\r"
                        + "Content-Type: text/html\n\r"
                        + "\n\r"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Title of the document</title>\n"
                        + "</head>"
                        + "<body>"
                        + route.handle(request, new Response())
                        + "</body>"
                        + "</html>";
            } else {
                String body = serveStaticFile(reqPath);
                outputLine = "HTTP/1.1 200 OK\n\r"
                        + "Content-Type: text/html\n\r"
                        + "\n\r"
                        + body;
            }
            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }
            serverSocket.close();
    }

    public static void get(String path, WebMethod wm){
        endPoints.put(appPath + path, (req, res) -> wm.execute());
    }

    public static void get(String path, Route route){
        endPoints.put(appPath + path, route);
    }

    private static Request parseRequest(URI uri) {
        java.util.Map<String, String> params = new java.util.HashMap<>();
        String query = uri.getQuery();
        if (query != null) {
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    params.put(kv[0], kv[1]);
                } else if (kv.length == 1) {
                    params.put(kv[0], "");
                }
            }
        }
        return new Request(params);
    }

    private static String serveStaticFile(String reqPath) {
        if (staticFilesLocation != null) {
            String base = staticFilesLocation.startsWith("/") ? staticFilesLocation : "/" + staticFilesLocation;
            String resourcePath = base + reqPath;
            try (InputStream fileStream = HttpServer.class.getResourceAsStream(resourcePath)) {
                if (fileStream != null) {
                    return new String(fileStream.readAllBytes());
                }
            } catch (IOException e) {
                System.err.println("Error reading static file: " + e.getMessage());
            }
        }
        return "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Not Found</title></head>"
                + "<body>404 - Not Found</body></html>";
    }


}
