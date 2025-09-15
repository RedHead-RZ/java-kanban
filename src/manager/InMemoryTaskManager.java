package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private static int counter = 0;
    private final HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks;
    private static final Comparator<Task> TASK_COMPARATOR = Comparator.comparing(Task::getStartTime)
            .thenComparing(Task::getEndTime);

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(TASK_COMPARATOR);
    }

    @Override
    public Task addNewTask(Task task) {
        if (task.getId() == null) {
            task.setId(counter);
        }
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null && !(task instanceof Epic)) {
            prioritizedTasks.add(task);
        }
        if (task instanceof Subtask subtask && subtask.getParentTask() != null) {
            Epic parent = subtask.getParentTask();
            parent.addSubtask(subtask);
            parent.updateTask(subtask.getParentTask());
        }
        counter++;
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
        getTasksByType(taskType).forEach(task -> {
            removeTaskById(task.getId());
            prioritizedTasks.remove(task);
        });

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
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.get(task.getId()) != null) {
            if (task.getStartTime() != null && !(task instanceof Epic)) prioritizedTasks.add(task);
            return tasks.get(task.getId()).updateTask(task);
        }
        return null;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values().stream().sorted(Comparator.comparing(Task::getId)).toList());
    }

    public ArrayList<Task> getPrioritizedTasks() {
        return (ArrayList<Task>) prioritizedTasks.stream()
                .filter(task -> task.getStartTime() != null)
                .sorted(Comparator.comparing(Task::getStartTime))
                .collect(Collectors.toList());
    }

    public boolean hasTimeOverlap(Task task) {
        if (prioritizedTasks.size() <= 1) {
            return false;
        }

        Task taskBefore = prioritizedTasks.lower(task) == null ? task : prioritizedTasks.lower(task);
        Task taskAfter = prioritizedTasks.higher(task) == null ? task : prioritizedTasks.higher(task);

        if (taskBefore != null && hasOverlapBetweenTasks(taskBefore, task)) {
            return true;
        }
        return taskAfter != null && hasOverlapBetweenTasks(task, taskAfter);
    }

    private void removeSubtasks(Subtask subtask) {
        tasks.remove(subtask.getId());
        prioritizedTasks.remove(subtask);
        historyManager.remove(subtask.getId());
    }

    private Task getTaskById(int id, boolean updateHistory) {
        Task task = tasks.get(id) == null ? null : tasks.get(id);
        if (task != null && updateHistory) {
            historyManager.add(task);
        }
        return task;
    }

    private boolean hasOverlapBetweenTasks(Task first, Task second) {
        LocalDateTime firstStart = first.getStartTime();
        LocalDateTime firstEnd = first.getEndTime();
        LocalDateTime secondStart = second.getStartTime();
        LocalDateTime secondEnd = second.getEndTime();

        return (firstStart.isBefore(secondEnd) && firstEnd.isAfter(secondStart)) ||
                (firstStart.equals(secondStart) && firstEnd.equals(secondEnd));
    }
}
