package service;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;

public class CommonHandler {
    private BaseHttpHandler handler;
    private HttpExchange testExchange;

    @BeforeEach
    public void setUp() throws IOException {
        HttpTaskServer.start();

    }

    @AfterEach
    public void shutDown() throws IOException {
        HttpTaskServer.stop();
    }
}
