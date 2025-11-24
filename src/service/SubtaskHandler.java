package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskTimeOverlapException;
import model.Subtask;
import model.Task;
import service.dto.SubtaskDTO;
import java.io.IOException;
import java.util.ArrayList;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

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
        ArrayList<SubtaskDTO> subtasksModel = new ArrayList<>();
        manager.getTasksByType(Subtask.class).forEach(subtask -> {
            subtasksModel.add(SubtaskDTO.toSubtaskDTO((Subtask) subtask));
        });
        sendText(exchange, gson.toJson(subtasksModel), 200);
    }

    private void handleGetById(HttpExchange exchange, String idStr) throws IOException {
        Integer id = parseId(exchange, idStr);
        if (id == null) return;

        Task task = manager.getTaskById(id);
        if (task != null && task instanceof Subtask) {
            sendText(exchange, gson.toJson(SubtaskDTO.toSubtaskDTO((Subtask) task)), 200);
        } else {
            sendTaskNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length != 2 && paths.length != 3) return;
        SubtaskDTO subtaskDTO = gson.fromJson(getRequestBody(exchange), SubtaskDTO.class);
        Subtask subtask = subtaskDTO.toSubtask(manager);
        if (paths.length == 2) {
            handleCreateTask(exchange, subtask);
        } else {
            handleUpdateTask(exchange, paths[2], subtask);
        }
    }

    private void handleCreateTask(HttpExchange exchange, Task task) throws IOException {
        try {
            Subtask createdTask = (Subtask) manager.addNewTask(task);
            if (createdTask == null) {
                throw new TaskTimeOverlapException("Задача имеет пересечение с существующей задачей");
            }
            sendText(exchange, gson.toJson(SubtaskDTO.toSubtaskDTO(createdTask)), 200);
        } catch (TaskTimeOverlapException e) {
            sendTaskOverlap(exchange);
        }
    }

    private void handleUpdateTask(HttpExchange exchange, String idStr, Task task) throws IOException {
        Integer id = parseId(exchange, idStr);
        if (id == null) {
            sendTaskNotFound(exchange);
            return;
        }

        task.setId(id);
        manager.updateTask(task);
        sendText(exchange, gson.toJson(SubtaskDTO.toSubtaskDTO((Subtask) task)), 200);
    }
}
