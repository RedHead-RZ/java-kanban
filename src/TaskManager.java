import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private static int counter = 0;

    /*
     * Метод для добавления Task и Epic
     * */
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

    public <T> ArrayList<Task> getTasksByType(Class<T> type) {
        return new ArrayList<>(tasks.values()
                .stream().filter(task -> task.getClass().equals(type)).toList());
    }

    public Task getTaskById(int id) {
        return tasks.get(id) == null ? null : tasks.get(id);
    }

    public <T> void removeTasksByType(Class<T> taskType) {
        tasks.values().removeIf(task -> task.getClass().equals(taskType));
    }

    public void removeTaskById(int id) {
        if (getTaskById(id) != null) {
            Task task = getTaskById(id);
            switch (task) {
                case Subtask subtask:
                    subtask.removeFromParentTask();
                    break;
                case Epic epic:
                    epic.removeAllSubtasks();
                    break;
                default:
                    break;
            }
            tasks.remove(id);
        }
    }

    public Task updateTask(Task task) {
        if (tasks.get(task.getId()) != null) {
            return tasks.get(task.getId()).updateTask(task);
        }
        return null;
    }
}
