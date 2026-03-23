package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregateRootTest {

    @Test
    void markAsInProgressChangesStatus() {
        TestRoot root = TestRoot.newRoot(1L);
        SeDateTime initialModification = root.getModificationTs();

        TestRoot updated = root.markAsInProgress();

        assertEquals(ProcessStatus.IN_PROGRESS, updated.getProcessStatusValue());
        assertTrue(updated.getModificationTs().isAfter(initialModification) || updated.getModificationTs().isEqual(initialModification));
    }

    @Test
    void markAsFailedChangesStatus() {
        TestRoot root = TestRoot.newRoot(1L);
        SeDateTime initialModification = root.getModificationTs();

        TestRoot updated = root.markAsFailed();

        assertEquals(ProcessStatus.FAILED, updated.getProcessStatusValue());
        assertTrue(updated.getModificationTs().isAfter(initialModification) || updated.getModificationTs().isEqual(initialModification));
    }

    @Test
    void markAsCompletedChangesStatus() {
        TestRoot root = TestRoot.newRoot(1L);
        SeDateTime initialModification = root.getModificationTs();

        TestRoot updated = root.markAsCompleted();

        assertEquals(ProcessStatus.COMPLETED, updated.getProcessStatusValue());
        assertTrue(updated.getModificationTs().isAfter(initialModification) || updated.getModificationTs().isEqual(initialModification));
    }

    @Test
    void markAsRollBackedChangesStatus() {
        TestRoot root = TestRoot.newRoot(1L);
        SeDateTime initialModification = root.getModificationTs();

        TestRoot updated = root.markAsRollBacked();

        assertEquals(ProcessStatus.ROLL_BACKED, updated.getProcessStatusValue());
        assertTrue(updated.getModificationTs().isAfter(initialModification) || updated.getModificationTs().isEqual(initialModification));
    }

    @Test
    void markAsRowStatusUpdates() {
        TestRoot root = TestRoot.newRoot(1L);
        SeDateTime initialModification = root.getModificationTs();

        assertEquals(RowStatus.ACTIVE, root.getRowStatusValue());

        TestRoot hidden = root.markAsHidden();
        assertEquals(RowStatus.HIDDEN, hidden.getRowStatusValue());
        assertTrue(hidden.getModificationTs().isAfter(initialModification) || hidden.getModificationTs().isEqual(initialModification));

        TestRoot archived = hidden.markAsArchived();
        assertEquals(RowStatus.ARCHIVED, archived.getRowStatusValue());

        TestRoot deleted = archived.markAsDeleted();
        assertEquals(RowStatus.DELETED, deleted.getRowStatusValue());

        TestRoot active = deleted.markAsActive();
        assertEquals(RowStatus.ACTIVE, active.getRowStatusValue());
    }
}

