package model;

import model.enums.AccessStatus;
import model.pass.SkiPass;

import java.time.LocalDateTime;
import java.util.Optional;

public class Turnstile {
    private final PassRegistry registry;
    private final TurnstileStatistics statistics;

    public Turnstile(PassRegistry registry, TurnstileStatistics statistics) {
        this.registry = registry;
        this.statistics = statistics;
    }

    public AccessResult processPass(String id, LocalDateTime now) {
        Optional<SkiPass> maybePass = registry.findById(id);

        AccessResult result;

        if (maybePass.isEmpty()) {
            result = new AccessResult(AccessStatus.READ_ERROR, id, null, now);
        } else {
            SkiPass pass = maybePass.get();
            AccessStatus status = pass.checkAndConsume(now);
            result = new AccessResult(status, id, pass.getType(), now);
        }

        statistics.record(result);
        return result;
    }
}
