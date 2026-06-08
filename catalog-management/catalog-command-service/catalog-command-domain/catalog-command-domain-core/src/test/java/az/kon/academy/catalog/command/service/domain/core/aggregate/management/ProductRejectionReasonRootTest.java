package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonChangedReasonEvent;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonCreatedEvent;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonDeletedEvent;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonSolvedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductRejectionReasonRoot")
class ProductRejectionReasonRootTest {

    private ProductId productId;
    private ModeratorId moderatorId;
    private String reason;
    private ProductCreateRejectionReasonCommand command;

    @BeforeEach
    void setUp() {
        productId = ProductId.from(UUID.randomUUID());
        moderatorId = ModeratorId.from(UUID.randomUUID());
        reason = "Product description contains prohibited content";
        command = ProductCreateRejectionReasonCommand.builder()
                .productId(productId)
                .reason(reason)
                .moderatedBy(moderatorId)
                .build();
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
                var result = ProductRejectionReasonRoot.initialize(command);

                assertThat(result.getRootID()).isNotNull();
                assertThat(result.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = ProductRejectionReasonRoot.initialize(command);
                var second = ProductRejectionReasonRoot.initialize(command);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets productId from command")
            void setsProductIdFromCommand() {
                var result = ProductRejectionReasonRoot.initialize(command);

                assertThat(result.getProductId()).isEqualTo(productId);
                assertThat(result.getProductId().value()).isEqualTo(productId.value());
            }

            @Test
            @DisplayName("Sets reason from command")
            void setsReasonFromCommand() {
                var result = ProductRejectionReasonRoot.initialize(command);

                assertThat(result.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Sets moderatedBy from command")
            void setsModeratedByFromCommand() {
                var result = ProductRejectionReasonRoot.initialize(command);

                assertThat(result.getModeratedBy()).isEqualTo(moderatorId);
                assertThat(result.getModeratedBy().value()).isEqualTo(moderatorId.value());
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var result = ProductRejectionReasonRoot.initialize(command);

                assertThat(result.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = ProductRejectionReasonRoot.initialize(command);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductRejectionReasonCreatedEvent")
            void registeredEventIsCorrectType() {
                var result = ProductRejectionReasonRoot.initialize(command);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductRejectionReasonCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the rejection reason ID")
            void eventAggregateIdMatchesRejectionReasonId() {
                var result = ProductRejectionReasonRoot.initialize(command);
                var event = (ProductRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event carries the productId from command")
            void eventCarriesProductId() {
                var result = ProductRejectionReasonRoot.initialize(command);
                var event = (ProductRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getProductId()).isEqualTo(productId.value());
            }

            @Test
            @DisplayName("Event carries the reason from command")
            void eventCarriesReason() {
                var result = ProductRejectionReasonRoot.initialize(command);
                var event = (ProductRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Event carries the moderatorId from command")
            void eventCarriesModeratorId() {
                var result = ProductRejectionReasonRoot.initialize(command);
                var event = (ProductRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getModeratedBy()).isEqualTo(moderatorId.value());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = ProductRejectionReasonRoot.initialize(command);
                var event = (ProductRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = ProductRejectionReasonRoot.initialize(command);
                var event = (ProductRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }
        }
    }

    @Nested
    @DisplayName("markAsSolved()")
    class MarkAsSolved {

        @Nested
        @DisplayName("When not yet solved")
        class WhenNotYetSolved {

            @Nested
            @DisplayName("Aggregate state")
            class AggregateState {

                @Test
                @DisplayName("solved becomes true")
                void solvedBecomesTrue() {
                    var result = ProductRejectionReasonRoot.initialize(command).markAsSolved();

                    assertThat(result.getSolved()).isTrue();
                }

                @Test
                @DisplayName("Original aggregate is unchanged")
                void originalAggregateIsUnchanged() {
                    var original = ProductRejectionReasonRoot.initialize(command);
                    original.markAsSolved();

                    assertThat(original.getSolved()).isFalse();
                }
            }

            @Nested
            @DisplayName("Event publishing")
            class EventPublishing {

                @Test
                @DisplayName("Registers exactly one uncommitted event")
                void registersExactlyOneEvent() {
                    var result = ProductRejectionReasonRoot.initialize(command).markAsSolved();

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is ProductRejectionReasonSolvedEvent")
                void registeredEventIsCorrectType() {
                    var result = ProductRejectionReasonRoot.initialize(command).markAsSolved();

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(ProductRejectionReasonSolvedEvent.class);
                }
            }
        }

        @Nested
        @DisplayName("When already solved")
        class WhenAlreadySolved {

            @Test
            @DisplayName("Returns the same aggregate unchanged")
            void returnsSameAggregateUnchanged() {
                var solved = ProductRejectionReasonRoot.initialize(command).markAsSolved();
                var result = solved.markAsSolved();

                assertThat(result).isSameAs(solved);
                assertThat(result.getSolved()).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("changeReason()")
    class ChangeReason {

        private String newReason;
        private ModeratorId newModeratorId;
        private ProductRejectionReasonChangeReasonCommand changeCommand;

        @BeforeEach
        void setUp() {
            newReason = "Updated rejection reason";
            newModeratorId = ModeratorId.from(UUID.randomUUID());
            changeCommand = ProductRejectionReasonChangeReasonCommand.builder()
                    .productRejectionReasonId(ProductRejectionReasonId.random())
                    .reason(newReason)
                    .moderatorId(newModeratorId)
                    .build();
        }

        @Nested
        @DisplayName("When reason is different")
        class WhenReasonIsDifferent {

            @Test
            @DisplayName("Updates reason from command")
            void updatesReason() {
                var result = ProductRejectionReasonRoot.initialize(command).changeReason(changeCommand);

                assertThat(result.getReason()).isEqualTo(newReason);
            }

            @Test
            @DisplayName("Updates moderatedBy from command")
            void updatesModeratedBy() {
                var result = ProductRejectionReasonRoot.initialize(command).changeReason(changeCommand);

                assertThat(result.getModeratedBy()).isEqualTo(newModeratorId);
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = ProductRejectionReasonRoot.initialize(command);
                original.changeReason(changeCommand);

                assertThat(original.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = ProductRejectionReasonRoot.initialize(command).changeReason(changeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductRejectionReasonChangedReasonEvent")
            void registeredEventIsCorrectType() {
                var result = ProductRejectionReasonRoot.initialize(command).changeReason(changeCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductRejectionReasonChangedReasonEvent.class);
            }
        }

        @Nested
        @DisplayName("When reason is the same")
        class WhenReasonIsSame {

            @Test
            @DisplayName("Returns the same aggregate unchanged")
            void returnsSameAggregateUnchanged() {
                var sameReasonCommand = ProductRejectionReasonChangeReasonCommand.builder()
                        .productRejectionReasonId(ProductRejectionReasonId.random())
                        .reason(reason)
                        .moderatorId(newModeratorId)
                        .build();
                var original = ProductRejectionReasonRoot.initialize(command);
                var result = original.changeReason(sameReasonCommand);

                assertThat(result).isSameAs(original);
                assertThat(result.getReason()).isEqualTo(reason);
            }
        }
    }

    @Nested
    @DisplayName("remove()")
    class Remove {

        private ModeratorId removeModeratorId;
        private ProductRejectionReasonRemoveCommand removeCommand;

        @BeforeEach
        void setUp() {
            removeModeratorId = ModeratorId.from(UUID.randomUUID());
            removeCommand = ProductRejectionReasonRemoveCommand.builder()
                    .productRejectionReasonId(ProductRejectionReasonId.random())
                    .moderatorId(removeModeratorId)
                    .build();
        }

        @Test
        @DisplayName("Row status becomes DELETED")
        void rowStatusIsDeleted() {
            var result = ProductRejectionReasonRoot.initialize(command).remove(removeCommand);

            assertThat(result.getRowStatus()).isEqualTo(RowStatus.DELETED);
        }

        @Test
        @DisplayName("Updates moderatedBy from command")
        void updatesModeratedBy() {
            var result = ProductRejectionReasonRoot.initialize(command).remove(removeCommand);

            assertThat(result.getModeratedBy()).isEqualTo(removeModeratorId);
        }

        @Test
        @DisplayName("Original aggregate is unchanged")
        void originalAggregateIsUnchanged() {
            var original = ProductRejectionReasonRoot.initialize(command);
            original.remove(removeCommand);

            assertThat(original.getRowStatus()).isEqualTo(RowStatus.ACTIVE);
        }

        @Test
        @DisplayName("Registers exactly one uncommitted event")
        void registersExactlyOneEvent() {
            var result = ProductRejectionReasonRoot.initialize(command).remove(removeCommand);

            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Registered event is ProductRejectionReasonDeletedEvent")
        void registeredEventIsCorrectType() {
            var result = ProductRejectionReasonRoot.initialize(command).remove(removeCommand);

            assertThat(result.getUncommittedEvents().getFirst())
                    .isInstanceOf(ProductRejectionReasonDeletedEvent.class);
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherProductId = ProductId.from(UUID.randomUUID());
            var otherModeratorId = ModeratorId.from(UUID.randomUUID());
            var otherCommand = ProductCreateRejectionReasonCommand.builder()
                    .productId(otherProductId)
                    .reason("Different reason")
                    .moderatedBy(otherModeratorId)
                    .build();

            var first = ProductRejectionReasonRoot.initialize(command);
            var second = ProductRejectionReasonRoot.initialize(otherCommand);

            assertThat(first.getProductId().value()).isNotEqualTo(second.getProductId().value());
            assertThat(first.getReason()).isNotEqualTo(second.getReason());
            assertThat(first.getModeratedBy().value()).isNotEqualTo(second.getModeratedBy().value());
        }

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            var result = ProductRejectionReasonRoot.initialize(command);

            assertThat(result.getUncommittedEvents())
                    .isUnmodifiable();
        }
    }
}
