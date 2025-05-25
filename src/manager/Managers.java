package manager;

public class Managers {
    private static Managers manager;

    public Managers() {
        if (manager == null) {
            manager = new Managers();
        }
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
