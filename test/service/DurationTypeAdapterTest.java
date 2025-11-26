package service;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DurationTypeAdapterTest {
    private DurationTypeAdapter adapter;
    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        adapter = new DurationTypeAdapter();
    }

    @Test
    void testWriteNonNullDuration() throws IOException {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        Duration duration = Duration.ofMinutes(120);
        adapter.write(jsonWriter, duration);
        jsonWriter.close();
        String result = stringWriter.toString();
        assertEquals("120", result);
    }

    @Test
    void testWriteZeroDuration() throws IOException {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        Duration duration = Duration.ZERO;
        adapter.write(jsonWriter, duration);
        jsonWriter.close();
        String result = stringWriter.toString();
        assertEquals("0", result);
    }

    @Test
    void testReadNonNullDuration() throws IOException {
        String json = "120";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Duration result = adapter.read(jsonReader);
        Duration expected = Duration.ofMinutes(120);
        assertEquals(expected, result);
    }

    @Test
    void testReadZeroDuration() throws IOException {
        String json = "0";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        Duration result = adapter.read(jsonReader);
        assertEquals(Duration.ZERO, result);
    }
}
