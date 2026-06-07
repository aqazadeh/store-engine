package az.kon.academy.aggragate;

import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * Abstract base class for all aggregate roots in the domain model.
 * <p>
 * Provides identity-based equality via {@link AggregateId} and a
 * {@link #self()} method for fluent, immutable domain operations.
 *
 * @param <T> the concrete aggregate root type (self-type)
 * @param <ID> the aggregate identifier type
 */
@ToString
@SuperBuilder(toBuilder = true)
public abstract class BaseRoot<T extends BaseRoot<T, ID>, ID extends AggregateId<?>> {
    private ID id;

    /**
     * Returns the aggregate root identifier.
     *
     * @return the root ID
     */
    public ID getRootID() {
        return id;
    }

    /**
     * Returns the concrete instance cast to the self-type {@code T}.
     * Used by subclasses for fluent builder chaining.
     *
     * @return this instance as type {@code T}
     */
    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseRoot<?, ?> that = (BaseRoot<?, ?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
