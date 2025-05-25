package manager;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Label", "Description");
        historyManager.add(task);
        //Проверка на добавление
        assertEquals(1, historyManager.getHistory().size());
        //проверили на добавление
        assertEquals("Label", historyManager.getHistory().getFirst().getLabel());
        for (int i = 0; i < 10; i++) {
            historyManager.add(new Task("Label" + i, "Description" + i));
        }
        //проверка на удаление первых элементов при переполнении списка истории
        assertNotEquals("Label", historyManager.getHistory().getFirst().getLabel());
        //проверка на размер списка при переполнении
        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Task("Label", "Description"));
        //проверка вохврата списка историичности
        assertNotNull(historyManager.getHistory());
    }
}