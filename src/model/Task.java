package model;


import enums.Status;

public class Task {
    private int id;
    private String label;
    private String description;
    private Status status;

    public Task(String label, String description) {
        this.label = label;
        this.description = description;
        this.status = Status.NEW;
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
        return this.id == task.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + id;
    }

    public int getId() {
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
}
