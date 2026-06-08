package az.kon.academy.aggragate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseRootTest {

    @Test
    void equalityBasedOnId() {
        TestRoot root1 = TestRoot.newRoot(1L);
        TestRoot root2 = TestRoot.newRoot(1L);
        TestRoot root3 = TestRoot.newRoot(2L);

        assertEquals(root1, root2);
        assertNotEquals(root1, root3);
    }

    @Test
    void notEqualsNull() {
        TestRoot root = TestRoot.newRoot(1L);
        assertNotEquals(null, root);
    }

    @Test
    void hashCodeConsistentWithEquals() {
        TestRoot root1 = TestRoot.newRoot(1L);
        TestRoot root2 = TestRoot.newRoot(1L);

        assertEquals(root1, root2);
        assertEquals(root1.hashCode(), root2.hashCode());
    }

    @Test
    void getRootIdReturnsId() {
        TestRoot root = TestRoot.newRoot(42L);
        assertEquals(42L, ((TestId) root.getRootID()).value());
    }

    @Test
    void selfReturnsConcreteType() {
        TestRoot root = TestRoot.newRoot(1L);
        assertEquals(TestRoot.class, root.self().getClass());
    }

    @Test
    void toStringIncludesId() {
        TestRoot root = TestRoot.newRoot(1L);
        String str = root.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }
}
