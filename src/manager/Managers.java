package manager;

import java.io.File;

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

    public static TaskManager getFileBackedManager() {
        return FileBackedTaskManager.loadFromFile(new File("resources/TaskBase.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
