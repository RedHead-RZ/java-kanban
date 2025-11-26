package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import manager.Managers;
import manager.TaskManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    static TaskManager manager;
    static Gson gson;
    String[] paths;
    String method;

    BaseHttpHandler() {
        if (manager == null) {
            manager = Managers.getDefault();
        }
        if (gson == null) gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);

        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }

    protected void sendTaskOverlap(HttpExchange h) throws IOException {
        sendText(h, "Имеются пересечения с существующими задачами", 406);
    }

    protected void sendTaskNotFound(HttpExchange h) throws IOException {
        sendText(h, "Задача не найдена", 404);
    }

    protected void sendUnavailableMethod(HttpExchange h) throws IOException {
        sendText(h, "Метод не поддерживается", 405);
    }

    protected String getRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

    protected Integer parseId(HttpExchange exchange, String idStr) throws IOException {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            sendText(exchange, "Неверный ID", 404);
            return null;
        }
    }

    protected void handleDelete(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length == 3) {
            Integer id = parseId(exchange, paths[2]);
            if (id == null) {
                return;
            }
            try {
                manager.removeTaskById(id);
                sendText(exchange, "Задача удалена", 200);
            } catch (NotFoundException e) {
                sendTaskNotFound(exchange);
            }
        } else {
            sendText(exchange, "Некорректный запрос", 404);
        }
    }

    protected void getRequestParams(HttpExchange exchange) {
        paths = exchange.getRequestURI().getPath().split("/");
        method = exchange.getRequestMethod();
    }
}
