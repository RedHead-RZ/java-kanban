package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private Epic parentTask;

    public Subtask(String label, String description, Epic parentTask) {
        super(label, description);
        this.parentTask = parentTask;
    }

    public Subtask(String label, String description, Epic parentTask, LocalDateTime startTime, Duration duration) {
        this(label, description, parentTask);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        parentTask.updateTask(task);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + "; Родительская задача: " + parentTask.getLabel();
    }

    public Epic getParentTask() {
        return parentTask;
    }

    public void removeFromParentTask() {
        parentTask.removeSubtask(this);
        parentTask.updateTask(parentTask);
    }
}
