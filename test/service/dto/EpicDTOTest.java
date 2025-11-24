package service.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import org.junit.jupiter.api.Test;
import service.DurationTypeAdapter;
import service.LocalDateTimeTypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicDTOTest {

    @Test
    public void toEpic() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        EpicDTO epicDTO = gson.fromJson("{\"label\": \"Epic Label\",\"description\": \"Epic Description\"}", EpicDTO.class);
        Epic epic = epicDTO.toEpic();
        assertEquals("Epic Label", epic.getLabel());
        assertEquals("Epic Description", epic.getDescription());
    }

    @Test
    public void toEpicDTO() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        Epic epic = new Epic("Epic Label", "Epic Description");
        EpicDTO epicDTO = EpicDTO.toEpicDTO(epic);
        String json = gson.toJson(epicDTO);
        assertEquals("{\"label\":\"Epic Label\",\"description\":\"Epic Description\",\"status\":\"NEW\"}", json);
    }
}
