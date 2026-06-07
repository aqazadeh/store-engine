package az.kon.academy.aggragate;

import az.kon.academy.event.AbstractEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class TestEventSourcedRoot extends EventSourcedAggregateRoot<TestEventSourcedRoot, TestId, TestEvent> {

    private String name;

    public static TestEventSourcedRoot newRoot(long id) {
        return TestEventSourcedRoot.builder()
                .id(new TestId(id))
                .build();
    }

    @Override
    protected TestEventSourcedRoot applyEvent(TestEvent event) {
        return this.toBuilder()
                .name(event.getName())
                .build();
    }
}
