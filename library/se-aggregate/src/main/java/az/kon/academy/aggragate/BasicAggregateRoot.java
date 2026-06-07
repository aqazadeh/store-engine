package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Aggregate root base that adds versioning and audit timestamps.
 * <p>
 * Extends {@link BaseRoot} with an optimistic-locking {@link Version},
 * a creation timestamp, and a modification timestamp. The version
 * is incremented on every state change via {@link #increaseVersion()}.
 *
 * @param <T> the concrete aggregate root type (self-type)
 * @param <ID> the aggregate identifier type
 */
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class BasicAggregateRoot<T extends BaseRoot<T, ID>, ID extends AggregateId<?>> extends BaseRoot<T, ID> {
    @Getter
    @Builder.Default
    private final Version version = Version.START;

    @Getter
    @Builder.Default
    private final SeDateTime creationTs = SeDateTime.now();

    @Getter
    @Builder.Default
    private final SeDateTime modificationTs = SeDateTime.now();

    /**
     * Returns a new instance with the version incremented by one
     * and the modification timestamp updated to now.
     *
     * @return a new instance of the aggregate with an increased version
     */
    public final T increaseVersion() {
        return this.toBuilder()
                .version(this.version.increase())
                .modificationTs(SeDateTime.now())
                .build()
                .self();
    }
}
