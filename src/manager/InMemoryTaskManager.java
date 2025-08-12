package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private static int counter = 0;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public Task addNewTask(Task task) {
        tasks.put(counter, task);
        task.setId(counter++);
        if (task instanceof Subtask subtask && subtask.getParentTask() != null) {
            Epic parent = subtask.getParentTask();
            parent.addSubtask(subtask);
            parent.updateTask(subtask.getParentTask());
        }
        return task;
    }

    @Override
    public <T> ArrayList<Task> getTasksByType(Class<T> type) {
        return new ArrayList<>(tasks.values()
                .stream().filter(task -> task.getClass().equals(type)).toList());
    }

    @Override
    public Task getTaskById(int id) {
        return getTaskById(id, true);
    }

    @Override
    public <T> void removeTasksByType(Class<T> taskType) {
        getTasksByType(taskType).forEach(task -> removeTaskById(task.getId()));
    }

    @Override
    public void removeTaskById(int id) {
        Task task = getTaskById(id, false);
        if (task != null) {
            switch (task) {
                case Subtask subtask:
                    subtask.removeFromParentTask();
                    break;
                case Epic epic:
                    epic.getSubtasks().forEach(this::removeSubtasks);
                    epic.removeAllSubtasks();
                    break;
                default:
                    break;
            }
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.get(task.getId()) != null) {
            return tasks.get(task.getId()).updateTask(task);
        }
        return null;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void removeSubtasks(Subtask subtask) {
        tasks.remove(subtask.getId());
        historyManager.remove(subtask.getId());
    }

    private Task getTaskById(int id, boolean updateHistory) {
        Task task = tasks.get(id) == null ? null : tasks.get(id);
        if (task != null && updateHistory) {
            historyManager.add(task);
        }
        return task;
    }
}
