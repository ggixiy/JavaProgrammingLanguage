package model;

import model.enums.AccessStatus;
import model.enums.PassType;
import java.time.LocalDateTime;

public class AccessResult {
    private final AccessStatus status;
    private final String passId;
    private final PassType passType;
    private final LocalDateTime time;

    public AccessResult(AccessStatus status, String passId, PassType passType, LocalDateTime time) {
        this.status = status;
        this.passId = passId;
        this.passType = passType;
        this.time = time;
    }

    public boolean isAllowed() {
        return status == AccessStatus.ALLOWED;
    }

    public AccessStatus getStatus() {
        return status;
    }

    public String getPassId() {
        return passId;
    }

    public PassType getPassType() {
        return passType;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
