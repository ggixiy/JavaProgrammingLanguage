package model;

import model.enums.DayCategory;
import model.enums.TimeWindow;
import model.pass.RideBasedSkiPass;
import model.pass.SeasonPass;
import model.pass.TimeBasedSkiPass;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PassFactory {
    public TimeBasedSkiPass createTimeBasedPass(String id, DayCategory day, TimeWindow window, LocalDate date) {
        DayCategory actualDay =
                switch (date.getDayOfWeek()) {
                    case SATURDAY, SUNDAY -> DayCategory.WEEKEND;
                    default -> DayCategory.WEEKDAY;
                };

        if (actualDay != day) {
            throw new IllegalArgumentException(
                    "Обрана категорія " + day +
                            " не відповідає даті " + date +
                            ". Для цієї дати потрібно: " + actualDay
            );
        }

        if (!PassRules.isWindowAllowed(day, window)) {
            throw new IllegalArgumentException(
                    window + " недоступне для " + day +
                            ". Доступні: " + PassRules.allowedWindows(day));
        }

        LocalDateTime from;
        LocalDateTime validTo;

        switch (window) {
            case MORNING_HALF -> {
                from = date.atTime(9, 0);
                validTo = date.atTime(13, 0);
            }
            case AFTERNOON_HALF -> {
                from = date.atTime(13, 0);
                validTo = date.atTime(17, 0);
            }
            case ONE_DAY -> {
                from = date.atStartOfDay();
                validTo = from.plusDays(1);
            }
            case TWO_DAYS -> {
                from = date.atStartOfDay();
                validTo = from.plusDays(2);
            }
            case FIVE_DAYS -> {
                from = date.atStartOfDay();
                validTo = from.plusDays(5);
            }
            default -> throw new IllegalArgumentException(
                    "Невідомий тип часової картки");
        }

        return new TimeBasedSkiPass(id, day, window, from, validTo);
    }

    public RideBasedSkiPass createRideBasedPass(String id, DayCategory day, int rides) {
        if (!PassRules.isRideCountAllowed(rides)) {
            throw new IllegalArgumentException(
                    "Дозволена кількість поїздок: " + PassRules.allowedRideCounts());
        }
        return new RideBasedSkiPass(id, day, rides);
    }

    public SeasonPass createSeasonPass(String id, LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Дата завершення сезону не може бути раніше дати початку");
        }
        return new SeasonPass(id, start, end);
    }
}