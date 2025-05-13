public class Subtask extends Task {

    private Epic parentTask;

    public Subtask(String label, String description, Epic parentTask) {
        super(label, description);
        this.parentTask = parentTask;
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
