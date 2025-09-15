package model;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String label, String description) {
        super(label, description);
    }

    @Override
    public Task updateTask(Task task) {
        if (task instanceof Epic) {
            this.setLabel(task.getLabel());
            this.setDescription(task.getDescription());
        }

        if (this.checkSubtaskStatus(Status.IN_PROGRESS)) {
            setStatus(Status.IN_PROGRESS);
        } else if (this.checkSubtaskStatus(Status.DONE)) {
            setStatus(Status.DONE);
        } else setStatus(Status.NEW); //если список сабтасок пустой устанавливаем стартовый статус
        endTime = getEndTime(); //обновляем параметры времени выполнения эпика

        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Subtask subtask : subtasks) {
            str.append("\n     ").append(subtask.toString());
        }
        return super.toString() + "; Список задач:" + str;
    }

    private boolean checkSubtaskStatus(Status status) {
        if (status == Status.DONE && !subtasks.isEmpty()) {
            return subtasks.stream().allMatch(task -> task.getStatus().equals(status));
        }
        if (!subtasks.isEmpty()) {
            return subtasks.stream().anyMatch(task -> task.getStatus().equals(status));
        }
        return false;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        this.subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        this.subtasks.remove(subtask);
        this.updateTask(this);
    }

    public void removeAllSubtasks() {
        this.subtasks.clear();
        this.updateTask(this);
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasks.isEmpty()) {
            setDuration(null);
            setStartTime(null);
            endTime = null;
            return null;
        }
        Optional<Subtask> min = subtasks.stream().filter(subtask -> subtask.getEndTime() != null)
                .min(Comparator.comparing(Task::getEndTime));
        Optional<Subtask> max = subtasks.stream().filter(subtask -> subtask.getEndTime() != null)
                .max(Comparator.comparing(Task::getEndTime));
        if (min.isPresent() && max.isPresent()) {
            if (min.get().getEndTime() != null && max.get().getEndTime() != null) {
                updateDuration(min.get().getStartTime(), max.get().getEndTime());
                setStartTime(min.get().getStartTime());
                endTime = min.get().getEndTime();
            }
        }
        return endTime;
    }

    private void updateDuration(LocalDateTime min, LocalDateTime max) {
        setDuration(Duration.between(min, max));
    }
}
