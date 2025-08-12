package manager;

import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    @Test
    void add() {
        TaskManager taskManager = Managers.getDefault();

        for (int i = 1; i <= 10; i++) {
            taskManager.addNewTask(new Task("Label-" + i, "Description-" + i));
            taskManager.getTaskById(i);
        }
        int id = taskManager.getHistory().getFirst().getId();
        taskManager.getTaskById(id);    //проверка на добавление первой ноды
        assertEquals(id, taskManager.getHistory().getLast().getId());
        id = taskManager.getHistory().getLast().getId();
        taskManager.getTaskById(taskManager.getHistory().getLast().getId()); //добавлнеие последней ноды
        assertEquals(id, taskManager.getHistory().getLast().getId());
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