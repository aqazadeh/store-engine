package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class AggregateRoot<T extends BaseRoot<T, ID>, ID extends AggregateId<?>> extends BasicAggregateRoot<T, ID> {
    private final List<DomainEvent> events = new ArrayList<>();

    @Getter
    @Builder.Default
    private ProcessStatus processStatus = ProcessStatus.COMPLETED;

    @Getter
    @Builder.Default
    private RowStatus rowStatus = RowStatus.ACTIVE;

    public final T markAsInProgress() {
        return this.toBuilder()
                .processStatus(ProcessStatus.IN_PROGRESS)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    public final T markAsFailed() {
        return this.toBuilder()
                .processStatus(ProcessStatus.FAILED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    public final T markAsCompleted() {
        return this.toBuilder()
                .processStatus(ProcessStatus.COMPLETED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    public final T markAsRollBacked() {
        return this.toBuilder()
                .processStatus(ProcessStatus.ROLL_BACKED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    protected final T markAsActive() {
        return this.toBuilder()
                .rowStatus(RowStatus.ACTIVE)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    protected final T markAsHidden() {
        return this.toBuilder()
                .rowStatus(RowStatus.HIDDEN)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    protected final T markAsArchived() {
        return this.toBuilder()
                .rowStatus(RowStatus.ARCHIVED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    protected final T markAsDeleted() {
        return this.toBuilder()
                .rowStatus(RowStatus.DELETED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    public final List<AbstractEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(this.events);
    }

    public final void addEvent(DomainEvent event) {
        this.events.add(event);
    }

}
