package manager;

import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task addNewTask(Task task);

    <T> ArrayList<Task> getTasksByType(Class<T> type);

    Task getTaskById(int id);

    <T> void removeTasksByType(Class<T> taskType);

    void removeTaskById(int id);

    Task updateTask(Task task);

    ArrayList<Task> getHistory();
}
