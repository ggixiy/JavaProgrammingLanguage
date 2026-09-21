package model.pass;

import model.enums.AccessStatus;
import model.enums.DayCategory;
import model.enums.PassType;
import model.enums.TimeWindow;

import java.time.LocalDateTime;

public class TimeBasedSkiPass extends SkiPass {
    private final DayCategory dayCategory;
    private final TimeWindow timeWindow;
    private final LocalDateTime validFrom;
    private final LocalDateTime validTo;

    public TimeBasedSkiPass(String id, DayCategory dayCategory, TimeWindow timeWindow,
                            LocalDateTime validFrom, LocalDateTime validTo) {
        super(id);
        if (validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException(
                    "Дата завершення не може бути раніше дати початку");
        }
        this.dayCategory = dayCategory;
        this.timeWindow = timeWindow;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    @Override
    public AccessStatus checkAndConsumeIfNotBlocked(LocalDateTime now) {
        if (now.isBefore(validFrom)) {
            return AccessStatus.NOT_YET_ACTIVE;
        }

        if (now.isAfter(validTo)) {
            return AccessStatus.EXPIRED;
        }

        return AccessStatus.ALLOWED;
    }

    @Override
    public PassType getType() {
        return dayCategory == DayCategory.WEEKDAY ? PassType.WEEKDAY_TIME : PassType.WEEKEND_TIME;
    }

    @Override
    public String describeDetails() {
        return String.format("%s, вікно: %s, дійсна з %s до %s",
                dayCategory, timeWindow, validFrom, validTo);
    }

    public DayCategory getDayCategory() {
        return dayCategory;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }
}