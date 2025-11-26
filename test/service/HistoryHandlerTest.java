package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryHandlerTest {
    private TestHttpExchange testExchange;
    HistoryHandler historyHandler;

    @BeforeEach
    public void setUp() throws IOException {
        historyHandler = new HistoryHandler();
        HistoryHandler.manager.removeTasksByType(Task.class);
        HistoryHandler.manager.removeTasksByType(Epic.class);
        HistoryHandler.manager.removeTasksByType(Subtask.class);
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void testGetHistory() throws IOException {
        Task task = new Task("Label-1", "Description-1");
        Task task2 = new Task("Label-2", "Description-2");
        Task task3 = new Task("Label-3", "Description-3");
        HistoryHandler.manager.addNewTask(task);
        HistoryHandler.manager.addNewTask(task2);
        HistoryHandler.manager.addNewTask(task3);
        HistoryHandler.manager.getTaskById(task3.getId());
        HistoryHandler.manager.getTaskById(task.getId());
        HistoryHandler.manager.getTaskById(task2.getId());
        testExchange = new TestHttpExchange("GET", "/history", null);
        historyHandler.handle(testExchange.getDelegate());
        assertEquals(3, HistoryHandler.manager.getHistory().size());
        assertEquals(200, testExchange.getResponseCode());
        assertEquals("Label-3", HistoryHandler.manager.getHistory().get(0).getLabel());
        assertEquals("Label-1", HistoryHandler.manager.getHistory().get(1).getLabel());
        assertEquals("Label-2", HistoryHandler.manager.getHistory().get(2).getLabel());
    }

    @Test
    public void testGetHistoryWrongPath() throws IOException {
        testExchange = new TestHttpExchange("POST", "/his", null);
        historyHandler.handle(testExchange.getDelegate());
        assertEquals("Метод не поддерживается", testExchange.getResponseBodyAsString());
        assertEquals(405, testExchange.getResponseCode());
    }
}
