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
    void notEqualsNull() {
        AggregateId<Long> id = new TestId(1L);
        assertNotEquals(null, id);
    }

    @Test
    void notEqualsDifferentClass() {
        AggregateId<Long> id1 = new TestId(1L);
        AggregateId<Long> id2 = new AggregateId<>(1L) {};
        assertNotEquals(id1, id2);
    }

    @Test
    void valueReturnsConstructorArgument() {
        AggregateId<Long> id = new TestId(42L);
        assertEquals(42L, id.value());
    }

    @Test
    void toStringReturnsValue() {
        AggregateId<Long> id = new TestId(42L);
        assertEquals("42", id.toString());
    }
}
