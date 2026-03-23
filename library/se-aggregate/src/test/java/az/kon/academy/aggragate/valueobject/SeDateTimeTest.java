package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SeDateTimeTest {

    @Test
    void factoryMethodsNormalizeToUtc() {
        OffsetDateTime odt = OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.ofHours(4));
        SeDateTime gdt = SeDateTime.of(odt);

        assertEquals(OffsetDateTime.of(2024, 1, 1, 6, 0, 0, 0, ZoneOffset.UTC), gdt.toOffsetDateTime());
    }

    @Test
    void conversionHelpers() {
        LocalDateTime ldt = LocalDateTime.of(2024, 1, 1, 10, 0);
        SeDateTime gdt = SeDateTime.of(ldt);

        assertEquals(LocalDate.of(2024, 1, 1), gdt.toLocalDate());
        assertEquals(ldt.toLocalTime().atOffset(ZoneOffset.UTC), gdt.toOffsetTime());
    }

    @Test
    void comparisonAndArithmetic() {
        SeDateTime base = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        SeDateTime plusDay = base.plusDays(1);
        SeDateTime minusHour = base.minusHours(1);

        assertTrue(plusDay.isAfter(base));
        assertTrue(base.isBefore(plusDay));
        assertTrue(base.isAfter(minusHour));
        assertTrue(base.isBeforeOrEqual(base));
        assertTrue(base.isAfterOrEqual(base));
    }

    @Test
    void toStringPrintsValue() {
        SeDateTime gdt = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertEquals("2024-01-01T00:00Z", gdt.toString());
    }
}

