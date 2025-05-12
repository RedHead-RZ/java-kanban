/*
* Так как нет интерфейса и творим чистый бэк, то прикручен тестовый класс, где все проверяется
* */

public class Main {

    public static void main(String[] args) {
        System.out.println("Методы оптимизированы под работу с несклькими классами.");
        System.out.println("Чтобы избежать дублирования кода и не плодить разные списки.");
        TestApp.runTests();
    }
}
