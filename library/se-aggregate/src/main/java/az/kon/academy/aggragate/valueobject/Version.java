package az.kon.academy.aggragate.valueobject;

public final class Version {
    public static final Version START = new Version(0L);

    private final Long value;

    public Version(Long value) {
        this.value = value;
    }

    public static Version of(Long value) {
        return new Version(value);
    }

    public static Version of(Integer value) {
        return Version.of(value.longValue());
    }

    public static Version of(Short value) {
        return Version.of(value.longValue());
    }

    public Version increase() {
        return new Version(value + 1L);
    }

    public Version decrease() {
        return new Version(value - 1L);
    }

    public Version reset() {
        return new Version(1L);
    }

    public Long value() {
        return value;
    }

    public Boolean isEquals(Version version) {
        return this.value.equals(version.value);
    }

    public Boolean isNotEquals(Version version) {
        return !this.value.equals(version.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
