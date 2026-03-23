package az.kon.academy.aggragate.valueobject;

import java.time.*;

public final class SeDateTime {
    private final OffsetDateTime value;
    private static final ZoneOffset zone = ZoneOffset.UTC;

    public SeDateTime(OffsetDateTime value) {
        this.value = value;
    }

    public static SeDateTime now() {
        return new SeDateTime(OffsetDateTime.now(zone));
    }

    public static SeDateTime of(String date) {
        return SeDateTime.of(OffsetDateTime.parse(date));
    }

    public static SeDateTime of(LocalDate date) {
        return SeDateTime.of(date.atStartOfDay().atOffset(zone));
    }

    public static SeDateTime of(LocalDateTime date) {
        return SeDateTime.of(date.atOffset(zone));
    }

    public static SeDateTime of(OffsetDateTime value) {
        OffsetDateTime normalized = value
                .atZoneSameInstant(zone)
                .toOffsetDateTime();
        return new SeDateTime(normalized);
    }

    public LocalDate toLocalDate() {
        return value.toLocalDate();
    }

    public OffsetDateTime toOffsetDateTime() {
        return value;
    }

    public OffsetTime toOffsetTime() {
        return value.toOffsetTime();
    }

    public Boolean isEqual(SeDateTime other) {
        return value.isEqual(other.value);
    }

    public Boolean isNotEqual(SeDateTime other) {
        return !value.isEqual(other.value);
    }

    public Boolean isAfter(SeDateTime other) {
        return value.isAfter(other.value);
    }

    public Boolean isAfterNow() {
        return this.isBefore(SeDateTime.now());
    }

    public Boolean isBefore(SeDateTime other) {
        return value.isBefore(other.value);
    }

    public Boolean isBeforeOrEqual(SeDateTime other) {
        return this.isBefore(other) || this.isEqual(other);
    }

    public Boolean isAfterOrEqual(SeDateTime other) {
        return this.isAfter(other) || this.isEqual(other);
    }

    public Boolean isBeforeNow() {
        return this.isAfter(SeDateTime.now());
    }

    public SeDateTime plusNanos(long nanos) {
        return new SeDateTime(value.plusNanos(nanos));
    }

    public SeDateTime plusSeconds(int second) {
        return new SeDateTime(value.plusSeconds(second));
    }

    public SeDateTime plusMinutes(int minutes) {
        return new SeDateTime(value.plusMinutes(minutes));
    }

    public SeDateTime plusHours(int hours) {
        return new SeDateTime(value.plusHours(hours));
    }

    public SeDateTime plusDays(int days) {
        return new SeDateTime(value.plusDays(days));
    }

    public SeDateTime plusMonths(int months) {
        return new SeDateTime(value.plusMonths(months));
    }

    public SeDateTime plusYears(int years) {
        return new SeDateTime(value.plusYears(years));
    }

    public SeDateTime minusNanos(long nanos) {
        return new SeDateTime(value.minusNanos(nanos));
    }

    public SeDateTime minusSeconds(int second) {
        return new SeDateTime(value.minusSeconds(second));
    }

    public SeDateTime minusMinutes(int minutes) {
        return new SeDateTime(value.minusMinutes(minutes));
    }

    public SeDateTime minusHours(int hours) {
        return new SeDateTime(value.minusHours(hours));
    }

    public SeDateTime minusDays(int days) {
        return new SeDateTime(value.minusDays(days));
    }

    public SeDateTime minusMonths(int months) {
        return new SeDateTime(value.minusMonths(months));
    }

    public SeDateTime minusYears(int years) {
        return new SeDateTime(value.minusYears(years));
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
