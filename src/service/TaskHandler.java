package service;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import exceptions.TaskTimeOverlapException;
import model.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getRequestParams(exchange);
        try {
            switch (method) {
                case "GET" -> handleGet(exchange, paths);
                case "POST" -> handlePost(exchange, paths);
                case "DELETE" -> handleDelete(exchange, paths);
                default -> sendUnavailableMethod(exchange);
            }
        } catch (Exception e) {
            sendText(exchange, "Внутренняя ошибка сервера", 500);
        }
    }

    private void handleGet(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length == 2) {
            handleGetTaskList(exchange);
        } else if (paths.length == 3) {
            handleGetById(exchange, paths[2]);
        }
    }

    private void handleGetTaskList(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(manager.getTasksByType(Task.class)), 200);
    }

    private void handlePost(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length != 2 && paths.length != 3) return;
        Task task = gson.fromJson(getRequestBody(exchange), Task.class);

        if (paths.length == 2) {
            handleCreateTask(exchange, task);
        } else {
            handleUpdateTask(exchange, paths[2], task);
        }
    }

    private void handleGetById(HttpExchange exchange, String idStr) throws IOException {
        Integer id = parseId(exchange, idStr);
        if (id == null) return;
        try {
            Task task = manager.getTaskById(id);
            if (task != null) {
                sendText(exchange, gson.toJson(task), 200);
            }
        } catch (NotFoundException e) {
            sendTaskNotFound(exchange);
        }
    }

    private void handleCreateTask(HttpExchange exchange, Task task) throws IOException {
        try {
            Task createdTask = manager.addNewTask(task);
            if (createdTask == null) {
                throw new TaskTimeOverlapException("Задача имеет пересечение с существующей задачей");
            }
            sendText(exchange, gson.toJson(createdTask), 200);
        } catch (TaskTimeOverlapException e) {
            sendTaskOverlap(exchange);
        }
    }

    private void handleUpdateTask(HttpExchange exchange, String idStr, Task task) throws IOException {
        Integer id = parseId(exchange, idStr);
        if (id == null) {
            return;
        }
        task.setId(id);
        manager.updateTask(task);
        sendText(exchange, gson.toJson(task), 200);
    }
}
