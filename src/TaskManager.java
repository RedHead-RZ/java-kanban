import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    static HashMap<Integer, Task> tasks = new HashMap<>();

    public Task addNewTask(Task task) {
        tasks.put(task.getId(), task);
        Task.incrementCounter(); //учитываем увеличение счетчика только при сохранении задачи
        return task;
    }

    public Subtask addNewSubtask(Subtask subtask) {
        if (getTaskById(subtask.getParentId()) != null) {
            tasks.put(subtask.getId(), subtask);
            Epic epic = (Epic) getTaskById(subtask.getParentId());
            epic.addSubtask(subtask);
            Task.incrementCounter(); //учитываем увеличение счетчика только при сохранении задачи
            return subtask;
        }
        return null;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Task> getTasksByType(String type) {
        ArrayList<Task> tasksByType = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getClass().getSimpleName().equals(type)) {
                tasksByType.add(task);
            }
        }
        return tasksByType;
    }

    public static Task getTaskById(int id) {
        return tasks.get(id) == null ? null : tasks.get(id);
    }

    public void removeAllTasks() {
        tasks.clear();
        Task.setCounter(0);
    }

    public void removeTaskById(int id) {
        if (getTaskById(id) != null) {
            if (Objects.requireNonNull(getTaskById(id)).getClass().equals(Epic.class)) {
                Epic epic = (Epic) getTaskById(id);
                ArrayList<Subtask> subtasks = epic != null ? epic.getSubtasks() : null;
                if (subtasks != null) {
                    for (int i = subtasks.size() - 1; i > -1; i--) {
                        removeTaskById(subtasks.get(i).getId());
                    }
                }
            }
            if (getTaskById(id).getClass().equals(Subtask.class)) {
                Subtask subtask = (Subtask) getTaskById(id);
                Epic epic = (Epic) getTaskById(Objects.requireNonNull(subtask).getParentId());
                epic.getSubtasks().remove(subtask);
            }
            tasks.remove(id);
        }
    }
}
