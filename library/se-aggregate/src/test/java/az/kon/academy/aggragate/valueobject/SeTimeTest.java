package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SeTimeTest {

    @Test
    void nowReturnsCurrentTime() {
        SeTime now = SeTime.now();
        assertNotNull(now);
        assertNotNull(now.value());
    }

    @Test
    void ofCreatesSeTime() {
        OffsetTime time = OffsetTime.of(10, 30, 0, 0, ZoneOffset.UTC);
        SeTime seTime = SeTime.of(time);
        assertEquals(time, seTime.value());
    }

    @Test
    void valueReturnsOffsetTime() {
        OffsetTime time = OffsetTime.of(14, 0, 0, 0, ZoneOffset.UTC);
        SeTime seTime = SeTime.of(time);
        assertEquals(time, seTime.value());
    }

    @Test
    void comparisonsWork() {
        SeTime morning = SeTime.of(OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC));
        SeTime noon = SeTime.of(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC));

        assertTrue(noon.isAfter(morning));
        assertTrue(morning.isBefore(noon));
        assertTrue(noon.isAfterOrEqual(noon));
        assertTrue(morning.isBeforeOrEqual(morning));
        assertTrue(noon.isEqual(SeTime.of(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC))));
    }

    @Test
    void isAfterOrEqualWithEqualValues() {
        SeTime time = SeTime.of(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC));
        assertTrue(time.isAfterOrEqual(time));
    }

    @Test
    void isBeforeOrEqualWithEqualValues() {
        SeTime time = SeTime.of(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC));
        assertTrue(time.isBeforeOrEqual(time));
    }

    @Test
    void isEqualReturnsFalseForDifferentTime() {
        SeTime morning = SeTime.of(OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC));
        SeTime noon = SeTime.of(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC));
        assertFalse(morning.isEqual(noon));
    }

    @Test
    void toStringPrintsValue() {
        SeTime time = SeTime.of(OffsetTime.of(10, 15, 30, 0, ZoneOffset.UTC));
        String str = time.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }
}
