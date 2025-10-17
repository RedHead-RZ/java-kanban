package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileBackedTaskManagerTest {

    LocalDateTime now = LocalDateTime.now();

    /**
     * Одним методом проверяются два кейса. Чтобы избежать дублирования кода
     */
    @Test
    void saveAndLoadFromFile() {
        TaskManager tm = Managers.getFileBackedManager();
        Task task = new Task("Label", "Description", now, Duration.ofHours(3));
        Epic epic = new Epic("Label-E", "Description-E");
        Subtask subtask = new Subtask("Label-S", "Description-S", epic, now, Duration.ofHours(3));
        tm.addNewTask(task);
        tm.addNewTask(epic);
        tm.addNewTask(subtask);
        TaskManager tmFromFile = Managers.getFileBackedManager();
        assertEquals(tm.getTaskById(task.getId()).getLabel(), tmFromFile.getTaskById(task.getId()).getLabel());
        assertEquals(tm.getTaskById(epic.getId()).getLabel(), tmFromFile.getTaskById(epic.getId()).getLabel());
        assertEquals(task.getStartTime(), tmFromFile.getTaskById(task.getId()).getStartTime());
        assertEquals(task.getDuration(), tmFromFile.getTaskById(task.getId()).getDuration());
    }

    @Test
    void addFromApp() {
        TaskManager tm = Managers.getFileBackedManager();
        Task task = new Task("Label", "Description", now, Duration.ofHours(3));
        Epic epic = new Epic("Label-E", "Description-E");
        Subtask subtask = new Subtask("Label-S", "Description-S", epic, now, Duration.ofHours(3));
        tm.addNewTask(task);
        tm.addNewTask(epic);
        tm.addNewTask(subtask);
        assertEquals(tm.getTaskById(task.getId()).getLabel(), task.getLabel());
        assertEquals(tm.getTaskById(epic.getId()).getLabel(), epic.getLabel());
        assertEquals(task.getStartTime(), tm.getTaskById(task.getId()).getStartTime());
        assertEquals(task.getDuration(), tm.getTaskById(task.getId()).getDuration());
    }

    @Test
    void remove() {
        TaskManager tm = Managers.getFileBackedManager();
        Task task = new Task("Label", "Description");
        Epic epic = new Epic("Label-E", "Description-E");
        Subtask subtask = new Subtask("Label-S", "Description-S", epic);
        tm.addNewTask(task);
        tm.addNewTask(epic);
        tm.addNewTask(subtask);
        tm.removeTaskById(task.getId());
        assertNull(tm.getTaskById(task.getId()));
        assertEquals(tm.getTaskById(epic.getId()).getLabel(), epic.getLabel());
        assertEquals(tm.getTaskById(subtask.getId()).getLabel(), subtask.getLabel());
    }
}
