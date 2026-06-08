package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SeDateTest {

    @Test
    void comparisonsWork() {
        SeDate today = SeDate.of(LocalDate.of(2024, 1, 1));
        SeDate tomorrow = SeDate.of(LocalDate.of(2024, 1, 2));
        SeDate sameAsToday = SeDate.of(LocalDate.of(2024, 1, 1));

        assertTrue(tomorrow.isAfter(today));
        assertTrue(today.isBefore(tomorrow));
        assertTrue(today.isEqual(sameAsToday));
        assertTrue(tomorrow.isAfterOrEqual(today));
        assertTrue(today.isBeforeOrEqual(tomorrow));
    }

    @Test
    void nowReturnsCurrentDate() {
        SeDate today = SeDate.now();
        assertNotNull(today);
        assertNotNull(today.value());
    }

    @Test
    void valueReturnsLocalDate() {
        LocalDate date = LocalDate.of(2024, 5, 15);
        SeDate seDate = SeDate.of(date);
        assertEquals(date, seDate.value());
    }

    @Test
    void isAfterOrEqualWithEqualValues() {
        SeDate date = SeDate.of(LocalDate.of(2024, 1, 1));
        assertTrue(date.isAfterOrEqual(date));
        assertTrue(date.isAfterOrEqual(SeDate.of(LocalDate.of(2024, 1, 1))));
    }

    @Test
    void isBeforeOrEqualWithEqualValues() {
        SeDate date = SeDate.of(LocalDate.of(2024, 1, 1));
        assertTrue(date.isBeforeOrEqual(date));
        assertTrue(date.isBeforeOrEqual(SeDate.of(LocalDate.of(2024, 1, 1))));
    }

    @Test
    void toStringReturnsIsoDate() {
        SeDate date = SeDate.of(LocalDate.of(2024, 1, 1));
        assertEquals("2024-01-01", date.toString());
    }
}
