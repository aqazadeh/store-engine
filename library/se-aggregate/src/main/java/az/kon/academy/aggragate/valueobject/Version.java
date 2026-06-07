package az.kon.academy.aggragate.valueobject;

/**
 * An immutable value object representing an optimistic-locking version number.
 * <p>
 * Wraps a {@link Long} version counter. Provides {@link #increase()},
 * {@link #decrease()}, and {@link #reset()} for version management.
 * The static constant {@link #START} represents version zero.
 */
public final class Version {
    public static final Version START = new Version(0L);

    private final Long value;

    /**
     * Creates a new {@code Version} with the given value.
     *
     * @param value the version number
     */
    public Version(Long value) {
        this.value = value;
    }

    /**
     * Creates a new {@code Version} from a {@link Long} value.
     *
     * @param value the version number
     * @return a new {@code Version} instance
     */
    public static Version of(Long value) {
        return new Version(value);
    }

    /**
     * Creates a new {@code Version} from an {@link Integer} value.
     *
     * @param value the version number
     * @return a new {@code Version} instance
     */
    public static Version of(Integer value) {
        return Version.of(value.longValue());
    }

    /**
     * Creates a new {@code Version} from a {@link Short} value.
     *
     * @param value the version number
     * @return a new {@code Version} instance
     */
    public static Version of(Short value) {
        return Version.of(value.longValue());
    }

    /**
     * Returns a new version incremented by one.
     *
     * @return a new {@code Version} with value + 1
     */
    public Version increase() {
        return new Version(value + 1L);
    }

    /**
     * Returns a new version decremented by one.
     *
     * @return a new {@code Version} with value - 1
     */
    public Version decrease() {
        return new Version(value - 1L);
    }

    /**
     * Returns a new version reset to 1.
     *
     * @return a new {@code Version} with value 1
     */
    public Version reset() {
        return new Version(1L);
    }

    /**
     * Returns the underlying version number.
     *
     * @return the version value
     */
    public Long value() {
        return value;
    }

    /**
     * Checks whether this version equals the given version.
     *
     * @param version the version to compare to
     * @return {@code true} if the versions are equal
     */
    public Boolean isEquals(Version version) {
        return this.value.equals(version.value);
    }

    /**
     * Checks whether this version is not equal to the given version.
     *
     * @param version the version to compare to
     * @return {@code true} if the versions differ
     */
    public Boolean isNotEquals(Version version) {
        return !this.value.equals(version.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
