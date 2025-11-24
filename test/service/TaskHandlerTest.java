package service;

import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TaskHandlerTest {
    TaskManager taskManager;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager.removeTasksByType(Task.class);
        taskManager.removeTasksByType(Subtask.class);
        taskManager.removeTasksByType(Epic.class);
        taskManager = Managers.getDefault();
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() throws IOException {
        HttpTaskServer.stop();
    }

    @Test
    public void handleTest() {

    }
}
