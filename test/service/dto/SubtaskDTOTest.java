package service.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DurationTypeAdapter;
import service.LocalDateTimeTypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskDTOTest {
    Epic epic;
    Gson gson;
    TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager.removeTasksByType(Epic.class);
        taskManager.removeTasksByType(Subtask.class);
        taskManager = Managers.getDefault();
        epic = new Epic("Label-E", "Description-E");
        taskManager.addNewTask(epic);
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }


    @Test
    public void toSubtask() {
        SubtaskDTO subtaskDTO = gson.fromJson("{\"label\": \"Label-S\","
                + "\"description\": \"Description-S\",\"parentEpicId\": 0}", SubtaskDTO.class);
        Subtask subtask = subtaskDTO.toSubtask(taskManager);
        assertEquals("Label-S", subtask.getLabel());
        assertEquals("Description-S", subtask.getDescription());
        assertEquals(0, subtask.getParentTask().getId());
    }

    @Test
    public void toSubtaskDTO() {
        Subtask subtask = new Subtask("Label-S", "Description-S", epic);
        taskManager.addNewTask(subtask);
        int actualEpicId = epic.getId();
        int actualSubtaskId = subtask.getId();
        SubtaskDTO subtaskDTO = SubtaskDTO.toSubtaskDTO(subtask);
        String json = gson.toJson(subtaskDTO);
        assertEquals("{\"id\":" + actualSubtaskId + ",\"label\":\"Label-S\","
                + "\"description\":\"Description-S\",\"status\":\"NEW\",\"parentEpicId\":" + actualEpicId + "}", json);
    }
}
