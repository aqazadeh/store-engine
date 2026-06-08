package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonAddedEvent;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonChangedReasonEvent;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonDeletedEvent;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonSolvedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BrandRejectionReasonRoot")
class BrandRejectionReasonRootTest {

    private BrandId brandId;
    private ModeratorId moderatorId;
    private String reason;
    private BrandRejectionReasonAddCommand addCommand;

    @BeforeEach
    void setUp() {
        brandId = BrandId.from(UUID.randomUUID());
        moderatorId = ModeratorId.from(UUID.randomUUID());
        reason = "Brand name is too similar to existing trademark";
        addCommand = BrandRejectionReasonAddCommand.builder()
                .brandId(brandId)
                .moderatorId(moderatorId)
                .reason(reason)
                .build();
    }

    private BrandRejectionReasonRoot freshRejection() {
        return BrandRejectionReasonRoot.initialize(addCommand);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // initialize
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("initialize()")
    class Initialize {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(rejection.getRootID()).isNotNull();
                assertThat(rejection.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = BrandRejectionReasonRoot.initialize(addCommand);
                var second = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets brandId from command")
            void setsBrandIdFromCommand() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(rejection.getBrandId()).isEqualTo(brandId);
                assertThat(rejection.getBrandId().value()).isEqualTo(brandId.value());
            }

            @Test
            @DisplayName("Sets reason from command")
            void setsReasonFromCommand() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(rejection.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Sets moderatedBy from command")
            void setsModeratedByFromCommand() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(rejection.getModeratedBy()).isEqualTo(moderatorId);
                assertThat(rejection.getModeratedBy().value()).isEqualTo(moderatorId.value());
            }

            @Test
            @DisplayName("solved is false after initialization")
            void solvedIsFalse() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(rejection.getSolved()).isFalse();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(rejection.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandRejectionReasonAddedEvent")
            void registeredEventIsCorrectType() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);

                assertThat(rejection.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandRejectionReasonAddedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the rejection reason ID")
            void eventAggregateIdMatchesRejectionId() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);
                var event = rejection.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(rejection.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);
                var event = rejection.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);
                var event = rejection.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(rejection.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries brandId")
            void eventCarriesBrandId() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);
                var event = (BrandRejectionReasonAddedEvent) rejection.getUncommittedEvents().getFirst();

                assertThat(event.getBrandId()).isEqualTo(brandId.value());
            }

