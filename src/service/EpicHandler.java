package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskTimeOverlapException;
import model.Epic;
import model.Task;
import service.dto.EpicDTO;
import service.dto.SubtaskDTO;
import java.io.IOException;
import java.util.ArrayList;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
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
        } else if (paths.length == 4) {
            handleGetSubtasksById(exchange, paths[2]);
        }
    }

    private void handleGetTaskList(HttpExchange exchange) throws IOException {
        ArrayList<EpicDTO> epicsModel = new ArrayList<>();
        manager.getTasksByType(Epic.class).forEach(epic -> {
            epicsModel.add(EpicDTO.toEpicDTO((Epic) epic));
        });
        sendText(exchange, gson.toJson(epicsModel), 200);
    }

    private void handleGetById(HttpExchange exchange, String idStr) throws IOException {
        Integer id = parseId(exchange, idStr);
        if (id == null) return;

        Task task = manager.getTaskById(id);
        if (task != null && task instanceof Epic) {
            sendText(exchange, gson.toJson(EpicDTO.toEpicDTO((Epic) task)), 200);
        } else {
            sendTaskNotFound(exchange);
        }
    }

    private void handleGetSubtasksById(HttpExchange exchange, String idStr) throws IOException {
        Integer id = parseId(exchange, idStr);
        if (id == null) return;

        Epic epic = null;
        try {
            epic = (Epic) manager.getTaskById(id);
        } catch (ClassCastException e) {
            sendText(exchange, "Некорректный идентификатор эпика", 404);
            return;
        }
        ArrayList<SubtaskDTO> subtasks = new ArrayList<>();
        epic.getSubtasks().forEach(subtask -> {
            subtasks.add(SubtaskDTO.toSubtaskDTO(subtask));
        });
        sendText(exchange, gson.toJson(subtasks), 200);
    }

    private void handlePost(HttpExchange exchange, String[] paths) throws IOException {
        if (paths.length != 2 && paths.length != 3) return;
        EpicDTO epicDTO = gson.fromJson(getRequestBody(exchange), EpicDTO.class);
        Epic epic = epicDTO.toEpic();
        if (paths.length == 2) {
            handleCreateTask(exchange, epic);
        } else {
            handleUpdateTask(exchange, paths[2], epic);
        }
    }

    private void handleCreateTask(HttpExchange exchange, Task task) throws IOException {
        try {
            Epic createdTask = (Epic) manager.addNewTask(task);
            if (createdTask == null) {
                sendTaskOverlap(exchange);
            }
            sendText(exchange, gson.toJson(EpicDTO.toEpicDTO(createdTask)), 200);
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
        sendText(exchange, gson.toJson(EpicDTO.toEpicDTO((Epic) task)), 200);
    }
}
