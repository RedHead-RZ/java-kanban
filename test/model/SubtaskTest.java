package model;

import enums.Status;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    Epic epic;
    Subtask subtask;
    TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
        epic = new Epic("Label", "Description");
        subtask = new Subtask("SubLabel", "SubDescription", epic);
        manager.addNewTask(epic);
        manager.addNewTask(subtask);
    }

    @Test
    void updateTask() {
        subtask.setLabel("SubLabel-1");
        subtask.setDescription("SubDescription-1");
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateTask(subtask);
        assertEquals("SubLabel-1", subtask.getLabel());
        assertEquals("SubDescription-1", subtask.getDescription());
        assertEquals(Status.IN_PROGRESS, subtask.getStatus());
    }

    @Test
    void getParentTask() {
        assertEquals(subtask.getParentTask(), epic);
    }

    @Test
    void removeFromParentTask() {
        subtask.removeFromParentTask();
        assertEquals(0, epic.getSubtasks().size());
    }
}