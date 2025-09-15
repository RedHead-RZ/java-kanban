package model;


import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private Integer id;
    private String label;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String label, String description) {
        this.label = label;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String label, String description, LocalDateTime startTime, Duration duration) {
        this(label, description);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    public Task updateTask(Task task) {
        this.label = task.label;
        this.description = task.description;
        this.status = task.status;
        return this;
    }

    @Override
    public String toString() {
        return "Тип задачи: " + this.getClass().getName() + "; Название: " + label
                + "; Описание: " + description + "; Статус: " + status + "; ID: " + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Task task = (Task) obj;
        return this.id.equals(task.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + id;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
