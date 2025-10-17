package model;

import enums.Status;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {

    TaskManager manager;
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }


    @Test
    void updateTask() {
        Task task = new Task("Label", "Description", now, Duration.ofHours(3));
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
        assertEquals(now.plus(Duration.ofHours(3)), newTask.getEndTime());
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