package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class BaseHttpHandlerTest {

    @BeforeEach
    public void setUp() throws IOException {
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() throws IOException {
        HttpTaskServer.stop();
    }



}
