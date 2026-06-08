package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.event.AbstractEvent;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

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
    void addEventAndGetUncommittedEvents() {
        TestRoot root = TestRoot.newRoot(1L);
        assertEquals(0, root.getUncommittedEvents().size());

        root.addEvent(new TestDomainEvent("1", OffsetDateTime.now()));
        assertEquals(1, root.getUncommittedEvents().size());
    }

    @Test
    void getUncommittedEventsReturnsUnmodifiableList() {
        TestRoot root = TestRoot.newRoot(1L);
        root.addEvent(new TestDomainEvent("1", OffsetDateTime.now()));

        List<AbstractEvent> events = root.getUncommittedEvents();
        assertThrows(UnsupportedOperationException.class, () -> events.add(new TestDomainEvent("2", OffsetDateTime.now())));
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

    @Test
    void markAsActiveTransition() {
        TestRoot root = TestRoot.newRoot(1L);
        TestRoot hidden = root.markAsHidden();
        assertEquals(RowStatus.HIDDEN, hidden.getRowStatusValue());

        TestRoot reactivated = hidden.markAsActive();
        assertEquals(RowStatus.ACTIVE, reactivated.getRowStatusValue());
    }
}
