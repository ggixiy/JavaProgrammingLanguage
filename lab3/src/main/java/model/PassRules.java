package model;

import model.enums.DayCategory;
import model.enums.TimeWindow;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PassRules {
    private static final Set<Integer> ALLOWED_RIDE_COUNTS = Set.of(10, 20, 50, 100);

    public static List<TimeWindow> allowedWindows(DayCategory day) {
        return Arrays.stream(TimeWindow.values())
                .filter(w -> day != DayCategory.WEEKEND || w != TimeWindow.FIVE_DAYS)
                .toList();
    }

    public static boolean isWindowAllowed(DayCategory day, TimeWindow window) {
        return allowedWindows(day).contains(window);
    }

    public static boolean isRideCountAllowed(int rides) {
        return ALLOWED_RIDE_COUNTS.contains(rides);
    }

    public static Set<Integer> allowedRideCounts() {
        return ALLOWED_RIDE_COUNTS;
    }
}