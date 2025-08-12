package manager;

import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    @Test
    void add() {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("Label", "Description"));
        taskManager.getTaskById(3);
        assertEquals(1, taskManager.getHistory().size()); //проверка добавления одного элемента
        for (int i = 1; i <= 10; i++) {
            taskManager.addNewTask(new Task("Label-" + i, "Description-" + i));
            taskManager.getTaskById(i);
        }
        assertEquals(8, taskManager.getHistory().size());
        taskManager.getTaskById(0);    //проверка на добавление первой ноды
        assertEquals("Label-7", taskManager.getHistory().getLast().getLabel());
        taskManager.getTaskById(5);    //проверка на добавление ноды из середины мапы
        assertEquals("Label-2", taskManager.getHistory().getLast().getLabel());
        taskManager.getTaskById(5); //дополнительная на доабвднеие последней ноды
        assertEquals("Label-2", taskManager.getHistory().getLast().getLabel());
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Task("Label", "Description"));
        //проверка возврата списка историичности
        assertNotNull(historyManager.getHistory());
    }

    @Test
    void remove() {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(new Task("Label", "Description"));
        taskManager.addNewTask(new Task("Label-2", "Description-2"));
        taskManager.addNewTask(new Task("Label-3", "Description-3"));
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(0);
        taskManager.removeTaskById(1);
        assertEquals(2, taskManager.getHistory().size());//удаление элемента из середины
        taskManager.removeTaskById(0);
        assertEquals(1, taskManager.getHistory().size());//удаление элемента из середины
        taskManager.removeTaskById(2);
        assertEquals(0, taskManager.getHistory().size());//удаление элемента из середины
    }
}