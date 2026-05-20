package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValue;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.event.management.variant.VariantValueCreatedEvent;
import az.kon.academy.catalog.event.management.variant.VariantValueNameChangedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("VariantValueRoot")
class VariantValueRootTest {

    private VariantKeyId keyId;
    private VariantValue name;
    private VariantValueCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        keyId = VariantKeyId.from(UUID.randomUUID());
        name = new VariantValue("Crimson");
        createCommand = VariantValueCreateCommand.builder()
                .keyId(keyId)
                .name(name)
                .build();
    }

    private VariantValueRoot freshValue() {
        return VariantValueRoot.initialize(createCommand);
    }

    @Nested
    @DisplayName("initialize()")
    class Initialize {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var value = VariantValueRoot.initialize(createCommand);

                assertThat(value.getRootID()).isNotNull();
                assertThat(value.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = VariantValueRoot.initialize(createCommand);
                var second = VariantValueRoot.initialize(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets keyId from command")
            void setsKeyIdFromCommand() {
                var value = VariantValueRoot.initialize(createCommand);

                assertThat(value.getKeyId()).isEqualTo(keyId);
                assertThat(value.getKeyId().value()).isEqualTo(keyId.value());
            }

            @Test
            @DisplayName("Sets name from command")
            void setsNameFromCommand() {
                var value = VariantValueRoot.initialize(createCommand);

                assertThat(value.getName()).isEqualTo(name);
                assertThat(value.getName().value()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var value = VariantValueRoot.initialize(createCommand);

                assertThat(value.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Two values with the same keyId but different names are independent")
            void sameKeyDifferentNamesAreIndependent() {
                var red = VariantValueCreateCommand.builder()
                        .keyId(keyId)
                        .name(new VariantValue("Crimson"))
                        .build();
                var blue = VariantValueCreateCommand.builder()
                        .keyId(keyId)
                        .name(new VariantValue("Azure"))
                        .build();

                var redValue = VariantValueRoot.initialize(red);
                var blueValue = VariantValueRoot.initialize(blue);

                assertThat(redValue.getRootID().value()).isNotEqualTo(blueValue.getRootID().value());
                assertThat(redValue.getName()).isNotEqualTo(blueValue.getName());
                assertThat(redValue.getKeyId()).isEqualTo(blueValue.getKeyId());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var value = VariantValueRoot.initialize(createCommand);

                assertThat(value.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is VariantValueCreatedEvent")
            void registeredEventIsCorrectType() {
                var value = VariantValueRoot.initialize(createCommand);

                assertThat(value.getUncommittedEvents().getFirst())
                        .isInstanceOf(VariantValueCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the value ID")
            void eventAggregateIdMatchesValueId() {
                var value = VariantValueRoot.initialize(createCommand);
                var event = value.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(value.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var value = VariantValueRoot.initialize(createCommand);
                var event = value.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var value = VariantValueRoot.initialize(createCommand);
                var event = value.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(value.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the keyId from command")
            void eventCarriesKeyId() {
                var value = VariantValueRoot.initialize(createCommand);
                var event = (VariantValueCreatedEvent) value.getUncommittedEvents().getFirst();

                assertThat(event.getKeyId()).isEqualTo(keyId.value());
            }

            @Test
            @DisplayName("Event carries the name from command")
            void eventCarriesName() {
                var value = VariantValueRoot.initialize(createCommand);
                var event = (VariantValueCreatedEvent) value.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(name.value());
            }
        }
    }

    @Nested
    @DisplayName("changeName()")
    class ChangeName {

        private VariantValue newName;
        private VariantValueChangeNameCommand changeNameCommand;

        @BeforeEach
        void setUpCommand() {
            newName = new VariantValue("Green");
            changeNameCommand = VariantValueChangeNameCommand.builder()
                    .variantValueId(VariantValueId.random())
                    .name(newName)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates name from command")
            void updatesName() {
                var result = freshValue().changeName(changeNameCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getName().value()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("keyId is preserved after name change")
            void keyIdPreserved() {
                var result = freshValue().changeName(changeNameCommand);

                assertThat(result.getKeyId()).isEqualTo(keyId);
            }

            @Test
            @DisplayName("ID is preserved after name change")
            void idPreserved() {
                var original = freshValue();
                var result = original.changeName(changeNameCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshValue().changeName(changeNameCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshValue();
                original.changeName(changeNameCommand);

                assertThat(original.getName()).isEqualTo(name);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshValue().changeName(changeNameCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is VariantValueNameChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshValue().changeName(changeNameCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(VariantValueNameChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the value ID")
            void eventAggregateIdMatchesValueId() {
                var result = freshValue().changeName(changeNameCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshValue().changeName(changeNameCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshValue().changeName(changeNameCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new name")
            void eventCarriesNewName() {
                var result = freshValue().changeName(changeNameCommand);
                var event = (VariantValueNameChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(newName.value());
            }
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            assertThat(freshValue().getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherKeyId = VariantKeyId.from(UUID.randomUUID());
            var otherCommand = VariantValueCreateCommand.builder()
                    .keyId(otherKeyId)
                    .name(new VariantValue("Azure"))
                    .build();

            var first = VariantValueRoot.initialize(createCommand);
            var second = VariantValueRoot.initialize(otherCommand);

            assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            assertThat(first.getKeyId().value()).isNotEqualTo(second.getKeyId().value());
            assertThat(first.getName()).isNotEqualTo(second.getName());
        }

        @Test
        @DisplayName("Sequential name changes return new instances each time")
        void sequentialNameChangesReturnNewInstances() {
            var firstName = new VariantValue("Green");
            var secondName = new VariantValue("Azure");

            var firstCmd = VariantValueChangeNameCommand.builder()
                    .variantValueId(VariantValueId.random())
                    .name(firstName)
                    .build();
            var secondCmd = VariantValueChangeNameCommand.builder()
                    .variantValueId(VariantValueId.random())
                    .name(secondName)
                    .build();

            var original = freshValue();
            var afterFirst = original.changeName(firstCmd);
            var afterSecond = afterFirst.changeName(secondCmd);

            assertThat(original.getName()).isEqualTo(name);
            assertThat(afterFirst.getName()).isEqualTo(firstName);
            assertThat(afterSecond.getName()).isEqualTo(secondName);
        }
    }
}