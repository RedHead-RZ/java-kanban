package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicHandlerTest {
    private TestHttpExchange testExchange;
    private EpicHandler epicHandler;

    @BeforeEach
    public void setUp() throws IOException {
        epicHandler = new EpicHandler();
        EpicHandler.manager.removeTasksByType(Task.class);
        EpicHandler.manager.removeTasksByType(Subtask.class);
        EpicHandler.manager.removeTasksByType(Epic.class);
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void handleTestGet() throws IOException {
        EpicHandler.manager.addNewTask(new Epic("Label", "Description"));
        EpicHandler.manager.addNewTask(new Epic("Label-2", "Description-2"));
        testExchange = new TestHttpExchange("GET", "/epics", null);
        epicHandler.handle(testExchange.getDelegate());
        assertEquals(2, EpicHandler.manager.getTasksByType(Epic.class).size());
    }

    @Test
    public void handleTestPost() throws IOException {
        testExchange = new TestHttpExchange("POST", "/epics", "{\"label\": \"Label-E\","
                + "\"description\": \"Description-E\"}");
        epicHandler.handle(testExchange.getDelegate());
        int id = EpicHandler.manager.getTasksByType(Epic.class).getFirst().getId();
        assertEquals(1, EpicHandler.manager.getTasksByType(Epic.class).size());
        assertEquals("Label-E", EpicHandler.manager.getTaskById(id).getLabel());
        assertEquals("Description-E", EpicHandler.manager.getTaskById(id).getDescription());
    }

    @Test
    public void handleTestDelete() throws IOException {
        Epic epic = new Epic("Label", "Description");
        EpicHandler.manager.addNewTask(epic);
        testExchange = new TestHttpExchange("DELETE", "/epics/" + epic.getId(), null);
        assertEquals(1, EpicHandler.manager.getTasksByType(Epic.class).size());
        epicHandler.handle(testExchange.getDelegate());
        assertEquals(0, EpicHandler.manager.getTasksByType(Epic.class).size());
    }

    @Test
    public void handleTestUnavailableMethod() throws IOException {
        testExchange = new TestHttpExchange("NOT_AVAILABLE", "/epics", null);
        epicHandler.handle(testExchange.getDelegate());
        assertEquals(0, EpicHandler.manager.getTasksByType(Epic.class).size());
        assertEquals("Метод не поддерживается", testExchange.getResponseBodyAsString());
        assertEquals(405, testExchange.getResponseCode());
    }
}
