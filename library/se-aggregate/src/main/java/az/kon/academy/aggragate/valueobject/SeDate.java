package az.kon.academy.aggragate.valueobject;

import java.time.LocalDate;

public final class SeDate {
    private final LocalDate value;

    private SeDate(SeDateTime value) {
        this.value = value.toLocalDate();
    }

    public static SeDate now() {
        return new SeDate(SeDateTime.now());
    }

    public static SeDate of(LocalDate date) {
        return new SeDate(SeDateTime.of(date));
    }

    public LocalDate value() {
        return value;
    }

    public Boolean isAfter(SeDate other) {
        return value.isAfter(other.value);
    }

    public Boolean isBefore(SeDate other) {
        return value.isBefore(other.value);
    }

    public Boolean isEqual(SeDate other) {
        return value.isEqual(other.value);
    }

    public Boolean isAfterOrEqual(SeDate other) {
        return this.isAfter(other) || this.isEqual(other);
    }

    public Boolean isBeforeOrEqual(SeDate other) {
        return this.isBefore(other) || this.isEqual(other);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
