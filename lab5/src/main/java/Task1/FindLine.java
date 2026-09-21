package Task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FindLine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Шлях до файлу: ");
        String path = sc.nextLine();

        if (path.startsWith("\"") && path.endsWith("\"")) {
            path = path.substring(1, path.length() - 1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            String bestLine = null;
            int maxWords = -1;
            String line;

            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                int count = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
                if (count > maxWords) {
                    maxWords = count;
                    bestLine = line;
                }
            }

            if (bestLine == null) {
                System.out.println("Файл порожній");
            } else {
                System.out.println("Рядок з максимальною кількістю слів (" + maxWords + "):");
                System.out.println(bestLine);
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
        }
    }
}