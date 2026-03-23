package az.kon.academy.aggragate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregateIdTest {

    @Test
    void equalsAndHashCodeSameValue() {
        AggregateId<Long> id1 = new TestId(1L);
        AggregateId<Long> id2 = new TestId(1L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void notEqualsDifferentValue() {
        AggregateId<Long> id1 = new TestId(1L);
        AggregateId<Long> id2 = new TestId(2L);

        assertNotEquals(id1, id2);
    }

    @Test
    void toStringReturnsValue() {
        AggregateId<Long> id = new TestId(42L);
        assertEquals("42", id.toString());
    }
}

