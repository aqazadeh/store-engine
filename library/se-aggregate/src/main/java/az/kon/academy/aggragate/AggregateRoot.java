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

/**
 * A full-featured aggregate root with process lifecycle, soft-delete
 * row status, and uncommitted domain event tracking.
 * <p>
 * Extends {@link BasicAggregateRoot} by adding:
 * <ul>
 *   <li><b>Process status</b> — tracks long-running operation state
 *       (in progress, completed, failed, rolled back)</li>
 *   <li><b>Row status</b> — soft-delete lifecycle
 *       (active, hidden, archived, deleted)</li>
 *   <li><b>Domain events</b> — collects {@link DomainEvent}s for
 *       publication after persistence</li>
 * </ul>
 * All mutating methods return a new immutable instance via the builder,
 * preserving the functional, event-driven style.
 *
 * @param <T> the concrete aggregate root type (self-type)
 * @param <ID> the aggregate identifier type
 */
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

    /**
     * Transitions the process status to {@link ProcessStatus#IN_PROGRESS}
     * and updates the modification timestamp.
     *
     * @return a new instance with the updated process status
     */
    public final T markAsInProgress() {
        return this.toBuilder()
                .processStatus(ProcessStatus.IN_PROGRESS)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Transitions the process status to {@link ProcessStatus#FAILED}
     * and updates the modification timestamp.
     *
     * @return a new instance with the updated process status
     */
    public final T markAsFailed() {
        return this.toBuilder()
                .processStatus(ProcessStatus.FAILED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Transitions the process status to {@link ProcessStatus#COMPLETED}
     * and updates the modification timestamp.
     *
     * @return a new instance with the updated process status
     */
    public final T markAsCompleted() {
        return this.toBuilder()
                .processStatus(ProcessStatus.COMPLETED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Transitions the process status to {@link ProcessStatus#ROLL_BACKED}
     * and updates the modification timestamp.
     *
     * @return a new instance with the updated process status
     */
    public final T markAsRollBacked() {
        return this.toBuilder()
                .processStatus(ProcessStatus.ROLL_BACKED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Sets the row status to {@link RowStatus#ACTIVE}.
     *
     * @return a new instance with the updated row status
     */
    protected final T markAsActive() {
        return this.toBuilder()
                .rowStatus(RowStatus.ACTIVE)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Sets the row status to {@link RowStatus#HIDDEN}.
     *
     * @return a new instance with the updated row status
     */
    protected final T markAsHidden() {
        return this.toBuilder()
                .rowStatus(RowStatus.HIDDEN)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Sets the row status to {@link RowStatus#ARCHIVED}.
     *
     * @return a new instance with the updated row status
     */
    protected final T markAsArchived() {
        return this.toBuilder()
                .rowStatus(RowStatus.ARCHIVED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Sets the row status to {@link RowStatus#DELETED}.
     *
     * @return a new instance with the updated row status
     */
    protected final T markAsDeleted() {
        return this.toBuilder()
                .rowStatus(RowStatus.DELETED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    /**
     * Returns an unmodifiable view of all uncommitted domain events
     * collected since the aggregate was loaded.
     *
     * @return an unmodifiable list of uncommitted events
     */
    public final List<AbstractEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(this.events);
    }

    /**
     * Registers a domain event for later publication.
     *
     * @param event the domain event to add
     */
    public final void addEvent(DomainEvent event) {
        this.events.add(event);
    }

}
