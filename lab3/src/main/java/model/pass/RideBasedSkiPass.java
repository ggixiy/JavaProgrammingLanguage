package model.pass;

import model.enums.AccessStatus;
import model.enums.DayCategory;
import model.enums.PassType;
import java.time.LocalDateTime;

public class RideBasedSkiPass extends SkiPass {
    private final DayCategory dayCategory;
    private final int totalRides;
    private int remainingRides;

    public RideBasedSkiPass(String id, DayCategory dayCategory, int totalRides) {
        super(id);
        this.dayCategory = dayCategory;
        this.totalRides = totalRides;
        this.remainingRides = totalRides;
    }

    @Override
    public AccessStatus checkAndConsumeIfNotBlocked(LocalDateTime now) {
        DayCategory currentDay =
                switch (now.getDayOfWeek()) {
                    case SATURDAY, SUNDAY -> DayCategory.WEEKEND;
                    default -> DayCategory.WEEKDAY;
                };

        if (currentDay != dayCategory) {
            return AccessStatus.NOT_YET_ACTIVE;
        }

        if (remainingRides <= 0) {
            return AccessStatus.NO_RIDES_LEFT;
        }

        remainingRides--;

        return AccessStatus.ALLOWED;
    }

    @Override
    public PassType getType() {
        return dayCategory == DayCategory.WEEKDAY ? PassType.WEEKDAY_RIDE : PassType.WEEKEND_RIDE;
    }

    @Override
    public String describeDetails() {
        return String.format("%s, залишилось поїздок: %d з %d", dayCategory, remainingRides, totalRides);
    }

    public int getRemainingRides() {
        return remainingRides;
    }
}
