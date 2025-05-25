package model;

import enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic;
    Subtask subtask;
    Subtask subtask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("Label", "Description");
        subtask = new Subtask("SubLabel", "SubDescription", epic);
        subtask.setId(1);
        subtask2 = new Subtask("SubLabel-2", "SubDescription-2", epic);
        subtask.setId(2);
        epic.addSubtask(subtask);
        epic.addSubtask(subtask2);
    }

    @Test
    void updateTask() {
        epic.setDescription("New Description");
        epic.setLabel("New Label");
        //изменение базовых пераметров
        assertEquals("New Description", epic.getDescription());
        assertEquals("New Label", epic.getLabel());
        //изменение статуса при изменении одного эпика In Progress
        subtask.setStatus(Status.IN_PROGRESS);
        subtask.updateTask(subtask);
        assertNotEquals(epic.getStatus(), subtask.getStatus());
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask2.updateTask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        //Done
        subtask.setStatus(Status.DONE);
        subtask.updateTask(subtask);
        assertNotEquals(epic.getStatus(), subtask.getStatus());
        subtask2.setStatus(Status.DONE);
        subtask2.updateTask(subtask2);
        assertEquals(Status.DONE, epic.getStatus());
        //Возврат статуса в начальный при удалении всех эпиков
        epic.removeSubtask(subtask);
        epic.removeSubtask(subtask2);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void getSubtasks() {
        assertEquals(subtask, epic.getSubtasks().getFirst());
    }

    @Test
    void addSubtask() {
        Subtask subtask3 = new Subtask("SubLabel-3", "SubDescription-3", epic);
        epic.addSubtask(subtask3);
        assertEquals(epic.getSubtasks().getLast(), subtask3);
    }

    @Test
    void removeSubtask() {
        epic.removeSubtask(subtask);
        assertNotEquals(epic.getSubtasks().getFirst(), subtask);
    }

    @Test
    void removeAllSubtasks() {
        epic.removeAllSubtasks();
        assertEquals(0, epic.getSubtasks().size());
    }
}