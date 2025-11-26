package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizeHandlerTest {
    private TestHttpExchange testExchange;
    private PrioritizeHandler prioritizeHandler;

    @BeforeEach
    public void setUp() throws IOException {
        prioritizeHandler = new PrioritizeHandler();
        PrioritizeHandler.manager.removeTasksByType(Task.class);
        PrioritizeHandler.manager.removeTasksByType(Epic.class);
        PrioritizeHandler.manager.removeTasksByType(Subtask.class);
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task("Label-1", "Description-1", now.plusMonths(1), Duration.ofHours(1));
        Task task2 = new Task("Label-2", "Description-2", now, Duration.ofHours(2));
        Task task3 = new Task("Label-3", "Description-3", now.plusHours(3), Duration.ofHours(3));
        PrioritizeHandler.manager.addNewTask(task);
        PrioritizeHandler.manager.addNewTask(task2);
        PrioritizeHandler.manager.addNewTask(task3);
        testExchange = new TestHttpExchange("GET", "/prioritized", null);
        prioritizeHandler.handle(testExchange.getDelegate());
        assertEquals(3, PrioritizeHandler.manager.getPrioritizedTasks().size());
        assertEquals(200, testExchange.getResponseCode());
        assertEquals("Label-2", PrioritizeHandler.manager.getPrioritizedTasks().get(0).getLabel());
        assertEquals("Label-3", PrioritizeHandler.manager.getPrioritizedTasks().get(1).getLabel());
        assertEquals("Label-1", PrioritizeHandler.manager.getPrioritizedTasks().get(2).getLabel());
    }

    @Test
    public void testGetPrioritizedTasksWrongPath() throws IOException {
        testExchange = new TestHttpExchange("POST", "/his", null);
        prioritizeHandler.handle(testExchange.getDelegate());
        assertEquals("Метод не поддерживается", testExchange.getResponseBodyAsString());
        assertEquals(405, testExchange.getResponseCode());
    }
}
