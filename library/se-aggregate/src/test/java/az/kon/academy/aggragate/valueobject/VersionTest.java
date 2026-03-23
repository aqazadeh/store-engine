package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

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
    void arithmeticAndComparison() {
        Version base = Version.of(5L);

        assertEquals(6L, base.increase().value());
        assertEquals(4L, base.decrease().value());
        assertEquals(1L, base.reset().value());

        assertTrue(base.isEquals(Version.of(5L)));
        assertTrue(base.isNotEquals(Version.of(6L)));
    }
}

