package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    @Test
    void add() {
        TaskManager taskManager = Managers.getDefault();
        int id;
        for (int i = 1; i <= 10; i++) {
            id = taskManager.addNewTask(new Task("Label-" + i, "Description-" + i)).getId();
            taskManager.getTaskById(id);
        }
        assertEquals(10 , taskManager.getHistory().size());
        id = taskManager.getHistory().getFirst().getId();
        taskManager.getTaskById(id);    //проверка на перестановку первой ноды
        assertEquals(id, taskManager.getHistory().getLast().getId());
        id = taskManager.getHistory().getLast().getId();
        taskManager.getTaskById(id); //добавлнеие последней ноды
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
        taskManager.removeTasksByType(Task.class);
        taskManager.removeTasksByType(Epic.class);
        taskManager.removeTasksByType(Subtask.class);
        Task task = new Task("Label", "Description");
        Task task2 = new Task("Label-2", "Description-2");
        Task task3 = new Task("Label-3", "Description-3");
        taskManager.addNewTask(task);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.removeTaskById(taskManager.getHistory().getLast().getId());
        assertEquals(2, taskManager.getHistory().size());//удаление элемента из середины
        taskManager.removeTaskById(taskManager.getHistory().getFirst().getId());
        assertEquals(1, taskManager.getHistory().size());//удаление элемента из начала
        taskManager.removeTaskById(taskManager.getHistory().getFirst().getId());
        assertEquals(0, taskManager.getHistory().size());//удаление последнего элемента
    }
}