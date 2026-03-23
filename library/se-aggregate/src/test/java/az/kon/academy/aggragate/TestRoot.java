package az.kon.academy.aggragate;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class TestRoot extends AggregateRoot<TestRoot, TestId> {
    public static TestRoot newRoot(long id) {
        return TestRoot.builder()
                .id(new TestId(id))
                .build();
    }

    public ProcessStatus getProcessStatusValue() {
        return getProcessStatus();
    }

    public RowStatus getRowStatusValue() {
        return getRowStatus();
    }
}

