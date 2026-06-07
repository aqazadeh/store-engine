package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoAggregateRootTest {

    @Test
    void defaultsAreInitialized() {
        TestNoAggregateRoot root = TestNoAggregateRoot.newRoot(1L);

        assertNotNull(root.getRootID());
        assertEquals(1L, ((TestId) root.getRootID()).value());
        assertNotNull(root.getCreationTs());
        assertNotNull(root.getModificationTs());
    }

    @Test
    void equalityBasedOnId() {
        TestNoAggregateRoot root1 = TestNoAggregateRoot.newRoot(1L);
        TestNoAggregateRoot root2 = TestNoAggregateRoot.newRoot(1L);
        TestNoAggregateRoot root3 = TestNoAggregateRoot.newRoot(2L);

        assertEquals(root1, root2);
        assertNotEquals(root1, root3);
    }

    @Test
    void selfReturnsConcreteType() {
        TestNoAggregateRoot root = TestNoAggregateRoot.newRoot(1L);
        assertEquals(TestNoAggregateRoot.class, root.self().getClass());
    }
}
