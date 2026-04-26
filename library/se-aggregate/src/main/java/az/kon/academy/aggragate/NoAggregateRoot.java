package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
