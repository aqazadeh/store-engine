package az.kon.academy.aggragate.valueobject;

import java.time.*;

/**
 * An immutable value object representing a date and time with UTC offset.
 * <p>
 * Wraps an {@link OffsetDateTime} normalized to {@link ZoneOffset#UTC}.
 * Provides factory methods, comparison operations, and fluent
 * plus/minus methods for date arithmetic.
 */
public final class SeDateTime {
    private final OffsetDateTime value;
    private static final ZoneOffset zone = ZoneOffset.UTC;

    /**
     * Creates a new {@code SeDateTime} with the given {@link OffsetDateTime}.
     *
     * @param value the date-time value
     */
    public SeDateTime(OffsetDateTime value) {
        this.value = value;
    }

    /**
     * Returns the current date and time in UTC.
     *
     * @return a new {@code SeDateTime} representing now
     */
    public static SeDateTime now() {
        return new SeDateTime(OffsetDateTime.now(zone));
    }

    /**
     * Creates a new {@code SeDateTime} from an ISO-8601 string.
     *
     * @param date the ISO-8601 date-time string
     * @return a new {@code SeDateTime} instance
     */
    public static SeDateTime of(String date) {
        return SeDateTime.of(OffsetDateTime.parse(date));
    }

    /**
     * Creates a new {@code SeDateTime} from a {@link LocalDate},
     * set to the start of the day in UTC.
     *
     * @param date the date value
     * @return a new {@code SeDateTime} instance
     */
    public static SeDateTime of(LocalDate date) {
        return SeDateTime.of(date.atStartOfDay().atOffset(zone));
    }

    /**
     * Creates a new {@code SeDateTime} from a {@link LocalDateTime},
     * assigned to UTC.
     *
     * @param date the date-time value
     * @return a new {@code SeDateTime} instance
     */
    public static SeDateTime of(LocalDateTime date) {
        return SeDateTime.of(date.atOffset(zone));
    }

    /**
     * Creates a new {@code SeDateTime} from an {@link OffsetDateTime},
     * normalized to UTC.
     *
     * @param value the date-time value
     * @return a new {@code SeDateTime} instance
     */
    public static SeDateTime of(OffsetDateTime value) {
        OffsetDateTime normalized = value
                .atZoneSameInstant(zone)
                .toOffsetDateTime();
        return new SeDateTime(normalized);
    }

    /**
     * Returns the date portion as a {@link LocalDate}.
     *
     * @return the local date
     */
    public LocalDate toLocalDate() {
        return value.toLocalDate();
    }

    /**
     * Returns the underlying {@link OffsetDateTime} value.
     *
     * @return the offset date-time
     */
    public OffsetDateTime toOffsetDateTime() {
        return value;
    }

    /**
     * Returns the time portion as an {@link OffsetTime}.
     *
     * @return the offset time
     */
    public OffsetTime toOffsetTime() {
        return value.toOffsetTime();
    }

    /**
     * Checks whether this date-time is equal to the given date-time.
     *
     * @param other the date-time to compare to
     * @return {@code true} if the date-times are equal
     */
    public Boolean isEqual(SeDateTime other) {
        return value.isEqual(other.value);
    }

    /**
     * Checks whether this date-time is not equal to the given date-time.
     *
     * @param other the date-time to compare to
     * @return {@code true} if the date-times are not equal
     */
    public Boolean isNotEqual(SeDateTime other) {
        return !value.isEqual(other.value);
    }

    /**
     * Checks whether this date-time is strictly after the given date-time.
     *
     * @param other the date-time to compare to
     * @return {@code true} if this date-time is after
     */
    public Boolean isAfter(SeDateTime other) {
        return value.isAfter(other.value);
    }

    /**
     * Checks whether this date-time is strictly after now.
     *
     * @return {@code true} if this date-time is after now
     */
    public Boolean isAfterNow() {
        return this.isAfter(SeDateTime.now());
    }

    /**
     * Checks whether this date-time is strictly before the given date-time.
     *
     * @param other the date-time to compare to
     * @return {@code true} if this date-time is before
     */
    public Boolean isBefore(SeDateTime other) {
        return value.isBefore(other.value);
    }

