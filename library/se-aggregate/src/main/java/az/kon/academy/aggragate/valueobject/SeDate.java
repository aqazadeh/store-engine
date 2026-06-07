package az.kon.academy.aggragate.valueobject;

import java.time.LocalDate;

/**
 * An immutable value object representing a date in the system.
 * <p>
 * Wraps a {@link LocalDate} and provides comparison methods
 * (isAfter, isBefore, isEqual) and factory methods.
 */
public final class SeDate {
    private final LocalDate value;

    private SeDate(SeDateTime value) {
        this.value = value.toLocalDate();
    }

    /**
     * Returns the current date.
     *
     * @return a new {@code SeDate} representing today
     */
    public static SeDate now() {
        return new SeDate(SeDateTime.now());
    }

    /**
     * Creates a new {@code SeDate} from a {@link LocalDate}.
     *
     * @param date the date value
     * @return a new {@code SeDate} instance
     */
    public static SeDate of(LocalDate date) {
        return new SeDate(SeDateTime.of(date));
    }

    /**
     * Returns the underlying date value.
     *
     * @return the {@link LocalDate} value
     */
    public LocalDate value() {
        return value;
    }

    /**
     * Checks whether this date is strictly after the given date.
     *
     * @param other the date to compare to
     * @return {@code true} if this date is after
     */
    public Boolean isAfter(SeDate other) {
        return value.isAfter(other.value);
    }

    /**
     * Checks whether this date is strictly before the given date.
     *
     * @param other the date to compare to
     * @return {@code true} if this date is before
     */
    public Boolean isBefore(SeDate other) {
        return value.isBefore(other.value);
    }

    /**
     * Checks whether this date equals the given date.
     *
     * @param other the date to compare to
     * @return {@code true} if the dates are equal
     */
    public Boolean isEqual(SeDate other) {
        return value.isEqual(other.value);
    }

    /**
     * Checks whether this date is after or equal to the given date.
     *
     * @param other the date to compare to
     * @return {@code true} if this date is after or equal
     */
    public Boolean isAfterOrEqual(SeDate other) {
        return this.isAfter(other) || this.isEqual(other);
    }

    /**
     * Checks whether this date is before or equal to the given date.
     *
     * @param other the date to compare to
     * @return {@code true} if this date is before or equal
     */
    public Boolean isBeforeOrEqual(SeDate other) {
        return this.isBefore(other) || this.isEqual(other);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
