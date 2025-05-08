public class Subtask extends Task {

    private final int parentId;

    public Subtask(String label, String description, int parentId) {
        super(label, description);
        this.parentId = parentId;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        Epic subtask = (Epic) TaskManager.tasks.get(parentId);
        subtask.updateTask(task);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + "; Родительская задача: " + parentId;
    }

    public int getParentId() {
        return parentId;
    }
}
