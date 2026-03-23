package az.kon.academy.aggragate;

import java.util.Objects;

public abstract class AggregateId<ID> {
    private ID id;

    private AggregateId() {
    }

    public AggregateId(ID value) {
        this.id = value;
    }

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
