package org.example.cmexamples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import org.example.utilities.HttpServer;
import static org.example.utilities.HttpServer.get;
import static org.example.utilities.HttpServer.path;
import static org.example.utilities.HttpServer.staticfiles;

public class WebAppTest {

    public static void main(String[] args) throws InterruptedException {

        staticfiles("/webroot");
        path("/App");
        get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/pi",    (req, resp) -> String.valueOf(Math.PI));

        Thread serverThread = new Thread(() -> {
            try {
                HttpServer.start();
            } catch (IOException | URISyntaxException e) {
                System.err.println("Server error: " + e.getMessage());
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(500);

        int passed = 0;
        int failed = 0;

        String helloBody = httpGet("http://localhost:35000/App/hello?name=Pedro");
        if (helloBody.contains("Hello Pedro")) {
            System.out.println("✓ TEST 1 PASSED: GET /App/hello?name=Pedro  →  \"Hello Pedro\" found in response");
            passed++;
        } else {
            System.out.println("✗ TEST 1 FAILED: GET /App/hello?name=Pedro  →  unexpected response: " + helloBody);
            failed++;
        }

        String piBody = httpGet("http://localhost:35000/App/pi");
        if (piBody.contains(String.valueOf(Math.PI))) {
            System.out.println("✓ TEST 2 PASSED: GET /App/pi                →  PI value found in response");
            passed++;
        } else {
            System.out.println("✗ TEST 2 FAILED: GET /App/pi                →  unexpected response: " + piBody);
            failed++;
        }

        String indexBody = httpGet("http://localhost:35000/index.html");
        if (indexBody.contains("Welcome to My Web App")) {
            System.out.println("✓ TEST 3 PASSED: GET /index.html            →  static HTML content found in response");
            passed++;
        } else {
            System.out.println("✗ TEST 3 FAILED: GET /index.html            →  unexpected response: " + indexBody);
            failed++;
        }

        System.out.printf("  Results: %d passed, %d failed%n", passed, failed);
    }

    private static String httpGet(String urlStr) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
