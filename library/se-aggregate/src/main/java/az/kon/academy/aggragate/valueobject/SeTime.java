package az.kon.academy.aggragate.valueobject;

import java.time.OffsetTime;

/**
 * An immutable value object representing a time of day with offset.
 * <p>
 * Wraps an {@link OffsetTime} and provides comparison methods
 * (isAfter, isBefore, isEqual) and factory methods.
 */
public final class SeTime {
    private final OffsetTime value;

    private SeTime(OffsetTime value) {
        this.value = value;
    }

    /**
     * Returns the current time.
     *
     * @return a new {@code SeTime} representing now
     */
    public static SeTime now() {
        return new SeTime(OffsetTime.now());
    }

    /**
     * Creates a new {@code SeTime} from an {@link OffsetTime}.
     *
     * @param date the time value
     * @return a new {@code SeTime} instance
     */
    public static SeTime of(OffsetTime date) {
        return new SeTime(date);
    }

    /**
     * Returns the underlying time value.
     *
     * @return the {@link OffsetTime} value
     */
    public OffsetTime value() {
        return value;
    }

    /**
     * Checks whether this time is strictly after the given time.
     *
     * @param other the time to compare to
     * @return {@code true} if this time is after
     */
    public Boolean isAfter(SeTime other) {
        return value.isAfter(other.value);
    }

    /**
     * Checks whether this time is strictly before the given time.
     *
     * @param other the time to compare to
     * @return {@code true} if this time is before
     */
    public Boolean isBefore(SeTime other) {
        return value.isBefore(other.value);
    }

    /**
     * Checks whether this time equals the given time.
     *
     * @param other the time to compare to
     * @return {@code true} if the times are equal
     */
    public Boolean isEqual(SeTime other) {
        return value.isEqual(other.value);
    }

    /**
     * Checks whether this time is after or equal to the given time.
     *
     * @param other the time to compare to
     * @return {@code true} if this time is after or equal
     */
    public Boolean isAfterOrEqual(SeTime other) {
        return this.isAfter(other) || this.isEqual(other);
    }

    /**
     * Checks whether this time is before or equal to the given time.
     *
     * @param other the time to compare to
     * @return {@code true} if this time is before or equal
     */
    public Boolean isBeforeOrEqual(SeTime other) {
        return this.isBefore(other) || this.isEqual(other);
    }
}
