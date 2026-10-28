package com.lab11;

import com.lab11.dao.CompanyDao;
import com.lab11.db.DatabaseConnection;
import com.lab11.model.Employee;
import com.lab11.model.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final CompanyDao dao;
    private final Scanner in;

    public Main(CompanyDao dao, Scanner in) {
        this.dao = dao;
        this.in = in;
    }

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();

        try (Connection c = db.getConnection()) {
            System.out.println("З'єднання з БД встановлено: " + db.getUrl()
                    + " (" + c.getMetaData().getDatabaseProductName()
                    + " " + c.getMetaData().getDatabaseProductVersion() + ")");
        } catch (SQLException e) {
            System.err.println("Не вдалося з'єднатися з БД " + db.getUrl() + ": " + e.getMessage());
            return;
        }

        new Main(new CompanyDao(db), new Scanner(System.in)).run();
    }

    private void run() {
        while (true) {
            printMenu();
            String choice = in.hasNextLine() ? in.nextLine().trim() : "0";
            try {
                switch (choice) {
                    case "1" -> printEmployees("Усі співробітники", dao.findAllEmployees());
                    case "2" -> printTasks("Усі завдання", dao.findAllTasks());
                    case "3" -> {
                        int dept = readInt("Номер відділу: ");
                        printEmployees("Співробітники відділу №" + dept, dao.findEmployeesByDepartment(dept));
                    }
                    case "4" -> addTask();
                    case "5" -> {
                        int emp = readInt("Номер співробітника: ");
                        printTasks("Завдання співробітника №" + emp, dao.findTasksByEmployee(emp));
                    }
                    case "6" -> deleteEmployee();
                    case "0" -> {
                        System.out.println("Завершення роботи.");
                        return;
                    }
                    default -> System.out.println("Невідомий пункт меню.");
                }
            } catch (SQLException e) {
                System.out.println("Помилка БД: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("""

                МЕНЮ
                1 - Список всіх співробітників
                2 - Список всіх завдань
                3 - Співробітники зазначеного відділу
                4 - Додати завдання співробітнику
                5 - Завдання зазначеного співробітника
                6 - Видалити співробітника
                0 - Вихід""");
        System.out.print("Ваш вибір: ");
    }

    private void addTask() throws SQLException {
        int emp = readInt("Номер співробітника: ");
        if (!dao.employeeExists(emp)) {
            System.out.println("Співробітника №" + emp + " не існує.");
            return;
        }
        String description;
        do {
            System.out.print("Опис завдання (до 50 символів): ");
            description = in.nextLine().trim();
        } while (description.isEmpty() || description.length() > 50);
        int id = dao.addTask(emp, description);
        System.out.println("Додано завдання №" + id + " для співробітника №" + emp + ".");
    }

    private void deleteEmployee() throws SQLException {
        int emp = readInt("Номер співробітника для видалення: ");
        if (dao.deleteEmployee(emp)) {
            System.out.println("Співробітника №" + emp + " та його завдання видалено.");
        } else {
            System.out.println("Співробітника №" + emp + " не знайдено.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Введіть ціле число.");
            }
        }
    }

    private static void printEmployees(String title, List<Employee> employees) {
        System.out.println("\n--- " + title + " ---");
        if (employees.isEmpty()) {
            System.out.println("(немає даних)");
            return;
        }
        System.out.println(Employee.header());
        employees.forEach(System.out::println);
    }

    private static void printTasks(String title, List<Task> tasks) {
        System.out.println("\n--- " + title + " ---");
        if (tasks.isEmpty()) {
            System.out.println("(немає даних)");
            return;
        }
        System.out.println(Task.header());
        tasks.forEach(System.out::println);
    }
}
