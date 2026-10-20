package Task3;

import Task2.CryptReader;
import Task2.CryptWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.*;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final String BUNDLE_NAME = "location.messages";
    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.of("uk"));

    private static final Scanner sc = new Scanner(System.in);

    // рядок без параметрів
    private static String msg(String key) {
        return bundle.getString(key);
    }

    // рядок з параметрами
    private static String msg(String key, Object... args) {
        return new MessageFormat(bundle.getString(key), bundle.getLocale()).format(args);
    }

    private static void setupLogging() throws IOException {
        log.setUseParentHandlers(false); // прибрати дублювання
        log.setLevel(Level.FINE);        // перший фільтр

        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.WARNING); // у консоль лише проблеми
        log.addHandler(console);

        FileHandler file = new FileHandler("task3.log", true);  // true щоб дописувати, а не перезаписувати
        file.setLevel(Level.FINE);
        file.setFormatter(new SimpleFormatter());
        file.setEncoding("UTF-8");
        log.addHandler(file);
    }

    public static void encrypt(String file, char key) throws IOException {
        String temp = file + ".tmp";

        log.log(Level.FINE, "Створення тимчасового файлу {0}", temp);
        try (Reader in = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
             Writer out = new CryptWriter(
                     new BufferedWriter(new FileWriter(temp, StandardCharsets.UTF_8)), key)) {

            in.transferTo(out);
        }

        Files.move(Path.of(temp), Path.of(file), StandardCopyOption.REPLACE_EXISTING);
        log.log(Level.FINE, "Файл замінено");
    }

    public static void decrypt(String file, char key) throws IOException {
        String temp = file + ".tmp";

        log.log(Level.FINE, "Створення тимчасового файлу {0}", temp);
        try (Reader in = new CryptReader(
                new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)), key);
             Writer out = new BufferedWriter(new FileWriter(temp, StandardCharsets.UTF_8))) {

            in.transferTo(out);
        }

        Files.move(Path.of(temp), Path.of(file), StandardCopyOption.REPLACE_EXISTING);
        log.log(Level.FINE, "Файл замінено");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println(msg("menu.title"));
        System.out.println(msg("menu.encrypt"));
        System.out.println(msg("menu.decrypt"));
        System.out.println(msg("menu.language"));
        System.out.println(msg("menu.exit"));
        System.out.print(msg("prompt.choice") + " ");
    }

    private static void processFile(boolean isEncrypt) {
        System.out.print(msg("prompt.file") + " ");
        String file = sc.nextLine().trim();
        if (file.startsWith("\"") && file.endsWith("\"") && file.length() > 1) {
            file = file.substring(1, file.length() - 1);
        }
        log.log(Level.FINE, "Шлях до файлу: {0}", file);

        System.out.print(msg("prompt.key") + " ");
        String keyStr = sc.nextLine();
        if (keyStr.isEmpty()) {
            System.out.println(msg("error.emptyKey"));
            log.log(Level.WARNING, "Порожнє значення в ключі");
            return;
        }
        char key = keyStr.charAt(0);
        log.log(Level.FINEST, "Ключ шифрування: {0}", key); // не дійде нікуди: рівень логера FINE

        try {
            if (isEncrypt) {
                log.log(Level.INFO, "Початок шифрування");
                encrypt(file, key);
                log.log(Level.INFO, "Кінець шифрування");
                System.out.println(msg("result.encrypted"));
            } else {
                log.log(Level.INFO, "Початок дешифрування");
                decrypt(file, key);
                log.log(Level.INFO, "Кінець дешифрування");
                System.out.println(msg("result.decrypted"));
            }
        } catch (IOException e) {
            System.out.println(msg("error.file", file, e.getMessage()));
            log.log(Level.SEVERE, "Помилка роботи з файлом " + file, e);
        }
    }

    private static void chooseLanguage() {
        System.out.println(msg("language.title"));
        System.out.println(msg("language.uk"));
        System.out.println(msg("language.en"));
        System.out.print(msg("prompt.choice") + " ");

        Locale locale = switch (sc.nextLine().trim()) {
            case "1" -> Locale.of("uk");
            case "2" -> Locale.ENGLISH;
            default -> null;
        };

        if (locale == null) {
            System.out.println(msg("error.invalidChoice"));
            log.log(Level.WARNING, "Невірний вибір мови");
            return;
        }

        bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        System.out.println(msg("language.changed"));
        log.log(Level.INFO, "Мову інтерфейсу змінено на {0}", locale);
    }

    public static void main(String[] args) {
        try {
            setupLogging();
        } catch (IOException e) {
            System.err.println("Не вдалося створити файл логу: " + e.getMessage());
        }

        log.log(Level.INFO, "Початок роботи");

        while (true) {
            printMenu();
            if (!sc.hasNextLine()) {  // кінець вводу (наприклад, Ctrl+D) - завершуємо коректно
                break;
            }
            String choice = sc.nextLine().trim();
            log.log(Level.INFO, "Користувач обрав пункт меню: {0}", choice);

            switch (choice) {
                case "1" -> processFile(true);
                case "2" -> processFile(false);
                case "3" -> chooseLanguage();
                case "0" -> {
                    System.out.println(msg("app.exit"));
                    log.log(Level.INFO, "Завершення роботи");
                    return;
                }
                default -> {
                    System.out.println(msg("error.invalidChoice"));
                    log.log(Level.WARNING, "Невірний вибір пункту меню: {0}", choice);
                }
            }
        }
        log.log(Level.INFO, "Завершення роботи (кінець вводу)");
    }
}
