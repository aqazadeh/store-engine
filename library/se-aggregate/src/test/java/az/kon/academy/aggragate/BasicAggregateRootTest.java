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
    void increaseVersionIncrementsAndUpdatesTimestamp() {
        TestRoot original = TestRoot.newRoot(1L);
        SeDateTime initialModification = original.getModificationTs();

        TestRoot updated = original.increaseVersion();

        assertEquals(original.getVersion().increase().value(), updated.getVersion().value());
        assertTrue(updated.getModificationTs().isAfter(initialModification) || updated.getModificationTs().isEqual(initialModification));
    }
}

