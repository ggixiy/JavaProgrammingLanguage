package view;

import model.AccessResult;
import model.TurnstileStatistics;
import model.enums.AccessStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println("\n=== Турнікет лижного підйомника ===");
        System.out.println("1. Провести картку (прохід)");
        System.out.println("2. Випустити нову картку");
        System.out.println("3. Заблокувати картку");
        System.out.println("4. Показати статистику");
        System.out.println("0. Вихід");
        System.out.print("Оберіть дію: ");
    }

    public int readMenuChoice() {
        String raw = scanner.nextLine().trim();
        return Integer.parseInt(raw);
    }

    public String promptPassId() {
        System.out.print("ID картки: ");
        return scanner.nextLine().trim();
    }

    public int promptPassCategory() {
        System.out.println("Тип картки: 1=за часом, 2=за кількістю поїздок, 3=сезонна");
        System.out.print("Оберіть: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    public <T extends Enum<T>> T promptEnum(Class<T> enumClass, String label) {
        T[] values = enumClass.getEnumConstants();
        while (true) {
            System.out.println(label + ":");
            for (int i = 0; i < values.length; i++) {
                System.out.println((i + 1) + ". " + values[i]);
            }
            System.out.print("Оберіть: ");
            String raw = scanner.nextLine().trim();
            try {
                int index = Integer.parseInt(raw);
                if (index >= 1 && index <= values.length) {
                    return values[index - 1];
                }
            } catch (NumberFormatException ignored) {
            }
            showError("Введіть число від 1 до " + values.length);
        }
    }

    public LocalDate promptDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        while (true) {
            System.out.print("Дата початку дії картки (dd.MM.yyyy): ");

            String raw = scanner.nextLine().trim();

            try {
                return LocalDate.parse(raw, formatter);
            } catch (DateTimeParseException e) {
                showError("Некоректна дата. Формат: dd.MM.yyyy");
            }
        }
    }

    public <T extends Enum<T>> T promptEnumFromList(String label, List<T> allowed) {
        while (true) {
            System.out.println(label + ":");
            for (int i = 0; i < allowed.size(); i++) {
                System.out.println((i + 1) + ". " + allowed.get(i));
            }
            System.out.print("Оберіть: ");
            String raw = scanner.nextLine().trim();
            try {
                int index = Integer.parseInt(raw);
                if (index >= 1 && index <= allowed.size()) {
                    return allowed.get(index - 1);
                }
            } catch (NumberFormatException ignored) {
            }
            showError("Введіть число від 1 до " + allowed.size());
        }
    }

    public int promptRideCount(Set<Integer> allowed) {
        List<Integer> sorted = allowed.stream().sorted().toList();
        while (true) {
            System.out.print("Кількість поїздок (" + sorted + "): ");
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (allowed.contains(value)) {
                    return value;
                }
                showError("Дозволено лише: " + sorted);
            } catch (NumberFormatException e) {
                showError("Введіть число зі списку: " + sorted);
            }
        }
    }

    public void showAccessResult(AccessResult result) {
        if (result.isAllowed()) {
            System.out.printf("[ДОЗВОЛЕНО] Картка %s (%s)%n", result.getPassId(), result.getPassType());
        } else {
            System.out.printf("[ВІДМОВА] Картка %s — причина: %s%n",
                    result.getPassId(), describeStatus(result.getStatus()));
        }
    }

    private String describeStatus(AccessStatus status) {
        return switch (status) {
            case READ_ERROR -> "картку не знайдено / не вдалося зчитати";
            case BLOCKED -> "картку заблоковано";
            case EXPIRED -> "термін дії картки закінчився";
            case NOT_YET_ACTIVE -> "картка ще не почала діяти";
            case NO_RIDES_LEFT ->  "на картці не залишилося поїздок";
            default -> status.toString();
        };
    }

    public void showStatistics(TurnstileStatistics stats) {
        System.out.println("\n--- Загальна статистика ---");
        System.out.println("Дозволено проходів: " + stats.getTotalAllowed());
        System.out.println("Відмовлено проходів: " + stats.getTotalDenied());

        System.out.println("\n--- Дозволено по типах ---");
        stats.getAllowedByType().forEach((type, count) -> System.out.printf("%s: %d%n", type, count));

        System.out.println("\n--- Відмовлено по типах ---");
        stats.getDeniedByType().forEach((type, count) -> System.out.printf("%s: %d%n", type, count));
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println("Помилка: " + message);
    }
}