    /**
     * Checks whether this date-time is before or equal to the given date-time.
     *
     * @param other the date-time to compare to
     * @return {@code true} if this date-time is before or equal
     */
    public Boolean isBeforeOrEqual(SeDateTime other) {
        return this.isBefore(other) || this.isEqual(other);
    }

    /**
     * Checks whether this date-time is after or equal to the given date-time.
     *
     * @param other the date-time to compare to
     * @return {@code true} if this date-time is after or equal
     */
    public Boolean isAfterOrEqual(SeDateTime other) {
        return this.isAfter(other) || this.isEqual(other);
    }

    /**
     * Checks whether this date-time is strictly before now.
     *
     * @return {@code true} if this date-time is before now
     */
    public Boolean isBeforeNow() {
        return this.isBefore(SeDateTime.now());
    }

    /**
     * Returns a new {@code SeDateTime} with the given nanoseconds added.
     *
     * @param nanos the nanoseconds to add
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime plusNanos(long nanos) {
        return new SeDateTime(value.plusNanos(nanos));
    }

    /**
     * Returns a new {@code SeDateTime} with the given seconds added.
     *
     * @param second the seconds to add
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime plusSeconds(int second) {
        return new SeDateTime(value.plusSeconds(second));
    }

    /**
     * Returns a new {@code SeDateTime} with the given minutes added.
     *
     * @param minutes the minutes to add
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime plusMinutes(int minutes) {
        return new SeDateTime(value.plusMinutes(minutes));
    }

    /**
     * Returns a new {@code SeDateTime} with the given hours added.
     *
     * @param hours the hours to add
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime plusHours(int hours) {
        return new SeDateTime(value.plusHours(hours));
    }

    /**
     * Returns a new {@code SeDateTime} with the given days added.
     *
     * @param days the days to add
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime plusDays(int days) {
        return new SeDateTime(value.plusDays(days));
    }

    /**
     * Returns a new {@code SeDateTime} with the given months added.
     *
     * @param months the months to add
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime plusMonths(int months) {
        return new SeDateTime(value.plusMonths(months));
    }

    /**
     * Returns a new {@code SeDateTime} with the given years added.
     *
     * @param years the years to add
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime plusYears(int years) {
        return new SeDateTime(value.plusYears(years));
    }

    /**
     * Returns a new {@code SeDateTime} with the given nanoseconds subtracted.
     *
     * @param nanos the nanoseconds to subtract
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime minusNanos(long nanos) {
        return new SeDateTime(value.minusNanos(nanos));
    }

    /**
     * Returns a new {@code SeDateTime} with the given seconds subtracted.
     *
     * @param second the seconds to subtract
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime minusSeconds(int second) {
        return new SeDateTime(value.minusSeconds(second));
    }

    /**
     * Returns a new {@code SeDateTime} with the given minutes subtracted.
     *
     * @param minutes the minutes to subtract
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime minusMinutes(int minutes) {
        return new SeDateTime(value.minusMinutes(minutes));
    }

    /**
     * Returns a new {@code SeDateTime} with the given hours subtracted.
     *
     * @param hours the hours to subtract
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime minusHours(int hours) {
        return new SeDateTime(value.minusHours(hours));
    }

    /**
     * Returns a new {@code SeDateTime} with the given days subtracted.
     *
     * @param days the days to subtract
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime minusDays(int days) {
        return new SeDateTime(value.minusDays(days));
    }

    /**
     * Returns a new {@code SeDateTime} with the given months subtracted.
     *
     * @param months the months to subtract
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime minusMonths(int months) {
        return new SeDateTime(value.minusMonths(months));
    }

    /**
     * Returns a new {@code SeDateTime} with the given years subtracted.
     *
     * @param years the years to subtract
     * @return a new {@code SeDateTime} instance
     */
    public SeDateTime minusYears(int years) {
        return new SeDateTime(value.minusYears(years));
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
