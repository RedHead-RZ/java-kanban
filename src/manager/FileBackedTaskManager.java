package manager;

import enums.Status;
import exceptions.ManagerFileLoadException;
import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static Path filePath = null;

    private FileBackedTaskManager() {
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        filePath = file.toPath();
        if (!Files.exists(filePath)) {
            return new FileBackedTaskManager();
        }
        try {
            FileBackedTaskManager manager = new FileBackedTaskManager();
            manager.parseFile(Files.readAllLines(filePath));
            return manager;
        } catch (IOException e) {
            throw new ManagerFileLoadException("Ошибка при загрузке задач из файла: " + e.getMessage());
        }
    }

    @Override
    public Task addNewTask(Task task) {
        Task resultTask = super.addNewTask(task);
        save();
        return resultTask;
    }

    @Override
    public <T> void removeTasksByType(Class<T> taskType) {
        super.removeTasksByType(taskType);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public Task updateTask(Task task) {
        Task resultTask = super.updateTask(task);
        save();
        return resultTask;
    }

    private void parseFile(List<String> lines) {
        if (lines.size() <= 1) return;
        for (int i = 1; i < lines.size(); i++) {
            Task task = fromString(lines.get(i));
            if (task != null) {
                super.addNewTask(task);
            }
        }
    }

    private void save() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                bw.write("id,type,name,status,description,duration,startTime,epic");
                bw.newLine();
                for (Task task : getTasks()) {
                    bw.write(formatTaskToCSV(task));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения задач в файл: " + e.getMessage());
        }
    }

    private String formatTaskToCSV(Task task) {
        if (task instanceof Subtask subtask) {
            return String.format("%d,%s,%s,%s,%s,%s,%s,%d",
                    subtask.getId(),
                    subtask.getClass().getSimpleName(),
                    subtask.getLabel(),
                    subtask.getStatus(),
                    subtask.getDescription(),
                    subtask.getDuration() != null ? subtask.getDuration().toMinutes() : 0,
                    subtask.getStartTime() != null ? subtask.getStartTime() : 0,
                    subtask.getParentTask().getId());

        } else {
            return String.format("%d,%s,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getClass().getSimpleName(),
                    task.getLabel(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getDuration() != null ? task.getDuration().toMinutes() : 0,
                    task.getStartTime() != null ? task.getStartTime() : 0);
        }
    }

    private Task fromString(String value) {
        String[] taskProps = value.split(",");
        Task task = null;
        if (taskProps.length > 0) {
            switch (taskProps[1]) {
                case "Task": {
                    task = new Task(taskProps[2], taskProps[4]);
                }
                case "Epic": {
                    task = new Epic(taskProps[2], taskProps[4]);
                    break;
                }
                case "Subtask": {
                    task = new Subtask(taskProps[2], taskProps[4], (Epic) getTaskById(Integer.parseInt(taskProps[7])));
                    break;
                }
            }
            if (task != null) {
                setTaskParamsFromFile(taskProps, task);
            }
        }
        return task;
    }

    private void setTaskParamsFromFile(String[] taskProps, Task task) {
        task.setId(Integer.parseInt(taskProps[0]));
        task.setStatus(Status.valueOf(taskProps[3]));
        task.setDuration(Duration.ofMinutes(Integer.parseInt(taskProps[5])));
        if (!taskProps[6].equals("0")) {
            task.setStartTime(LocalDateTime.parse(taskProps[6]));
        }
    }
}
