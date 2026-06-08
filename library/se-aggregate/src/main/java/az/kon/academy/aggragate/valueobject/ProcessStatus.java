package az.kon.academy.aggragate.valueobject;

/**
 * Represents the lifecycle status of a long-running process
 * associated with an aggregate root.
 */
public enum ProcessStatus {
    /** The process is currently executing. */
    IN_PROGRESS,
    /** The process has failed. */
    FAILED,
    /** The process has completed successfully. */
    COMPLETED,
    /** The process was rolled back. */
    ROLL_BACKED;

    /**
     * Checks whether the process is in progress.
     *
     * @return {@code true} if the status is {@link #IN_PROGRESS}
     */
    public Boolean isInProgress() {
        return this == IN_PROGRESS;
    }

    /**
     * Checks whether the process has failed.
     *
     * @return {@code true} if the status is {@link #FAILED}
     */
    public Boolean isFailed() {
        return this == FAILED;
    }

    /**
     * Checks whether the process has completed.
     *
     * @return {@code true} if the status is {@link #COMPLETED}
     */
    public Boolean isCompleted() {
        return this == COMPLETED;
    }

    /**
     * Checks whether the process was rolled back.
     *
     * @return {@code true} if the status is {@link #ROLL_BACKED}
     */
    public Boolean isRollBacked() {
        return this == ROLL_BACKED;
    }
}
