package service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizeHandler());
        server.start();
    }

    public static void stop() {
        server.stop(0);
    }
}
