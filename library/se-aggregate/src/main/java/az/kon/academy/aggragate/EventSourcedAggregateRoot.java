package az.kon.academy.aggragate;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuperBuilder
public abstract class EventSourcedAggregateRoot<R extends EventSourcedAggregateRoot<R, ID, E>, ID extends AggregateId<?>, E>
        extends BasicAggregateRoot<R, ID> {

    @Builder.Default private final List<DomainEvent> committedEvents = new ArrayList<>();

    @Builder.Default private final List<DomainEvent> uncommitedEvents = new ArrayList<>();

    protected abstract R applyEvent(E event);

    @SuppressWarnings("unchecked")
    public final R replay(List<E> events) {
        R aggregate = (R) this;
        for (E event : events) {
            aggregate = aggregate.applyEvent(event);
        }
        return aggregate;
    }

    public final List<AbstractEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(this.uncommitedEvents);
    }

    public final List<AbstractEvent> getCommittedEvents() {
        return Collections.unmodifiableList(this.committedEvents);
    }

    public final void addEvent(DomainEvent event) {
        this.uncommitedEvents.add(event);
    }
}
