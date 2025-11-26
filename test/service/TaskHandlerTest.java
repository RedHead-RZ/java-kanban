package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskHandlerTest {
    private TestHttpExchange testExchange;
    private TaskHandler taskHandler;

    @BeforeEach
    public void setUp() throws IOException {
        taskHandler = new TaskHandler();
        TaskHandler.manager.removeTasksByType(Task.class);
        TaskHandler.manager.removeTasksByType(Subtask.class);
        TaskHandler.manager.removeTasksByType(Epic.class);
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void handleTestGet() throws IOException {
        TaskHandler.manager.addNewTask(new Task("Label", "Description"));
        TaskHandler.manager.addNewTask(new Task("Label-2", "Description-2"));
        testExchange = new TestHttpExchange("GET", "/tasks", null);
        taskHandler.handle(testExchange.getDelegate());
        assertEquals(2, TaskHandler.manager.getTasksByType(Task.class).size());
    }

    @Test
    public void handleTestPost() throws IOException {
        testExchange = new TestHttpExchange("POST", "/tasks", "{"
                + "\"label\": \"Label-T\","
                + "\"description\": \"Description-T\","
                + "\"duration\": 180,"
                + "\"startTime\": \"2029-09-26T15:09:24.8880416\""
                + "}");
        taskHandler.handle(testExchange.getDelegate());
        int id = TaskHandler.manager.getTasksByType(Task.class).getFirst().getId();
        assertEquals(1, TaskHandler.manager.getTasksByType(Task.class).size());
        assertEquals("Label-T", TaskHandler.manager.getTaskById(id).getLabel());
        assertEquals("Description-T", TaskHandler.manager.getTaskById(id).getDescription());
    }

    @Test
    public void handleTestDelete() throws IOException {
        Task task = new Task("Label", "Description");
        TaskHandler.manager.addNewTask(task);
        testExchange = new TestHttpExchange("DELETE", "/tasks/" + task.getId(), null);
        assertEquals(1, TaskHandler.manager.getTasksByType(Task.class).size());
        taskHandler.handle(testExchange.getDelegate());
        assertEquals(0, TaskHandler.manager.getTasksByType(Task.class).size());
    }

    @Test
    public void handleTestUnavailableMethod() throws IOException {
        testExchange = new TestHttpExchange("NOT_AVAILABLE", "/tasks", null);
        taskHandler.handle(testExchange.getDelegate());
        assertEquals(0, TaskHandler.manager.getTasksByType(Task.class).size());
        assertEquals("Метод не поддерживается", testExchange.getResponseBodyAsString());
        assertEquals(405, testExchange.getResponseCode());
    }
}
