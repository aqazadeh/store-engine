package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RowStatusTest {

    @Test
    void enumValues() {
        RowStatus[] values = RowStatus.values();
        assertEquals(4, values.length);
        assertEquals(RowStatus.ACTIVE, RowStatus.valueOf("ACTIVE"));
        assertEquals(RowStatus.HIDDEN, RowStatus.valueOf("HIDDEN"));
        assertEquals(RowStatus.ARCHIVED, RowStatus.valueOf("ARCHIVED"));
        assertEquals(RowStatus.DELETED, RowStatus.valueOf("DELETED"));
    }

    @Test
    void lifecycleOrder() {
        RowStatus[] expected = {RowStatus.ACTIVE, RowStatus.HIDDEN, RowStatus.ARCHIVED, RowStatus.DELETED};
        assertArrayEquals(expected, RowStatus.values());
    }
}
