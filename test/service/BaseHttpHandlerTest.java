package service;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseHttpHandlerTest {

    private BaseHttpHandler handler;
    private TestHttpExchange testExchange;

    @BeforeEach
    public void setUp() throws IOException {
        HttpTaskServer.start();
        handler = new BaseHttpHandler() {
            @Override
            public void handle(HttpExchange exchange) {
            }
        };
        testExchange = new TestHttpExchange("GET", "/test", null);
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    void sendTextShouldSetCorrectMessage() throws IOException {
        String responseText = "{\"message\":\"test\"}";

        handler.sendText(testExchange.getDelegate(), responseText, 200);

        assertEquals(200, testExchange.getResponseCode());
        assertEquals(responseText, testExchange.getResponseBodyAsString());
    }

    @Test
    void sendTaskOverlapMessage() throws IOException {
        handler.sendTaskOverlap(testExchange.getDelegate());
        assertEquals(406, testExchange.getResponseCode());
        assertEquals("Имеются пересечения с существующими задачами", testExchange.getResponseBodyAsString());
    }

    @Test
    void sendTaskNotFoundMessage() throws IOException {
        handler.sendTaskNotFound(testExchange.getDelegate());
        assertEquals(404, testExchange.getResponseCode());
        assertEquals("Задача не найдена", testExchange.getResponseBodyAsString());
    }

    @Test
    void sendUnavailableMethodMessage() throws IOException {
        testExchange = new TestHttpExchange("GET", "/test", null);
        handler.sendUnavailableMethod(testExchange.getDelegate());
        assertEquals(405, testExchange.getResponseCode());
        assertEquals("Метод не поддерживается", testExchange.getResponseBodyAsString());
    }

    @Test
    void getRequestedBody() throws IOException {
        String expectedBody = "{\"name\":\"test\", \"value\":123}";
        testExchange = new TestHttpExchange("POST", "/tasks", expectedBody);
        String result = handler.getRequestBody(testExchange.getDelegate());

        assertEquals(expectedBody, result);
    }

    @Test
    void parseIdCorrect() throws IOException {
        int id = handler.parseId(testExchange.getDelegate(), "23");
        assertEquals(23, id);
    }

    @Test
    void parseIdIncorrect() throws IOException {
        testExchange = new TestHttpExchange("POST", "/tasks", null);
        handler.parseId(testExchange.getDelegate(), "abc");
        assertEquals("Неверный ID", testExchange.getResponseBodyAsString());
    }

    @Test
    void handleDeleteSuccess() throws IOException {
        Task task = new Task("Label", "Description");
        BaseHttpHandler.manager.addNewTask(task);
        testExchange = new TestHttpExchange("DELETE", "/tasks/" + task.getId(), null);
        handler.getRequestParams(testExchange.getDelegate());
        handler.handleDelete(testExchange.getDelegate(), handler.paths);
        assertEquals(200, testExchange.getResponseCode());
        assertEquals("Задача удалена", testExchange.getResponseBodyAsString());
    }

    @Test
    void handleDeleteNotFoundTask() throws IOException {
        testExchange = new TestHttpExchange("DELETE", "/tasks/909", null);
        handler.getRequestParams(testExchange.getDelegate());
        handler.handleDelete(testExchange.getDelegate(), handler.paths);
        assertEquals(404, testExchange.getResponseCode());
        assertEquals("Задача не найдена", testExchange.getResponseBodyAsString());
    }

    @Test
    void handleDeleteIncorrectId() throws IOException {
        testExchange = new TestHttpExchange("DELETE", "/tasks/1/qq", null);
        handler.getRequestParams(testExchange.getDelegate());
        handler.handleDelete(testExchange.getDelegate(), handler.paths);
        assertEquals(404, testExchange.getResponseCode());
        assertEquals("Некорректный запрос", testExchange.getResponseBodyAsString());
    }

    @Test
    void getRequestParamsCorrect() {
        testExchange = new TestHttpExchange("GET", "/tasks", null);
        handler.getRequestParams(testExchange.getDelegate());
        assertEquals("tasks", handler.paths[1]);
        assertEquals("GET", testExchange.getDelegate().getRequestMethod());
    }
}
