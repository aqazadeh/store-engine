package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class TestNoAggregateRoot extends NoAggregateRoot<TestNoAggregateRoot, TestId> {

    public static TestNoAggregateRoot newRoot(long id) {
        return TestNoAggregateRoot.builder()
                .id(new TestId(id))
                .build();
    }
}
