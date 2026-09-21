package model.pass;

import model.enums.AccessStatus;
import model.enums.PassType;
import java.time.LocalDateTime;

public abstract class SkiPass {
    private final String id;
    private boolean blocked;

    public SkiPass(String id) {
        this.id = id;
        this.blocked = false;
    }

    public String getId() {
        return id;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void block() {
        this.blocked = true;
    }

    public final AccessStatus checkAndConsume(LocalDateTime now) {
        if (blocked) {
            return AccessStatus.BLOCKED;
        }
        return checkAndConsumeIfNotBlocked(now);
    }

    public abstract AccessStatus checkAndConsumeIfNotBlocked(LocalDateTime now);
    public abstract PassType getType();
    public abstract String describeDetails();

}

