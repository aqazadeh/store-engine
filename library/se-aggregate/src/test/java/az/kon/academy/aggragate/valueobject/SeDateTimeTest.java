package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class SeDateTimeTest {

    @Test
    void factoryMethodsNormalizeToUtc() {
        OffsetDateTime odt = OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.ofHours(4));
        SeDateTime gdt = SeDateTime.of(odt);

        assertTrue(gdt.toOffsetDateTime().isEqual(OffsetDateTime.of(2024, 1, 1, 6, 0, 0, 0, ZoneOffset.UTC)));
    }

    @Test
    void ofStringParsesIso() {
        SeDateTime gdt = SeDateTime.of("2024-01-01T10:00:00Z");
        assertNotNull(gdt.toOffsetDateTime());
    }

    @Test
    void ofLocalDateCreatesStartOfDayUtc() {
        SeDateTime gdt = SeDateTime.of(LocalDate.of(2024, 1, 1));
        assertEquals(LocalDate.of(2024, 1, 1), gdt.toLocalDate());
    }

    @Test
    void ofLocalDateTimeAssignsUtc() {
        LocalDateTime ldt = LocalDateTime.of(2024, 1, 1, 10, 0);
        SeDateTime gdt = SeDateTime.of(ldt);
        assertEquals(ldt.toLocalTime(), gdt.toOffsetTime().toLocalTime());
    }

    @Test
    void nowReturnsCurrent() {
        SeDateTime now = SeDateTime.now();
        assertNotNull(now);
        assertNotNull(now.toOffsetDateTime());
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
    void isEqualAndIsNotEqual() {
        SeDateTime a = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        SeDateTime b = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        SeDateTime c = SeDateTime.of(LocalDateTime.of(2024, 1, 2, 0, 0));

        assertTrue(a.isEqual(b));
        assertTrue(a.isNotEqual(c));
        assertFalse(a.isNotEqual(b));
    }

    @Test
    void isBeforeNowAndIsAfterNow() {
        SeDateTime past = SeDateTime.now().minusHours(1);
        SeDateTime future = SeDateTime.now().plusHours(1);

        assertTrue(past.isBeforeNow());
        assertFalse(past.isAfterNow());
        assertTrue(future.isAfterNow());
        assertFalse(future.isBeforeNow());
    }

    @Test
    void plusAllUnits() {
        SeDateTime base = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));

        assertNotNull(base.plusNanos(1));
        assertNotNull(base.plusSeconds(1));
        assertNotNull(base.plusMinutes(1));
        assertNotNull(base.plusHours(1));
        assertTrue(base.plusDays(1).isAfter(base));
        assertTrue(base.plusMonths(1).isAfter(base));
        assertTrue(base.plusYears(1).isAfter(base));
    }

    @Test
    void minusAllUnits() {
        SeDateTime base = SeDateTime.of(LocalDateTime.of(2024, 7, 1, 0, 0));

        assertNotNull(base.minusNanos(1));
        assertNotNull(base.minusSeconds(1));
        assertNotNull(base.minusMinutes(1));
        assertNotNull(base.minusHours(1));
        assertTrue(base.minusDays(1).isBefore(base));
        assertTrue(base.minusMonths(1).isBefore(base));
        assertTrue(base.minusYears(1).isBefore(base));
    }

    @Test
    void isBeforeOrEqual_withEarlierAndSame() {
        SeDateTime base = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        SeDateTime earlier = base.minusDays(1);

        assertTrue(earlier.isBeforeOrEqual(base));
        assertTrue(base.isBeforeOrEqual(base));
        assertFalse(base.isBeforeOrEqual(earlier));
    }

    @Test
    void isAfterOrEqual_withLaterAndSame() {
        SeDateTime base = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        SeDateTime later = base.plusDays(1);

        assertTrue(later.isAfterOrEqual(base));
        assertTrue(base.isAfterOrEqual(base));
        assertFalse(base.isAfterOrEqual(later));
    }

    @Test
    void toOffsetDateTimeReturnsValue() {
        SeDateTime gdt = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        OffsetDateTime odt = gdt.toOffsetDateTime();
        assertNotNull(odt);
    }

    @Test
    void toStringPrintsValue() {
        SeDateTime gdt = SeDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertEquals("2024-01-01T00:00Z", gdt.toString());
    }
}
