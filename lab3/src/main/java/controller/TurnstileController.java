package controller;

import model.PassFactory;
import model.PassRegistry;
import model.PassRules;
import model.AccessResult;
import model.Turnstile;
import model.TurnstileStatistics;
import model.enums.DayCategory;
import model.enums.TimeWindow;
import model.pass.SkiPass;
import view.ConsoleView;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TurnstileController {
    private final Turnstile turnstile;
    private final PassRegistry registry;
    private final PassFactory factory;
    private final TurnstileStatistics statistics;
    private final ConsoleView view;

    public TurnstileController(Turnstile turnstile, PassRegistry registry, PassFactory factory,
                               TurnstileStatistics statistics, ConsoleView view) {
        this.turnstile = turnstile;
        this.registry = registry;
        this.factory = factory;
        this.statistics = statistics;
        this.view = view;
    }

    public void handleSwipe() {
        String passId = view.promptPassId();
        AccessResult result = turnstile.processPass(passId, LocalDateTime.now());
        view.showAccessResult(result);
    }

    public void handleIssuePass() {
        int type = view.promptPassCategory();
        try {
            switch (type) {
                case 1 -> issueTimeBasedPass();
                case 2 -> issueRideBasedPass();
                case 3 -> issueSeasonPass();
                default -> view.showError("Невідомий тип картки");
            }
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    private void issueTimeBasedPass() {
        String id = view.promptPassId();
        DayCategory day = view.promptEnum(DayCategory.class, "День");
        TimeWindow window = view.promptEnumFromList(
                "Вікно", PassRules.allowedWindows(day));

        LocalDate date = view.promptDate();

        SkiPass pass = factory.createTimeBasedPass(id, day, window, date);
        registry.register(pass);
        view.showMessage("Картку " + id + " успішно випущено (" + pass.describeDetails() + ")");
    }

    private void issueRideBasedPass() {
        String id = view.promptPassId();
        DayCategory day = view.promptEnum(DayCategory.class, "День");
        int rides = view.promptRideCount(PassRules.allowedRideCounts());

        SkiPass pass = factory.createRideBasedPass(id, day, rides);
        registry.register(pass);
        view.showMessage("Картку " + id + " успішно випущено (" + pass.describeDetails() + ")");
    }

    private void issueSeasonPass() {
        String id = view.promptPassId();
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusMonths(3);

        SkiPass pass = factory.createSeasonPass(id, start, end);
        registry.register(pass);
        view.showMessage("Картку " + id + " успішно випущено (" + pass.describeDetails() + ")");
    }

    public void handleBlock() {
        String passId = view.promptPassId();
        boolean blocked = registry.blockPass(passId);
        if (blocked) {
            view.showMessage("Картку " + passId + " заблоковано");
        } else {
            view.showError("Картку з таким ID не знайдено");
        }
    }

    public void handleShowStatistics() {
        view.showStatistics(statistics);
    }
}