package org.example.utilities;

@FunctionalInterface
public interface Route {
    String handle(Request req, Response res);
}
