package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.event.AbstractEvent;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventSourcedAggregateRootTest {

    @Test
    void addEventAndGetUncommittedEvents() {
        TestEventSourcedRoot root = TestEventSourcedRoot.newRoot(1L);
        assertEquals(0, root.getUncommittedEvents().size());

        root.addEvent(new TestDomainEvent("1", OffsetDateTime.now()));
        assertEquals(1, root.getUncommittedEvents().size());
    }

    @Test
    void getUncommittedEventsReturnsUnmodifiableList() {
        TestEventSourcedRoot root = TestEventSourcedRoot.newRoot(1L);
        root.addEvent(new TestDomainEvent("1", OffsetDateTime.now()));

        List<AbstractEvent> events = root.getUncommittedEvents();
        assertThrows(UnsupportedOperationException.class, () -> events.add(new TestDomainEvent("2", OffsetDateTime.now())));
    }

    @Test
    void getCommittedEventsInitiallyEmpty() {
        TestEventSourcedRoot root = TestEventSourcedRoot.newRoot(1L);
        assertEquals(0, root.getCommittedEvents().size());
    }

    @Test
    void replayRebuildsStateFromEvents() {
        TestEventSourcedRoot root = TestEventSourcedRoot.newRoot(1L);

        List<TestEvent> events = new ArrayList<>();
        events.add(TestEvent.builder().name("first").build());
        events.add(TestEvent.builder().name("second").build());

        TestEventSourcedRoot replayed = root.replay(events);
        assertEquals("second", replayed.getName());
    }

    @Test
    void replayEmptyListPreservesState() {
        TestEventSourcedRoot root = TestEventSourcedRoot.newRoot(1L);
        TestEventSourcedRoot replayed = root.replay(new ArrayList<>());
        assertNull(replayed.getName());
        assertEquals(root.getRootID(), replayed.getRootID());
    }

    @Test
    void replayRebuildsStateWithoutPreservingUncommittedEvents() {
        TestEventSourcedRoot root = TestEventSourcedRoot.newRoot(1L);
        root.addEvent(new TestDomainEvent("1", OffsetDateTime.now()));
        assertEquals(1, root.getUncommittedEvents().size());

        List<TestEvent> events = new ArrayList<>();
        events.add(TestEvent.builder().name("updated").build());

        TestEventSourcedRoot replayed = root.replay(events);
        assertEquals("updated", replayed.getName());
        assertEquals(0, replayed.getUncommittedEvents().size());
        assertEquals(0, replayed.getCommittedEvents().size());
    }
}
