package az.kon.academy.aggragate;

import java.util.Objects;

/**
 * Abstract base class for strongly-typed aggregate identifiers.
 * <p>
 * Wraps a generic ID value and provides identity-based equality
 * (two aggregate IDs are equal if their wrapped values are equal,
 * regardless of subclass).
 *
 * @param <ID> the type of the underlying identifier value
 */
public abstract class AggregateId<ID> {
    private ID id;

    private AggregateId() {
    }

    /**
     * Creates an aggregate ID with the given value.
     *
     * @param value the underlying identifier
     */
    public AggregateId(ID value) {
        this.id = value;
    }

    /**
     * Returns the underlying identifier value.
     *
     * @return the wrapped ID
     */
    public ID value() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregateId<?> aggregateId = (AggregateId<?>) o;
        return Objects.equals(id, aggregateId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
