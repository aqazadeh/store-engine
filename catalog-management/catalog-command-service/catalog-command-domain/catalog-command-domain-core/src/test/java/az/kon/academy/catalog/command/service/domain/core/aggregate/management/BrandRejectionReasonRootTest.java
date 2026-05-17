package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.event.management.rejection.BrandRejectionReasonCreatedEvent;
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
    private BrandCreateRejectionReasonCommand command;

    @BeforeEach
    void setUp() {
        brandId = BrandId.from(UUID.randomUUID());
        moderatorId = ModeratorId.from(UUID.randomUUID());
        reason = "Brand logo does not meet quality standards";
        command = BrandCreateRejectionReasonCommand.builder()
                .brandId(brandId)
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
                var result = BrandRejectionReasonRoot.initialize(command);

                assertThat(result.getRootID()).isNotNull();
                assertThat(result.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = BrandRejectionReasonRoot.initialize(command);
                var second = BrandRejectionReasonRoot.initialize(command);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets brandId from command")
            void setsBrandIdFromCommand() {
                var result = BrandRejectionReasonRoot.initialize(command);

                assertThat(result.getBrandId()).isEqualTo(brandId);
                assertThat(result.getBrandId().value()).isEqualTo(brandId.value());
            }

            @Test
            @DisplayName("Sets reason from command")
            void setsReasonFromCommand() {
                var result = BrandRejectionReasonRoot.initialize(command);

                assertThat(result.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Sets moderatedBy from command")
            void setsModeratedByFromCommand() {
                var result = BrandRejectionReasonRoot.initialize(command);

                assertThat(result.getModeratedBy()).isEqualTo(moderatorId);
                assertThat(result.getModeratedBy().value()).isEqualTo(moderatorId.value());
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var result = BrandRejectionReasonRoot.initialize(command);

                assertThat(result.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = BrandRejectionReasonRoot.initialize(command);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandRejectionReasonCreatedEvent")
            void registeredEventIsCorrectType() {
                var result = BrandRejectionReasonRoot.initialize(command);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandRejectionReasonCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the rejection reason ID")
            void eventAggregateIdMatchesRejectionReasonId() {
                var result = BrandRejectionReasonRoot.initialize(command);
                var event = (BrandRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event carries the brandId from command")
            void eventCarriesBrandId() {
                var result = BrandRejectionReasonRoot.initialize(command);
                var event = (BrandRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getBrandId()).isEqualTo(brandId.value());
            }

            @Test
            @DisplayName("Event carries the reason from command")
            void eventCarriesReason() {
                var result = BrandRejectionReasonRoot.initialize(command);
                var event = (BrandRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getReason()).isEqualTo(reason);
            }

            @Test
            @DisplayName("Event carries the moderatorId from command")
            void eventCarriesModeratorId() {
                var result = BrandRejectionReasonRoot.initialize(command);
                var event = (BrandRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getModeratedBy()).isEqualTo(moderatorId.value());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = BrandRejectionReasonRoot.initialize(command);
                var event = (BrandRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = BrandRejectionReasonRoot.initialize(command);
                var event = (BrandRejectionReasonCreatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherBrandId = BrandId.from(UUID.randomUUID());
            var otherModeratorId = ModeratorId.from(UUID.randomUUID());
            var otherCommand = BrandCreateRejectionReasonCommand.builder()
                    .brandId(otherBrandId)
                    .reason("Different reason")
                    .moderatedBy(otherModeratorId)
                    .build();

            var first = BrandRejectionReasonRoot.initialize(command);
            var second = BrandRejectionReasonRoot.initialize(otherCommand);

            assertThat(first.getBrandId().value()).isNotEqualTo(second.getBrandId().value());
            assertThat(first.getReason()).isNotEqualTo(second.getReason());
            assertThat(first.getModeratedBy().value()).isNotEqualTo(second.getModeratedBy().value());
        }

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            var result = BrandRejectionReasonRoot.initialize(command);

            assertThat(result.getUncommittedEvents())
                    .isUnmodifiable();
        }
    }
}