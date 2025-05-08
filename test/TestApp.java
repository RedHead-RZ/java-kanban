/*
Класс для тестов. Когда-то тут будут Юнит-тесты, но пока бюджет не выделили (нас не научили)
 */

public class TestApp {
    public static void runTests() {
        TaskManager taskManager = new TaskManager();
        populateDataForTest(taskManager);

        //получаем спсиок всех задач
        System.out.println("~~Test getAllTasks");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }
        System.out.println();

        //получам список задач по типу
        System.out.println("~~Test getTaskByType");
        System.out.println("-Task"); //Task
        for (Task t : taskManager.getTasksByType("Task")) {
            System.out.println(t);
        }
        System.out.println("-Epic"); //Epic
        for (Task t : taskManager.getTasksByType("Epic")) {
            System.out.println(t);
        }
        System.out.println("-Subtask"); //Subtask
        for (Task t : taskManager.getTasksByType("Subtask")) {
            System.out.println(t);
        }
        System.out.println();

        //удаляем все задачи
        System.out.println("~~Test removeAllTasks");
        taskManager.removeAllTasks();
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }
        populateDataForTest(taskManager); //заполняем заново

        //удаление задачи по id
        System.out.println("~~Test removeTaskById");
        //удаляем тип задачи Task
        taskManager.removeTaskById(0);
        System.out.println("Remove ID: 0");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }
        System.out.println();
        //удаляем тип задачи Subtask
        taskManager.removeTaskById(6);
        System.out.println("Remove ID: 6");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }
        System.out.println();
        taskManager.removeTaskById(3);
        //удаляем тип задачи Epic
        System.out.println("Remove ID: 3");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }

        //подчищаем после тестов и заполняем заново
        taskManager.removeAllTasks();
        populateDataForTest(taskManager);
        System.out.println();

        //проверка изменения задачи
        System.out.println("~~Test updateTask");
        System.out.println("-Task"); //Task
        Task task = TaskManager.getTaskById(0);
        task.setLabel("Кормим попугая");
        task.setDescription("Ошибочка вышла");
        task.setStatus(Status.IN_PROGRESS);
        System.out.println(TaskManager.getTaskById(0).updateTask(task));

        System.out.println("-Epic"); //Epic
        Epic epic = (Epic) TaskManager.getTaskById(3);
        epic.setLabel("Празднуем новый год");
        epic.setDescription("С НГ чтоль");
        System.out.println(TaskManager.getTaskById(3).updateTask(epic));

        System.out.println("-Subtask"); //Subtask
        Subtask subtask = (Subtask) TaskManager.getTaskById(8);
        subtask.setStatus(Status.DONE);
        subtask.updateTask(subtask);
        System.out.println(TaskManager.getTaskById(4));
    }

    static void populateDataForTest(TaskManager taskManager) {
        Task task = new Task("Покормить кота", "Только паштет, только из определенного магазина.");
        taskManager.addNewTask(task);
        Task task2 = new Task("Пройти урок иностранного языка", "Надо, пока сова из Duolingo "
                + "снова не начала угрожать моей семье");
        taskManager.addNewTask(task2);
        Task task3 = new Task("Поспать", "Когда-нибудь");
        taskManager.addNewTask(task3);

        Epic epic = new Epic("Организовать день рождения", "Пора бы");
        taskManager.addNewTask(epic);
        Epic epic2 = new Epic("Сделать финалку", "Тоже пора бы");
        taskManager.addNewTask(epic2);

        Subtask subtask = new Subtask("Собрать гостей", "Желательно за 2 месяца", epic.getId());
        taskManager.addNewSubtask(subtask);
        subtask = new Subtask("Забронировтаь заведение", "Где будет весело", epic.getId());
        taskManager.addNewSubtask(subtask);
        subtask = new Subtask("Провести мероприятия", "Желательно выжить", epic.getId());
        taskManager.addNewSubtask(subtask);
        subtask = new Subtask("Отправить задачу", "Желательно", epic2.getId());
        taskManager.addNewSubtask(subtask);

    }
}
