package az.kon.academy.aggragate.valueobject;

import java.time.OffsetTime;

public final class SeTime {
    private final OffsetTime value;

    private SeTime(OffsetTime value) {
        this.value = value;
    }

    public static SeTime now() {
        return new SeTime(OffsetTime.now());
    }

    public static SeTime of(OffsetTime date) {
        return new SeTime(date);
    }

    public OffsetTime value() {
        return value;
    }

    public Boolean isAfter(SeTime other) {
        return value.isAfter(other.value);
    }

    public Boolean isBefore(SeTime other) {
        return value.isBefore(other.value);
    }

    public Boolean isEqual(SeTime other) {
        return value.isEqual(other.value);
    }

    public Boolean isAfterOrEqual(SeTime other) {
        return this.isAfter(other) || this.isEqual(other);
    }

    public Boolean isBeforeOrEqual(SeTime other) {
        return this.isBefore(other) || this.isEqual(other);
    }
}
