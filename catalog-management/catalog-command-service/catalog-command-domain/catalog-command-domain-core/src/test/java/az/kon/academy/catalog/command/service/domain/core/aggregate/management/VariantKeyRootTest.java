package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyChangeDescriptionCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import az.kon.academy.catalog.event.management.variant.VariantKeyCreatedEvent;
import az.kon.academy.catalog.event.management.variant.VariantKeyDescriptionChangedEvent;
import az.kon.academy.catalog.event.management.variant.VariantKeyNameChangedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("VariantKeyRoot")
class VariantKeyRootTest {

    private VariantName name;
    private String description;
    private VariantKeyCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        name = new VariantName("Color");
        description = "Defines the color of a product variant";
        createCommand = VariantKeyCreateCommand.builder()
                .name(name)
                .description(description)
                .build();
    }

    private VariantKeyRoot freshKey() {
        return VariantKeyRoot.initialize(createCommand);
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
                var key = VariantKeyRoot.initialize(createCommand);

                assertThat(key.getRootID()).isNotNull();
                assertThat(key.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = VariantKeyRoot.initialize(createCommand);
                var second = VariantKeyRoot.initialize(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets name from command")
            void setsNameFromCommand() {
                var key = VariantKeyRoot.initialize(createCommand);

                assertThat(key.getName()).isEqualTo(name);
                assertThat(key.getName().value()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Sets description from command")
            void setsDescriptionFromCommand() {
                var key = VariantKeyRoot.initialize(createCommand);

                assertThat(key.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var key = VariantKeyRoot.initialize(createCommand);

                assertThat(key.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Description can be null")
            void descriptionCanBeNull() {
                var commandWithoutDescription = VariantKeyCreateCommand.builder()
                        .name(name)
                        .description(null)
                        .build();

                var key = VariantKeyRoot.initialize(commandWithoutDescription);

                assertThat(key.getDescription()).isNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var key = VariantKeyRoot.initialize(createCommand);

                assertThat(key.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is VariantKeyCreatedEvent")
            void registeredEventIsCorrectType() {
                var key = VariantKeyRoot.initialize(createCommand);

                assertThat(key.getUncommittedEvents().getFirst())
                        .isInstanceOf(VariantKeyCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the key ID")
            void eventAggregateIdMatchesKeyId() {
                var key = VariantKeyRoot.initialize(createCommand);
                var event = key.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(key.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var key = VariantKeyRoot.initialize(createCommand);
                var event = key.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var key = VariantKeyRoot.initialize(createCommand);
                var event = key.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(key.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the name from command")
            void eventCarriesName() {
                var key = VariantKeyRoot.initialize(createCommand);
                var event = (VariantKeyCreatedEvent) key.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Event carries the description from command")
            void eventCarriesDescription() {
                var key = VariantKeyRoot.initialize(createCommand);
                var event = (VariantKeyCreatedEvent) key.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(description);
            }
        }
    }

    @Nested
    @DisplayName("changeName()")
    class ChangeName {

        private VariantName newName;
        private VariantKeyChangeNameCommand changeNameCommand;

        @BeforeEach
        void setUpCommand() {
            newName = new VariantName("Style");
            changeNameCommand = VariantKeyChangeNameCommand.builder()
                    .variantKeyId(VariantKeyId.random())
                    .name(newName)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates name from command")
            void updatesName() {
                var result = freshKey().changeName(changeNameCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getName().value()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("Description is preserved after name change")
            void descriptionPreserved() {
                var result = freshKey().changeName(changeNameCommand);

                assertThat(result.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("ID is preserved after name change")
            void idPreserved() {
                var original = freshKey();
                var result = original.changeName(changeNameCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshKey().changeName(changeNameCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshKey();
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
                var result = freshKey().changeName(changeNameCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is VariantKeyNameChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshKey().changeName(changeNameCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(VariantKeyNameChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the key ID")
            void eventAggregateIdMatchesKeyId() {
                var result = freshKey().changeName(changeNameCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshKey().changeName(changeNameCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshKey().changeName(changeNameCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new name")
            void eventCarriesNewName() {
                var result = freshKey().changeName(changeNameCommand);
                var event = (VariantKeyNameChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(newName.value());
            }
        }
    }

    @Nested
    @DisplayName("changeDescription()")
    class ChangeDescription {

        private String newDescription;
        private VariantKeyChangeDescriptionCommand changeDescriptionCommand;

        @BeforeEach
        void setUpCommand() {
            newDescription = "Defines the size of a product variant";
            changeDescriptionCommand = VariantKeyChangeDescriptionCommand.builder()
                    .variantKeyId(VariantKeyId.random())
                    .description(newDescription)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates description from command")
            void updatesDescription() {
                var result = freshKey().changeDescription(changeDescriptionCommand);

                assertThat(result.getDescription()).isEqualTo(newDescription);
            }

            @Test
            @DisplayName("Name is preserved after description change")
            void namePreserved() {
                var result = freshKey().changeDescription(changeDescriptionCommand);

                assertThat(result.getName()).isEqualTo(name);
            }

            @Test
            @DisplayName("ID is preserved after description change")
            void idPreserved() {
                var original = freshKey();
                var result = original.changeDescription(changeDescriptionCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshKey().changeDescription(changeDescriptionCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshKey();
                original.changeDescription(changeDescriptionCommand);

                assertThat(original.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("Description can be changed to null")
            void descriptionCanBeChangedToNull() {
                var commandWithNull = VariantKeyChangeDescriptionCommand.builder()
                        .variantKeyId(VariantKeyId.random())
                        .description(null)
                        .build();

                var result = freshKey().changeDescription(commandWithNull);

                assertThat(result.getDescription()).isNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshKey().changeDescription(changeDescriptionCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is VariantKeyDescriptionChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshKey().changeDescription(changeDescriptionCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(VariantKeyDescriptionChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the key ID")
            void eventAggregateIdMatchesKeyId() {
                var result = freshKey().changeDescription(changeDescriptionCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshKey().changeDescription(changeDescriptionCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshKey().changeDescription(changeDescriptionCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new description")
            void eventCarriesNewDescription() {
                var result = freshKey().changeDescription(changeDescriptionCommand);
                var event = (VariantKeyDescriptionChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(newDescription);
            }
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            assertThat(freshKey().getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherCommand = VariantKeyCreateCommand.builder()
                    .name(new VariantName("Style"))
                    .description("Defines the size of a product")
                    .build();

            var first = VariantKeyRoot.initialize(createCommand);
            var second = VariantKeyRoot.initialize(otherCommand);

            assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            assertThat(first.getName()).isNotEqualTo(second.getName());
        }

        @Test
        @DisplayName("Sequential operations return new instances each time")
        void sequentialOperationsReturnNewInstances() {
            var newName = new VariantName("Style");
            var changeCmd = VariantKeyChangeNameCommand.builder()
                    .variantKeyId(VariantKeyId.random())
                    .name(newName)
                    .build();

            var original = freshKey();
            var updated = original.changeName(changeCmd);

            assertThat(original.getName()).isEqualTo(name);
            assertThat(updated.getName()).isEqualTo(newName);
        }
    }
}