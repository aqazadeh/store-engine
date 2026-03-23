package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class AggregateRoot<T extends BaseRoot<T, ID>, ID extends AggregateId<?>> extends BasicAggregateRoot<T, ID> {

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

    public final T markAsActive() {
        return this.toBuilder()
                .rowStatus(RowStatus.ACTIVE)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    public final T markAsHidden() {
        return this.toBuilder()
                .rowStatus(RowStatus.HIDDEN)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    public final T markAsArchived() {
        return this.toBuilder()
                .rowStatus(RowStatus.ARCHIVED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

    public final T markAsDeleted() {
        return this.toBuilder()
                .rowStatus(RowStatus.DELETED)
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }

}
