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
    void toStringReturnsIsoDate() {
        SeDate date = SeDate.of(LocalDate.of(2024, 1, 1));
        assertEquals("2024-01-01", date.toString());
    }
}

