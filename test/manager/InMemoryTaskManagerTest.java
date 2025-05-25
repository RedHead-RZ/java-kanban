package manager;

import enums.Status;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager manager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
        task = new Task("Label", "Description");
        epic = new Epic("Label-E", "Description-E");
        subtask = new Subtask("Label-S", "Description-S", epic);
        manager.addNewTask(task);
        manager.addNewTask(epic);
        manager.addNewTask(subtask);
    }

    @Test
    void addNewTask() {
        //Task
        assertEquals("Label", manager.getTaskById(task.getId()).getLabel());
        assertEquals("Description", manager.getTaskById(task.getId()).getDescription());
        assertEquals(Status.NEW, manager.getTaskById(task.getId()).getStatus());
        //Epic
        assertEquals("Label-E", manager.getTaskById(epic.getId()).getLabel());
        assertEquals("Description-E", manager.getTaskById(epic.getId()).getDescription());
        assertEquals(Status.NEW, manager.getTaskById(epic.getId()).getStatus());
        //Subtask
        assertEquals("Label-S", manager.getTaskById(subtask.getId()).getLabel());
        assertEquals("Description-S", manager.getTaskById(subtask.getId()).getDescription());
        assertEquals(Status.NEW, manager.getTaskById(subtask.getId()).getStatus());
        assertEquals(epic, manager.getTaskById(subtask.getParentTask().getId()));
    }

    @Test
    void getTasksByType() {
        assertEquals(manager.getTasksByType(Task.class).getFirst(), task);
        assertEquals(manager.getTasksByType(Epic.class).getFirst(), epic);
        assertEquals(manager.getTasksByType(Subtask.class).getFirst(), subtask);
    }

    @Test
    void getTaskById() {
        assertEquals(task, manager.getTaskById(task.getId()));
        assertEquals(epic, manager.getTaskById(epic.getId()));
        assertEquals(subtask, manager.getTaskById(subtask.getId()));
    }

    @Test
    void removeTasksByType() {
        manager.removeTasksByType(Task.class);
        assertEquals(0, manager.getTasksByType(Task.class).size());
        manager.removeTasksByType(Epic.class);
        assertEquals(0, manager.getTasksByType(Epic.class).size());
        manager.removeTasksByType(Subtask.class);
        assertEquals(0, manager.getTasksByType(Subtask.class).size());
    }

    @Test
    void removeTaskById() {
        manager.removeTaskById(task.getId());
        assertNull(manager.getTaskById(task.getId()));
        manager.removeTaskById(epic.getId());
        assertNull(manager.getTaskById(epic.getId()));
        manager.removeTaskById(subtask.getId());
        assertNull(manager.getTaskById(subtask.getId()));
    }

    @Test
    void updateTask() {
        //Task
        task.setLabel("ILabel");
        task.setDescription("IDescription");
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus());
        assertEquals("IDescription", manager.getTaskById(task.getId()).getDescription());
        assertEquals("ILabel", manager.getTaskById(task.getId()).getLabel());
        //Epic
        epic.setLabel("ILabel-E");
        epic.setDescription("IDescription-E");
        epic.setStatus(Status.IN_PROGRESS);
        manager.updateTask(epic);
        assertEquals("IDescription-E", manager.getTaskById(epic.getId()).getDescription());
        assertEquals("ILabel-E", manager.getTaskById(epic.getId()).getLabel());
        //статус меняется только при апдейте сабтаски
        assertEquals(Status.NEW, manager.getTaskById(epic.getId()).getStatus());
        //Subtask
        subtask.setLabel("ILabel-S");
        subtask.setDescription("IDescription-S");
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateTask(subtask);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(subtask.getId()).getStatus());
        assertEquals("IDescription-S", manager.getTaskById(subtask.getId()).getDescription());
        assertEquals("ILabel-S", manager.getTaskById(subtask.getId()).getLabel());
    }

    @Test
    void getHistory() {
        manager.getTaskById(task.getId());
        manager.getTaskById(epic.getId());
        manager.getTaskById(subtask.getId());
        assertEquals(3, manager.getHistory().size());
        assertEquals(manager.getHistory().get(0), task);
        assertEquals(manager.getHistory().get(1), epic);
        assertEquals(manager.getHistory().get(2), subtask);
    }
}