import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String label, String description) {
        super(label, description);
    }

    @Override
    public Task updateTask(Task task) {
        this.setLabel(task.getLabel());
        this.setDescription(task.getDescription());

        if (this.checkSubtaskStatus(Status.NEW)) {
            setStatus(Status.NEW);
        } else if (this.checkSubtaskStatus(Status.IN_PROGRESS)) {
            setStatus(Status.IN_PROGRESS);
        } else setStatus(Status.DONE);
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
    }
}
