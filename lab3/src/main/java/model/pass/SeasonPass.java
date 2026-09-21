package model.pass;

import model.enums.AccessStatus;
import model.enums.PassType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SeasonPass extends SkiPass {
    private final LocalDate seasonStart;
    private final LocalDate seasonEnd;

    public SeasonPass(String id, LocalDate seasonStart, LocalDate seasonEnd) {
        super(id);
        this.seasonStart = seasonStart;
        this.seasonEnd = seasonEnd;
    }

    @Override
    public AccessStatus checkAndConsumeIfNotBlocked(LocalDateTime now) {
        if (now.toLocalDate().isBefore(seasonStart)) {
            return AccessStatus.NOT_YET_ACTIVE;
        }
        if (now.toLocalDate().isAfter(seasonEnd)) {
            return AccessStatus.EXPIRED;
        }
        return AccessStatus.ALLOWED;
    }

    @Override
    public PassType getType() {
        return PassType.SEASON;
    }

    @Override
    public String describeDetails() {
        return String.format("Сезонний абонемент: %s — %s", seasonStart, seasonEnd);
    }
}