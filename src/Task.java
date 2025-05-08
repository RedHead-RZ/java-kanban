public class Task {
    static int counter = 0;
    private final int id;
    private String label;
    private String description;
    Status status;

    public Task(String label, String description) {
        this.label = label;
        this.description = description;
        this.id = counter;
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

    public int getId() {
        return id;
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

    public static void incrementCounter() {
        counter++;
    }

    public static void setCounter(int newCounter) {
        counter = newCounter;
    }
}
