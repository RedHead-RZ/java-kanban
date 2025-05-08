import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String label, String description) {
        super(label, description);
    }

    @Override
    public Task updateTask(Task task) {
        if (!(task instanceof Subtask)) {
            setLabel(task.getLabel());
            setDescription(task.getDescription());
        } else {
            if (this.checkSubtaskStatus(Status.NEW) && task.getStatus().equals(Status.IN_PROGRESS)) {
                setStatus(task.getStatus());
            }
            if ((this.checkSubtaskStatus(Status.IN_PROGRESS)
                    || this.checkSubtaskStatus(Status.NEW))
                    && task.getStatus().equals(Status.DONE)) {
                setStatus(task.getStatus());
            }
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Subtask subtask : subtasks) {
            str.append("\n     ").append(subtask.toString());
        }
        return super.toString() + "; Спсиок задач:" + str;
    }

    private boolean checkSubtaskStatus(Status status) {
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (subtask.getStatus().equals(status)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        this.subtasks.add(subtask);
    }
}
