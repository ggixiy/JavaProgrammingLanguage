package task2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.*;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    private static void setupLogging() throws IOException {
        log.setUseParentHandlers(false); // прибрати дублювання
        log.setLevel(Level.FINE);        // перший фільтр

        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.WARNING); // у консоль лише проблеми
        log.addHandler(console);

        FileHandler file = new FileHandler("task2.log", true);  // true щоб дописувати, а не перезаписувати
        file.setLevel(Level.FINE);
        file.setFormatter(new SimpleFormatter());
        file.setEncoding("UTF-8");
        log.addHandler(file);
    }

    // шифрування
    public static void encrypt(String file, char key) throws IOException {
        String temp = file + ".tmp";

        log.log(Level.FINE, "Створення тимчасового файлу {0}", temp);
        try (Reader in = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
             Writer out = new CryptWriter(
                     new BufferedWriter(new FileWriter(temp, StandardCharsets.UTF_8)), key)) {

            in.transferTo(out);
        }

        Files.move(
                Path.of(temp),
                Path.of(file),
                StandardCopyOption.REPLACE_EXISTING
        );
        log.log(Level.FINE, "Файл замінено");
    }

    // дешифрування
    public static void decrypt(String file, char key) throws IOException {
        String temp = file + ".tmp";

        log.log(Level.FINE, "Створення тимчасового файлу {0}", temp);
        try (Reader in = new CryptReader(
                new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)), key);
             Writer out = new BufferedWriter(new FileWriter(temp, StandardCharsets.UTF_8))) {

            in.transferTo(out);
        }

        Files.move(
                Path.of(temp),
                Path.of(file),
                StandardCopyOption.REPLACE_EXISTING
        );
        log.log(Level.FINE, "Файл замінено");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            setupLogging();
        } catch (IOException e) {
            System.out.println("Не вдалося створити файл логу: " + e.getMessage());
        }

        log.log(Level.INFO, "Початок роботи");

        while (true) {
            System.out.print("\n1 - шифрувати, 2 - дешифрувати, 0 - вихід: ");
            if (!sc.hasNextLine()) {
                log.log(Level.INFO, "Завершення роботи (кінець вводу)");
                return;
            }
            String choice = sc.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Завершення роботи.");
                log.log(Level.INFO, "Завершення роботи");
                return;
            }
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Невірний вибір!");
                log.log(Level.WARNING, "Невірний вибір варіантів: {0}", choice);
                continue;
            }
            log.log(Level.INFO, "Користувач обрав: {0}", choice);

            System.out.print("Вхідний файл: ");
            String file = sc.nextLine().trim();
            if (file.length() > 1 && file.startsWith("\"") && file.endsWith("\"")) {
                file = file.substring(1, file.length() - 1);
            }
            log.log(Level.FINE, "Шлях до файлу: {0}", file);

            System.out.print("Ключовий символ: ");
            String keyStr = sc.nextLine();
            if (keyStr.isEmpty()) {
                System.out.println("Ключ не може бути порожнім!");
                log.log(Level.WARNING, "Порожнє значення в ключі");
                continue;
            }
            char key = keyStr.charAt(0);
            log.log(Level.FINEST, "Ключ шифрування: {0}", key); // не дійде нікуди: рівень логера FINE

            try {
                if (choice.equals("1")) {
                    log.log(Level.INFO, "Початок шифрування");
                    encrypt(file, key);
                    log.log(Level.INFO, "Кінець шифрування");
                    System.out.println("Файл зашифровано.");
                } else {
                    log.log(Level.INFO, "Початок дешифрування");
                    decrypt(file, key);
                    log.log(Level.INFO, "Кінець дешифрування");
                    System.out.println("Файл розшифровано.");
                }
            } catch (IOException e) {
                System.out.println("Помилка роботи з файлом: " + e.getMessage());
                log.log(Level.SEVERE, "Помилка роботи з файлом " + file, e);
            }
        }
    }
}