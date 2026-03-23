package az.kon.academy.aggragate;

import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@ToString
@SuperBuilder(toBuilder = true)
public abstract class BaseRoot<T extends BaseRoot<T, ID>, ID extends AggregateId<?>> {
    private ID id;

    public ID getRootID() {
        return id;
    }

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
