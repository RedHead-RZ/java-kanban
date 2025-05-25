package model;

import enums.Status;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }


    @Test
    void updateTask() {
        Task task = new Task("Label", "Description");
        assertEquals("Label", task.getLabel());
        assertEquals("Description", task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
        manager.addNewTask(task);
        task.setLabel("New Label");
        task.setDescription("New Description");
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        Task newTask = manager.getTaskById(task.getId());
        assertEquals("New Label", newTask.getLabel());
        assertEquals("New Description", newTask.getDescription());
        assertEquals(Status.IN_PROGRESS, newTask.getStatus());
    }

    @Test
    void testEquals() {
        Task task = new Task("Label", "Description");
        task.setId(1);
        Task task2 = new Task("Label1", "Description1");
        task2.setId(1);
        assertEquals(task, task2);
        task2.setId(2);
        assertNotEquals(task, task2);
    }

    @Test
    void testHashCode() {
        Task task = new Task("Label", "Description");
        task.setId(1);
        Task task2 = new Task("Label1", "Description1");
        task2.setId(1);
        assertEquals(task.hashCode(), task2.hashCode());
        task2.setId(2);
        assertNotEquals(task.hashCode(), task2.hashCode());
    }
}