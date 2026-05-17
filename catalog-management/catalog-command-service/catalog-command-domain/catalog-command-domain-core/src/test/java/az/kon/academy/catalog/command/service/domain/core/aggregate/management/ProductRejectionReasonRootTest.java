package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.event.management.rejection.ProductRejectionReasonCreatedEvent;
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
    @DisplayName("Immutability")
    class Immutability {

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
