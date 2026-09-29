package Task3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Task3 {

    // шифрування
    public static void encrypt(String file, char key) throws IOException {
        String temp = file + ".tmp";

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
    }

    // дешифрування
    public static void decrypt(String file, char key) throws IOException {
        String temp = file + ".tmp";

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
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("1 - шифрувати, 2 - дешифрувати: ");
            String choice = sc.nextLine().trim();
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Невірний вибір!");
                return;
            }

            System.out.print("Вхідний файл: ");
            String file = sc.nextLine().trim();
            if (file.startsWith("\"") && file.endsWith("\"")) {
                file = file.substring(1, file.length() - 1);
            }

            System.out.print("Ключовий символ: ");
            String keyStr = sc.nextLine();
            if (keyStr.isEmpty()) {
                System.out.println("Ключ не може бути порожнім!");
                return;
            }
            char key = keyStr.charAt(0);

            if (choice.equals("1")) {
                encrypt(file, key);
                System.out.println("Файл зашифровано.");
            } else {
                decrypt(file, key);
                System.out.println("Файл розшифровано.");
            }
        } catch (IOException e) {
            System.out.println("Помилка роботи з файлом: " + e.getMessage());
        }
    }
}