package service;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocalDateTimeTypeAdapterTest {

    private LocalDateTimeTypeAdapter adapter;
    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        adapter = new LocalDateTimeTypeAdapter();
    }

    @Test
    void testWriteNonNullLocalDateTime() throws IOException {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        LocalDateTime dateTime = LocalDateTime.of(2025, 11, 11, 11, 33, 44);
        adapter.write(jsonWriter, dateTime);
        jsonWriter.close();
        String result = stringWriter.toString();
        assertEquals("\"2025-11-11T11:33:44\"", result);
    }

    @Test
    void testWriteNullLocalDateTime() throws IOException {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        adapter.write(jsonWriter, null);
        jsonWriter.close();
        String result = stringWriter.toString();
        assertEquals("null", result);
    }

    @Test
    void testReadNonNullLocalDateTime() throws IOException {
        String json = "\"2025-11-11T11:33:44\"";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        LocalDateTime result = adapter.read(jsonReader);
        LocalDateTime expected = LocalDateTime.of(2025, 11, 11, 11, 33, 44);
        assertEquals(expected, result);
    }

    @Test
    void testReadNullLocalDateTime() throws IOException {
        String json = "null";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        LocalDateTime result = adapter.read(jsonReader);
        assertNull(result);
    }
}
