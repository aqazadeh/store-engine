package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SeTimeTest {

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
}

