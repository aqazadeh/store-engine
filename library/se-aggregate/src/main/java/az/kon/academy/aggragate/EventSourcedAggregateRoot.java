package az.kon.academy.aggragate;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base for event-sourced aggregate roots.
 * <p>
 * Extends {@link BasicAggregateRoot} with event sourcing capabilities:
 * maintains separate lists of committed and uncommitted domain events,
 * and supports replaying a history of events via {@link #replay(List)}.
 * Subclasses implement {@link #applyEvent(Object)} to define how each
 * event type mutates state.
 *
 * @param <R> the concrete aggregate root type (self-type)
 * @param <ID> the aggregate identifier type
 * @param <E> the domain event type
 */
@SuperBuilder
public abstract class EventSourcedAggregateRoot<R extends EventSourcedAggregateRoot<R, ID, E>, ID extends AggregateId<?>, E>
        extends BasicAggregateRoot<R, ID> {

    @Builder.Default private final List<DomainEvent> committedEvents = new ArrayList<>();

    @Builder.Default private final List<DomainEvent> uncommitedEvents = new ArrayList<>();

    /**
     * Applies a single event to rebuild aggregate state.
     * Called during replay and during normal event application.
     *
     * @param event the event to apply
     * @return the updated aggregate instance
     */
    protected abstract R applyEvent(E event);

    /**
     * Replays a list of historical events, rebuilding the aggregate
     * state from scratch. Each event is applied in sequence via
     * {@link #applyEvent(Object)}.
     *
     * @param events the chronological list of events to replay
     * @return the aggregate with fully replayed state
     */
    @SuppressWarnings("unchecked")
    public final R replay(List<E> events) {
        R aggregate = (R) this;
        for (E event : events) {
            aggregate = aggregate.applyEvent(event);
        }
        return aggregate;
    }

    /**
     * Returns an unmodifiable view of uncommitted events that have not
     * yet been persisted.
     *
     * @return an unmodifiable list of uncommitted events
     */
    public final List<AbstractEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(this.uncommitedEvents);
    }

    /**
     * Returns an unmodifiable view of events that have already been
     * committed (persisted) for this aggregate.
     *
     * @return an unmodifiable list of committed events
     */
    public final List<AbstractEvent> getCommittedEvents() {
        return Collections.unmodifiableList(this.committedEvents);
    }

    /**
     * Registers a domain event that has not yet been persisted.
     *
     * @param event the domain event to add
     */
    public final void addEvent(DomainEvent event) {
        this.uncommitedEvents.add(event);
    }
}
