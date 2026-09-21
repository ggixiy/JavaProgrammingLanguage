import controller.TurnstileController;
import model.PassFactory;
import model.PassRegistry;
import model.Turnstile;
import model.TurnstileStatistics;
import view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        PassRegistry registry = new PassRegistry();
        PassFactory factory = new PassFactory();
        TurnstileStatistics statistics = new TurnstileStatistics();
        Turnstile turnstile = new Turnstile(registry, statistics);
        ConsoleView view = new ConsoleView();
        TurnstileController controller =
                new TurnstileController(turnstile, registry, factory, statistics, view);

        boolean running = true;
        while (running) {
            view.showMenu();
            int choice;
            try {
                choice = view.readMenuChoice();
            } catch (NumberFormatException e) {
                view.showError("Введіть число з меню");
                continue;
            }

            switch (choice) {
                case 1 -> controller.handleSwipe();
                case 2 -> controller.handleIssuePass();
                case 3 -> controller.handleBlock();
                case 4 -> controller.handleShowStatistics();
                case 0 -> running = false;
                default -> view.showError("Невідома команда");
            }
        }
        System.out.println("Роботу завершено.");
    }
}