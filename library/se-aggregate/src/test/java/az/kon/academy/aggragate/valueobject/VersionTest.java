package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    void startConstantIsZero() {
        assertEquals(0L, Version.START.value());
    }

    @Test
    void constructorCreatesVersion() {
        Version v = new Version(42L);
        assertEquals(42L, v.value());
    }

    @Test
    void factoryMethodsCreateVersions() {
        Version v1 = Version.of(1L);
        Version v2 = Version.of(2);
        Version v3 = Version.of((short) 3);

        assertEquals(1L, v1.value());
        assertEquals(2L, v2.value());
        assertEquals(3L, v3.value());
    }

    @Test
    void increaseIncrementsByOne() {
        Version base = Version.of(5L);
        assertEquals(6L, base.increase().value());
    }

    @Test
    void decreaseDecrementsByOne() {
        Version base = Version.of(5L);
        assertEquals(4L, base.decrease().value());
    }

    @Test
    void resetReturnsOne() {
        Version base = Version.of(5L);
        assertEquals(1L, base.reset().value());
    }

    @Test
    void negativeVersion() {
        Version v = Version.of(-3L);
        assertEquals(-3L, v.value());
        assertEquals(-2L, v.increase().value());
        assertEquals(-4L, v.decrease().value());
        assertEquals(1L, v.reset().value());
    }

    @Test
    void isEqualsReturnsTrueForSameValue() {
        Version base = Version.of(5L);
        assertTrue(base.isEquals(Version.of(5L)));
    }

    @Test
    void isEqualsReturnsFalseForDifferentValue() {
        Version base = Version.of(5L);
        assertFalse(base.isEquals(Version.of(6L)));
    }

    @Test
    void isNotEquals() {
        Version base = Version.of(5L);
        assertTrue(base.isNotEquals(Version.of(6L)));
        assertFalse(base.isNotEquals(Version.of(5L)));
    }

    @Test
    void immutability() {
        Version original = Version.of(5L);
        Version increased = original.increase();
        assertEquals(5L, original.value());
        assertEquals(6L, increased.value());
    }

    @Test
    void toStringReturnsValue() {
        assertEquals("5", Version.of(5L).toString());
    }
}
