package Task3;

import java.io.*;
import java.util.Scanner;

public class Task3 {

    // a. шифрування
    public static void encrypt(String src, String dst, char key) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new CryptOutputStream(
                     new BufferedOutputStream(new FileOutputStream(dst)), key)) {
            in.transferTo(out);
        }
    }

    // b. дешифрування
    public static void decrypt(String src, String dst, char key) throws IOException {
        try (InputStream in = new CryptInputStream(
                new BufferedInputStream(new FileInputStream(src)), key);
             OutputStream out = new FileOutputStream(dst)) {
            in.transferTo(out);
        }
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
            String src = sc.nextLine().trim();
            System.out.print("Вихідний файл: ");
            String dst = sc.nextLine().trim();
            System.out.print("Ключовий символ: ");
            String keyStr = sc.nextLine();
            if (keyStr.isEmpty()) {
                System.out.println("Ключ не може бути порожнім!");
                return;
            }
            char key = keyStr.charAt(0);

            if (choice.equals("1")) {
                encrypt(src, dst, key);
                System.out.println("Файл зашифровано.");
            } else {
                decrypt(src, dst, key);
                System.out.println("Файл розшифровано.");
            }
        } catch (IOException e) {
            System.out.println("Помилка роботи з файлом: " + e.getMessage());
        }
    }
}
