package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskHandlerTest {
    private TestHttpExchange testExchange;
    private SubtaskHandler subtaskHandler;

    @BeforeEach
    public void setUp() throws IOException {
        subtaskHandler = new SubtaskHandler();
        SubtaskHandler.manager.removeTasksByType(Task.class);
        SubtaskHandler.manager.removeTasksByType(Subtask.class);
        SubtaskHandler.manager.removeTasksByType(Epic.class);
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void handleTestGet() throws IOException {
        Epic epic = new Epic("Label", "Description");
        SubtaskHandler.manager.addNewTask(epic);
        SubtaskHandler.manager.addNewTask(new Subtask("Label-S", "Description-S", epic));
        SubtaskHandler.manager.addNewTask(new Subtask("Label-S1", "Description-S2", epic));
        testExchange = new TestHttpExchange("GET", "/subtasks", null);
        subtaskHandler.handle(testExchange.getDelegate());
        assertEquals(2, SubtaskHandler.manager.getTasksByType(Subtask.class).size());
    }

    @Test
    public void handleTestPost() throws IOException {
        Epic epic = new Epic("Label", "Description");
        SubtaskHandler.manager.addNewTask(epic);
        int id = epic.getId();
        testExchange = new TestHttpExchange("POST", "/subtasks", "{\"label\": \"Label-S\","
                + "\"description\": \"Description-S\",\"parentEpicId\": " + id + "}");
        subtaskHandler.handle(testExchange.getDelegate());
        int idSubtask = SubtaskHandler.manager.getTasksByType(Subtask.class).getFirst().getId();
        assertEquals(1, SubtaskHandler.manager.getTasksByType(Subtask.class).size());
        assertEquals("Label-S", SubtaskHandler.manager.getTaskById(idSubtask).getLabel());
        assertEquals("Description-S", SubtaskHandler.manager.getTaskById(idSubtask).getDescription());
    }

    @Test
    public void handleTestDelete() throws IOException {
        Epic epic = new Epic("Label", "Description");
        SubtaskHandler.manager.addNewTask(epic);
        Subtask subtask = new Subtask("Label-S", "Description-S", epic);
        SubtaskHandler.manager.addNewTask(subtask);
        int id = subtask.getId();
        testExchange = new TestHttpExchange("DELETE", "/subtasks/" + id, null);
        assertEquals(1, SubtaskHandler.manager.getTasksByType(Subtask.class).size());
        subtaskHandler.handle(testExchange.getDelegate());
        assertEquals(0, SubtaskHandler.manager.getTasksByType(Subtask.class).size());
    }

    @Test
    public void handleTestUnavailableMethod() throws IOException {
        testExchange = new TestHttpExchange("NOT_AVAILABLE", "/subtasks", null);
        subtaskHandler.handle(testExchange.getDelegate());
        assertEquals(0, SubtaskHandler.manager.getTasksByType(Subtask.class).size());
        assertEquals("Метод не поддерживается", testExchange.getResponseBodyAsString());
        assertEquals(405, testExchange.getResponseCode());
    }
}
