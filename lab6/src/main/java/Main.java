import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        Translator translator = new Translator();
        fillDefaultDictionary(translator);

        System.out.println("=== Перекладач English -> Українська ===");
        System.out.println("У словнику вже є " + translator.size() + " слів.");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addWordFromKeyboard(scanner, translator);
                case "2" -> translatePhrase(scanner, translator);
                case "3" -> printDictionary(translator);
                case "0" -> running = false;
                default -> System.out.println("Невідомий пункт меню, спробуйте ще раз.");
            }
        }
        System.out.println("До побачення!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 - Додати слово до словника");
        System.out.println("2 - Перекласти фразу");
        System.out.println("3 - Показати словник");
        System.out.println("0 - Вихід");
        System.out.print("Ваш вибір: ");
    }

    private static void fillDefaultDictionary(Translator t) {
        t.addWord("hello", "привіт");
        t.addWord("world", "світ");
        t.addWord("i", "я");
        t.addWord("you", "ти");
        t.addWord("love", "люблю");
        t.addWord("java", "джава");
        t.addWord("is", "є");
        t.addWord("a", "один");
        t.addWord("good", "добрий");
        t.addWord("day", "день");
        t.addWord("my", "мій");
        t.addWord("name", "ім'я");
        t.addWord("program", "програма");
        t.addWord("book", "книга");
        t.addWord("read", "читати");
    }

    private static void addWordFromKeyboard(Scanner scanner, Translator t) {
        System.out.print("Англійське слово: ");
        String english = scanner.nextLine();
        System.out.print("Український переклад: ");
        String ukrainian = scanner.nextLine();

        if (t.addWord(english, ukrainian)) {
            System.out.println("Пару додано. Розмір словника: " + t.size());
        } else {
            System.out.println("Помилка: слово та переклад не можуть бути порожніми.");
        }
    }

    private static void translatePhrase(Scanner scanner, Translator t) {
        System.out.print("Введіть фразу англійською: ");
        String phrase = scanner.nextLine();
        if (phrase.isBlank()) {
            System.out.println("Фраза порожня.");
            return;
        }
        System.out.println("Переклад: " + t.translate(phrase));
    }

    private static void printDictionary(Translator t) {
        Map<String, String> sorted = new TreeMap<>(t.getDictionary());
        System.out.println("Словник (" + sorted.size() + " слів):");
        sorted.forEach((en, ua) -> System.out.printf("  %-12s -> %s%n", en, ua));
    }
}