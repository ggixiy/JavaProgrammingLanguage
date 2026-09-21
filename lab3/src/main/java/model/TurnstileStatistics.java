package model;

import model.enums.PassType;

import java.util.EnumMap;
import java.util.Map;

public class TurnstileStatistics {
    private int totalAllowed = 0;
    private int totalDenied = 0;
    private final Map<PassType, Integer> allowedByType = new EnumMap<>(PassType.class);
    private final Map<PassType, Integer> deniedByType = new EnumMap<>(PassType.class);

    public void record(AccessResult result) {
        if (result.isAllowed()) {
            totalAllowed++;
            if (result.getPassType() != null) {
                allowedByType.merge(result.getPassType(), 1, Integer::sum);
            }
        } else {
            totalDenied++;
            if (result.getPassType() != null) {
                deniedByType.merge(result.getPassType(), 1, Integer::sum);
            }

        }
    }

    public int getTotalAllowed() {
        return totalAllowed;
    }

    public int getTotalDenied() {
        return totalDenied;
    }

    public Map<PassType, Integer> getAllowedByType() {
        return allowedByType;
    }

    public Map<PassType, Integer> getDeniedByType() {
        return deniedByType;
    }
}