            @Test
            @DisplayName("Event carries reason")
            void eventCarriesReason() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);
                var event = (BrandRejectionReasonAddedEvent) rejection.getUncommittedEvents().getFirst();

                assertThat(event.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Event carries moderatedBy")
            void eventCarriesModeratedBy() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);
                var event = (BrandRejectionReasonAddedEvent) rejection.getUncommittedEvents().getFirst();

                assertThat(event.getModeratedBy()).isEqualTo(moderatorId.value());
            }

            @Test
            @DisplayName("Event carries solved=false")
            void eventCarriesSolvedFalse() {
                var rejection = BrandRejectionReasonRoot.initialize(addCommand);
                var event = (BrandRejectionReasonAddedEvent) rejection.getUncommittedEvents().getFirst();

                assertThat(event.getSolved()).isFalse();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // markAsSolved
    // ─────────────────────────────────────────────────────────────────────────

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
                    var result = freshRejection().markAsSolved();

                    assertThat(result.getSolved()).isTrue();
                }

                @Test
                @DisplayName("modificationTs is updated")
                void modificationTsIsUpdated() {
                    var result = freshRejection().markAsSolved();

                    assertThat(result.getModificationTs()).isNotNull();
                }

                @Test
                @DisplayName("Original aggregate is unchanged")
                void originalAggregateIsUnchanged() {
                    var original = freshRejection();
                    original.markAsSolved();

                    assertThat(original.getSolved()).isFalse();
                }

                @Test
                @DisplayName("Other fields are preserved")
                void otherFieldsPreserved() {
                    var original = freshRejection();
                    var result = original.markAsSolved();

                    assertThat(result.getRootID()).isEqualTo(original.getRootID());
                    assertThat(result.getBrandId()).isEqualTo(original.getBrandId());
                    assertThat(result.getReason()).isEqualTo(original.getReason());
                    assertThat(result.getModeratedBy()).isEqualTo(original.getModeratedBy());
                }
            }

            @Nested
            @DisplayName("Event publishing")
            class EventPublishing {

                @Test
                @DisplayName("Registers exactly one uncommitted event")
                void registersExactlyOneEvent() {
                    var result = freshRejection().markAsSolved();

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is BrandRejectionReasonSolvedEvent")
                void registeredEventIsCorrectType() {
                    var result = freshRejection().markAsSolved();

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(BrandRejectionReasonSolvedEvent.class);
                }

                @Test
                @DisplayName("Event solved is true")
                void eventSolvedIsTrue() {
                    var result = freshRejection().markAsSolved();
                    var event = (BrandRejectionReasonSolvedEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.getSolved()).isTrue();
                }

                @Test
                @DisplayName("Event aggregateId matches the rejection reason ID")
                void eventAggregateIdMatchesRejectionId() {
                    var result = freshRejection().markAsSolved();
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getAggregateId())
                            .isEqualTo(result.getRootID().value().toString());
                }

                @Test
                @DisplayName("Event timestamp matches aggregate modificationTs")
                void eventTimestampMatchesModificationTs() {
                    var result = freshRejection().markAsSolved();
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getTimestamp())
                            .isEqualTo(result.getModificationTs().toOffsetDateTime());
                }
            }
        }

        @Nested
        @DisplayName("When already solved")
        class WhenAlreadySolved {

            @Test
            @DisplayName("Returns the same aggregate unchanged")
            void returnsSameAggregateUnchanged() {
                var solved = freshRejection().markAsSolved();
                var result = solved.markAsSolved();

                assertThat(result).isSameAs(solved);
                assertThat(result.getSolved()).isTrue();
            }

            @Test
            @DisplayName("Registers no additional events")
            void registersNoAdditionalEvents() {
                var solved = freshRejection().markAsSolved();
                var result = solved.markAsSolved();

                assertThat(result.getUncommittedEvents()).hasSize(1);
                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandRejectionReasonSolvedEvent.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeReason
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeReason()")
    class ChangeReason {

        private String newReason;
        private ModeratorId newModeratorId;
        private BrandRejectionReasonChangeReasonCommand changeCommand;

        @BeforeEach
        void setUpChangeCommand() {
            newReason = "Brand logo infringes on copyright";
            newModeratorId = ModeratorId.from(UUID.randomUUID());
            changeCommand = BrandRejectionReasonChangeReasonCommand.builder()
                    .brandRejectionReasonId(BrandRejectionReasonId.random())
                    .reason(newReason)
                    .moderatorId(newModeratorId)
                    .build();
        }

        @Nested
        @DisplayName("When reason is different")
        class WhenReasonIsDifferent {

            @Nested
            @DisplayName("Aggregate state")
            class AggregateState {

                @Test
                @DisplayName("Updates reason from command")
                void updatesReason() {
                    var result = freshRejection().changeReason(changeCommand);

                    assertThat(result.getReason()).isEqualTo(newReason);
                }

                @Test
                @DisplayName("Updates moderatedBy from command")
                void updatesModeratedBy() {
                    var result = freshRejection().changeReason(changeCommand);

                    assertThat(result.getModeratedBy()).isEqualTo(newModeratorId);
                    assertThat(result.getModeratedBy().value()).isEqualTo(newModeratorId.value());
                }

                @Test
                @DisplayName("Original aggregate is unchanged")
                void originalAggregateIsUnchanged() {
                    var original = freshRejection();
                    original.changeReason(changeCommand);

                    assertThat(original.getReason()).isEqualTo(reason);
                    assertThat(original.getModeratedBy()).isEqualTo(moderatorId);
                }

                @Test
                @DisplayName("solved status is preserved")
                void solvedStatusPreserved() {
                    var rejection = freshRejection();
                    var result = rejection.changeReason(changeCommand);

                    assertThat(result.getSolved()).isEqualTo(rejection.getSolved());
                }

                @Test
                @DisplayName("Other fields are preserved")
                void otherFieldsPreserved() {
                    var original = freshRejection();
                    var result = original.changeReason(changeCommand);

                    assertThat(result.getRootID()).isEqualTo(original.getRootID());
                    assertThat(result.getBrandId()).isEqualTo(original.getBrandId());
                }
            }

            @Nested
            @DisplayName("Event publishing")
            class EventPublishing {

                @Test
                @DisplayName("Registers exactly one uncommitted event")
                void registersExactlyOneEvent() {
                    var result = freshRejection().changeReason(changeCommand);

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is BrandRejectionReasonChangedReasonEvent")
                void registeredEventIsCorrectType() {
                    var result = freshRejection().changeReason(changeCommand);

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(BrandRejectionReasonChangedReasonEvent.class);
                }

                @Test
                @DisplayName("Event carries the new reason")
                void eventCarriesNewReason() {
                    var result = freshRejection().changeReason(changeCommand);
                    var event = (BrandRejectionReasonChangedReasonEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.getReason()).isEqualTo(newReason);
                }

                @Test
                @DisplayName("Event carries the new moderatedBy")
                void eventCarriesNewModeratedBy() {
                    var result = freshRejection().changeReason(changeCommand);
                    var event = (BrandRejectionReasonChangedReasonEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.getModeratedBy()).isEqualTo(newModeratorId.value());
                }

                @Test
                @DisplayName("Event aggregateId matches the rejection reason ID")
                void eventAggregateIdMatchesRejectionId() {
                    var result = freshRejection().changeReason(changeCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getAggregateId())
                            .isEqualTo(result.getRootID().value().toString());
                }

                @Test
                @DisplayName("Event timestamp matches aggregate modificationTs")
                void eventTimestampMatchesModificationTs() {
                    var result = freshRejection().changeReason(changeCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getTimestamp())
                            .isEqualTo(result.getModificationTs().toOffsetDateTime());
                }
            }
        }

        @Nested
        @DisplayName("When reason is the same")
        class WhenReasonIsSame {

            @Test
            @DisplayName("Returns the same aggregate unchanged")
            void returnsSameAggregateUnchanged() {
                var sameReasonCommand = BrandRejectionReasonChangeReasonCommand.builder()
                        .brandRejectionReasonId(BrandRejectionReasonId.random())
                        .reason(reason)
                        .moderatorId(newModeratorId)
                        .build();
                var original = freshRejection();
                var result = original.changeReason(sameReasonCommand);

                assertThat(result).isSameAs(original);
                assertThat(result.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Registers no additional events")
            void registersNoAdditionalEvents() {
                var sameReasonCommand = BrandRejectionReasonChangeReasonCommand.builder()
                        .brandRejectionReasonId(BrandRejectionReasonId.random())
                        .reason(reason)
                        .moderatorId(newModeratorId)
                        .build();
                var result = freshRejection().changeReason(sameReasonCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandRejectionReasonAddedEvent.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // remove
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("remove()")
    class Remove {

        private ModeratorId removeModeratorId;
        private BrandRejectionReasonRemoveCommand removeCommand;

        @BeforeEach
        void setUpRemoveCommand() {
            removeModeratorId = ModeratorId.from(UUID.randomUUID());
            removeCommand = BrandRejectionReasonRemoveCommand.builder()
                    .brandRejectionReasonId(BrandRejectionReasonId.random())
                    .moderatorId(removeModeratorId)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Row status becomes DELETED")
            void rowStatusIsDeleted() {
                var result = freshRejection().remove(removeCommand);

                assertThat(result.getRowStatus()).isEqualTo(RowStatus.DELETED);
            }

            @Test
            @DisplayName("Updates moderatedBy from command")
            void updatesModeratedBy() {
                var result = freshRejection().remove(removeCommand);

                assertThat(result.getModeratedBy()).isEqualTo(removeModeratorId);
                assertThat(result.getModeratedBy().value()).isEqualTo(removeModeratorId.value());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshRejection();
                original.remove(removeCommand);

                assertThat(original.getRowStatus()).isEqualTo(RowStatus.ACTIVE);
                assertThat(original.getModeratedBy()).isEqualTo(moderatorId);
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = freshRejection();
                var result = original.remove(removeCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getBrandId()).isEqualTo(original.getBrandId());
                assertThat(result.getReason()).isEqualTo(original.getReason());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshRejection().remove(removeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandRejectionReasonDeletedEvent")
            void registeredEventIsCorrectType() {
                var result = freshRejection().remove(removeCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandRejectionReasonDeletedEvent.class);
            }

            @Test
            @DisplayName("Event carries moderatedBy")
            void eventCarriesModeratedBy() {
                var result = freshRejection().remove(removeCommand);
                var event = (BrandRejectionReasonDeletedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getModeratedBy()).isEqualTo(removeModeratorId.value());
            }

            @Test
            @DisplayName("Event aggregateId matches the rejection reason ID")
            void eventAggregateIdMatchesRejectionId() {
                var result = freshRejection().remove(removeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshRejection().remove(removeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Immutability
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            var rejection = freshRejection();

            assertThat(rejection.getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherCommand = BrandRejectionReasonAddCommand.builder()
                    .brandId(BrandId.from(UUID.randomUUID()))
                    .moderatorId(ModeratorId.from(UUID.randomUUID()))
                    .reason("Insufficient product information")
                    .build();

            var first = BrandRejectionReasonRoot.initialize(addCommand);
            var second = BrandRejectionReasonRoot.initialize(otherCommand);

            assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            assertThat(first.getBrandId().value()).isNotEqualTo(second.getBrandId().value());
            assertThat(first.getReason()).isNotEqualTo(second.getReason());
        }

        @Test
        @DisplayName("Sequential operations return new instances each time")
        void sequentialOperationsReturnNewInstances() {
            var removeCmd = BrandRejectionReasonRemoveCommand.builder()
                    .brandRejectionReasonId(BrandRejectionReasonId.random())
                    .moderatorId(ModeratorId.from(UUID.randomUUID()))
                    .build();

            var original = freshRejection();
            var solved = original.markAsSolved();
            var deleted = solved.remove(removeCmd);

            assertThat(original.getSolved()).isFalse();
            assertThat(solved.getSolved()).isTrue();
            assertThat(deleted.getRowStatus()).isEqualTo(RowStatus.DELETED);
            assertThat(original.getRowStatus()).isEqualTo(RowStatus.ACTIVE);
        }
    }
}
