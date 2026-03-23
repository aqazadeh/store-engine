package az.kon.academy.aggragate.valueobject;

public enum ProcessStatus {
    IN_PROGRESS,
    FAILED,
    COMPLETED,
    ROLL_BACKED;

    public Boolean isInProgress() {
        return this == IN_PROGRESS;
    }

    public Boolean isFailed() {
        return this == FAILED;
    }

    public Boolean isCompleted() {
        return this == COMPLETED;
    }

    public Boolean isRollBacked() {
        return this == ROLL_BACKED;
    }
}
