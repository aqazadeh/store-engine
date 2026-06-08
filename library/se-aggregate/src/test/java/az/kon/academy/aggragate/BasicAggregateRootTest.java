package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicAggregateRootTest {

    @Test
    void defaultsAreInitialized() {
        TestRoot root = TestRoot.newRoot(1L);

        assertNotNull(root.getVersion());
        assertEquals(Version.START.value(), root.getVersion().value());
        assertNotNull(root.getCreationTs());
        assertNotNull(root.getModificationTs());
    }

    @Test
    void getRootIdReturnsId() {
        TestRoot root = TestRoot.newRoot(99L);
        assertEquals(99L, ((TestId) root.getRootID()).value());
    }

    @Test
    void increaseVersionIncrementsAndUpdatesTimestamp() {
        TestRoot original = TestRoot.newRoot(1L);
        Version originalVersion = original.getVersion();
        SeDateTime initialModification = original.getModificationTs();

        TestRoot updated = original.increaseVersion();

        assertEquals(originalVersion.increase().value(), updated.getVersion().value());
        assertTrue(updated.getModificationTs().isAfter(initialModification) || updated.getModificationTs().isEqual(initialModification));
    }

    @Test
    void increaseVersionDoesNotChangeCreationTimestamp() {
        TestRoot original = TestRoot.newRoot(1L);
        SeDateTime originalCreationTs = original.getCreationTs();

        TestRoot updated = original.increaseVersion();

        assertEquals(originalCreationTs.toOffsetDateTime(), updated.getCreationTs().toOffsetDateTime());
    }
}
