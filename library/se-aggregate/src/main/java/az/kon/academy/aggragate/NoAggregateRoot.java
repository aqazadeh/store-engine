package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A minimal aggregate root with only creation and modification timestamps.
 * <p>
 * Extends {@link BaseRoot} directly (without versioning, process status,
 * or domain events). Suitable for simple entities that require identity
 * and audit timestamps but not full aggregate lifecycle management.
 *
 * @param <T> the concrete aggregate root type (self-type)
 * @param <ID> the aggregate identifier type
 */
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class NoAggregateRoot<T extends BaseRoot<T, ID>, ID extends AggregateId<?>> extends BaseRoot<T, ID> {

    @Getter
    @Builder.Default
    private final SeDateTime creationTs = SeDateTime.now();

    @Getter
    @Builder.Default
    private final SeDateTime modificationTs = SeDateTime.now();

}
