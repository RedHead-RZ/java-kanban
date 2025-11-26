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
        return "{" +
                "\"id\":" + getId() +
                ",\"label\":\"" + getLabel() +
                "\",\"description\":\"" + getDescription() +
                "\",\"status\":\"" + getStatus() +
                "\",\"startTime\":\"" + getStartTime() +
                "\",\"endTime\":\"" + getEndTime() +
                "\",\"subtasksCount\":" + getSubtasks().size() +
                "\"}";
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
                updateDuration();
                setStartTime(min.get().getStartTime());
                endTime = max.get().getEndTime();
            }
        }
        return endTime;
    }

    private void updateDuration() {
        setDuration(Duration.ZERO);
        getSubtasks().stream().filter(subtask -> subtask.getDuration() != null).forEach(subtask -> {
            setDuration(getDuration().plus(subtask.getDuration()));
        });
    }
}
