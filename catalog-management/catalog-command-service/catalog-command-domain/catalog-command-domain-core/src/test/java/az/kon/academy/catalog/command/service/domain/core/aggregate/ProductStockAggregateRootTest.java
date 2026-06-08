package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.valueobject.Quantity;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReleaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReserveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.product.stock.ProductStockCreatedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockDecreasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockIncreasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockReleasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockReservedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProductStockAggregateRoot")
class ProductStockAggregateRootTest {

    private ProductVariantId variantId;
    private ProductStockId stockId;
    private MerchantId merchantId;
    private ProductStockCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        variantId = ProductVariantId.random();
        stockId = ProductStockId.random();
        merchantId = MerchantId.random();
        createCommand = ProductStockCreateCommand.builder()
                .variantId(variantId)
                .quantity(Quantity.of(100))
                .build();
    }

    private ProductStockAggregateRoot stockWith(int quantity, int reservedQuantity) {
        return ProductStockAggregateRoot.builder()
                .id(stockId)
                .variantId(variantId)
                .quantity(Quantity.of(quantity))
                .reservedQuantity(Quantity.of(reservedQuantity))
                .build();
    }

    private ProductStockCreateCommand createCommandWith(int quantity) {
        return ProductStockCreateCommand.builder()
                .variantId(ProductVariantId.random())
                .quantity(Quantity.of(quantity))
                .build();
    }

    private ProductStockIncreaseCommand increaseCommandWith(int qty) {
        return ProductStockIncreaseCommand.builder()
                .merchantId(merchantId)
                .stockId(stockId)
                .quantity(Quantity.of(qty))
                .build();
    }

    private ProductStockDecreaseCommand decreaseCommandWith(int qty) {
        return ProductStockDecreaseCommand.builder()
                .merchantId(merchantId)
                .stockId(stockId)
                .quantity(Quantity.of(qty))
                .build();
    }

    private ProductStockReserveCommand reserveCommandWith(int qty) {
        return ProductStockReserveCommand.builder()
                .stockId(stockId)
                .quantity(Quantity.of(qty))
                .build();
    }

    private ProductStockReleaseCommand releaseCommandWith(int qty) {
        return ProductStockReleaseCommand.builder()
                .stockId(stockId)
                .quantity(Quantity.of(qty))
                .build();
    }

    // ═════════════════════════════════════════════════════════════════════
    // initialize
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("initialize()")
    class Initialize {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(stock.getRootID()).isNotNull();
                assertThat(stock.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = ProductStockAggregateRoot.initialize(createCommand);
                var second = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets variantId from command")
            void setsVariantIdFromCommand() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(stock.getVariantId()).isEqualTo(variantId);
            }

            @Test
            @DisplayName("Sets quantity from command")
            void setsQuantityFromCommand() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(stock.getQuantity()).isEqualTo(Quantity.of(100));
            }

            @Test
            @DisplayName("Reserved quantity is zero")
            void reservedQuantityIsZero() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(stock.getReservedQuantity()).isEqualTo(Quantity.ZERO);
            }

            @Test
            @DisplayName("Supports zero initial quantity")
            void supportsZeroInitialQuantity() {
                var stock = ProductStockAggregateRoot.initialize(createCommandWith(0));

                assertThat(stock.getQuantity()).isEqualTo(Quantity.ZERO);
                assertThat(stock.getReservedQuantity()).isEqualTo(Quantity.ZERO);
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(stock.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(stock.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductStockCreatedEvent")
            void registeredEventIsProductStockCreatedEvent() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);

                assertThat(stock.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches stock ID")
            void eventAggregateIdMatchesStockId() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);
                var event = stock.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(stock.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);
                var event = stock.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);
                var event = stock.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(stock.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event variantId matches")
            void eventVariantIdMatches() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);
                var event = (ProductStockCreatedEvent) stock.getUncommittedEvents().getFirst();

                assertThat(event.getVariantId()).isEqualTo(variantId.value());
            }

            @Test
            @DisplayName("Event quantity matches")
            void eventQuantityMatches() {
                var stock = ProductStockAggregateRoot.initialize(createCommand);
                var event = (ProductStockCreatedEvent) stock.getUncommittedEvents().getFirst();

                assertThat(event.getQuantity()).isEqualTo(100);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // increase
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("increase()")
    class Increase {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Adds quantity")
            void addsQuantity() {
                var result = stockWith(100, 10).increase(increaseCommandWith(50));

                assertThat(result.getQuantity()).isEqualTo(Quantity.of(150));
            }

            @Test
            @DisplayName("Preserves reserved quantity")
            void preservesReservedQuantity() {
                var result = stockWith(100, 10).increase(increaseCommandWith(50));

                assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(10));
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = stockWith(100, 0).increase(increaseCommandWith(50));

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = stockWith(100, 0);
                original.increase(increaseCommandWith(50));

                assertThat(original.getQuantity()).isEqualTo(Quantity.of(100));
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsArePreserved() {
                var original = stockWith(100, 10);
                var result = original.increase(increaseCommandWith(50));

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getVariantId()).isEqualTo(original.getVariantId());
            }

            @Test
            @DisplayName("Supports increase by zero")
            void supportsIncreaseByZero() {
                var result = stockWith(100, 0).increase(increaseCommandWith(0));

                assertThat(result.getQuantity()).isEqualTo(Quantity.of(100));
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = stockWith(100, 0).increase(increaseCommandWith(50));

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductStockIncreasedEvent")
            void registeredEventIsProductStockIncreasedEvent() {
                var result = stockWith(100, 0).increase(increaseCommandWith(50));

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockIncreasedEvent.class);
            }

            @Test
            @DisplayName("Event addedQuantity matches")
            void eventAddedQuantityMatches() {
                var result = stockWith(100, 0).increase(increaseCommandWith(50));
                var event = (ProductStockIncreasedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getAddedQuantity()).isEqualTo(50);
            }

            @Test
            @DisplayName("Event newQuantity matches resulting quantity")
            void eventNewQuantityMatches() {
                var result = stockWith(100, 0).increase(increaseCommandWith(50));
                var event = (ProductStockIncreasedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getNewQuantity()).isEqualTo(150);
            }

            @Test
            @DisplayName("Event aggregateId matches stock ID")
            void eventAggregateIdMatchesStockId() {
                var result = stockWith(100, 0).increase(increaseCommandWith(50));
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // decrease
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("decrease()")
    class Decrease {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Subtracts quantity")
            void subtractsQuantity() {
                var result = stockWith(100, 10).decrease(decreaseCommandWith(30));

                assertThat(result.getQuantity()).isEqualTo(Quantity.of(70));
            }

            @Test
            @DisplayName("Preserves reserved quantity")
            void preservesReservedQuantity() {
                var result = stockWith(100, 10).decrease(decreaseCommandWith(30));

                assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(10));
            }

            @Test
            @DisplayName("Allows decreasing to zero")
            void allowsDecreasingToZero() {
                var result = stockWith(100, 0).decrease(decreaseCommandWith(100));

                assertThat(result.getQuantity()).isEqualTo(Quantity.ZERO);
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = stockWith(100, 0);
                original.decrease(decreaseCommandWith(30));

                assertThat(original.getQuantity()).isEqualTo(Quantity.of(100));
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsArePreserved() {
                var original = stockWith(100, 10);
                var result = original.decrease(decreaseCommandWith(30));

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getVariantId()).isEqualTo(original.getVariantId());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = stockWith(100, 0).decrease(decreaseCommandWith(30));

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductStockDecreasedEvent")
            void registeredEventIsProductStockDecreasedEvent() {
                var result = stockWith(100, 0).decrease(decreaseCommandWith(30));

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockDecreasedEvent.class);
            }

            @Test
            @DisplayName("Event removedQuantity matches")
            void eventRemovedQuantityMatches() {
                var result = stockWith(100, 0).decrease(decreaseCommandWith(30));
                var event = (ProductStockDecreasedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getRemovedQuantity()).isEqualTo(30);
            }

            @Test
            @DisplayName("Event newQuantity matches resulting quantity")
            void eventNewQuantityMatches() {
                var result = stockWith(100, 0).decrease(decreaseCommandWith(30));
                var event = (ProductStockDecreasedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getNewQuantity()).isEqualTo(70);
            }

            @Test
            @DisplayName("Event aggregateId matches stock ID")
            void eventAggregateIdMatchesStockId() {
                var result = stockWith(100, 0).decrease(decreaseCommandWith(30));
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when quantity exceeds stock")
            void throwsWhenQuantityExceedsStock() {
                assertThatThrownBy(() -> stockWith(20, 0).decrease(decreaseCommandWith(30)))
                        .isInstanceOf(ProductStockDomainException.class);
            }

            @Test
            @DisplayName("Throws when stock is zero")
            void throwsWhenStockIsZero() {
                assertThatThrownBy(() -> stockWith(0, 0).decrease(decreaseCommandWith(1)))
                        .isInstanceOf(ProductStockDomainException.class);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // availableQuantity
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("availableQuantity()")
    class AvailableQuantity {

        @Test
        @DisplayName("Returns quantity minus reserved")
        void returnsQuantityMinusReserved() {
            var stock = stockWith(100, 30);

            assertThat(stock.availableQuantity()).isEqualTo(Quantity.of(70));
        }

        @Test
        @DisplayName("Returns zero when reserved equals quantity")
        void returnsZeroWhenReservedEqualsQuantity() {
            var stock = stockWith(100, 100);

            assertThat(stock.availableQuantity()).isEqualTo(Quantity.ZERO);
        }

        @Test
        @DisplayName("Returns full quantity when nothing is reserved")
        void returnsFullQuantityWhenNothingReserved() {
            var stock = stockWith(100, 0);

            assertThat(stock.availableQuantity()).isEqualTo(Quantity.of(100));
        }

        @Test
        @DisplayName("Returns zero when quantity is zero")
        void returnsZeroWhenQuantityIsZero() {
            var stock = stockWith(0, 0);

            assertThat(stock.availableQuantity()).isEqualTo(Quantity.ZERO);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // reserve
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("reserve()")
    class Reserve {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Adds to reserved quantity")
            void addsToReservedQuantity() {
                var result = stockWith(100, 5).reserve(reserveCommandWith(10));

                assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(15));
            }

            @Test
            @DisplayName("Does not change total quantity")
            void doesNotChangeTotalQuantity() {
                var result = stockWith(100, 5).reserve(reserveCommandWith(10));

                assertThat(result.getQuantity()).isEqualTo(Quantity.of(100));
            }

            @Test
            @DisplayName("Reserves all available")
            void reservesAllAvailable() {
                var result = stockWith(100, 0).reserve(reserveCommandWith(100));

                assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(100));
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = stockWith(100, 5);
                original.reserve(reserveCommandWith(10));

                assertThat(original.getReservedQuantity()).isEqualTo(Quantity.of(5));
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsArePreserved() {
                var original = stockWith(100, 5);
                var result = original.reserve(reserveCommandWith(10));

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getVariantId()).isEqualTo(original.getVariantId());
            }

            @Test
            @DisplayName("Supports reserve by zero")
            void supportsReserveByZero() {
                var result = stockWith(100, 5).reserve(reserveCommandWith(0));

                assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(5));
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = stockWith(100, 0).reserve(reserveCommandWith(10));

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductStockReservedEvent")
            void registeredEventIsProductStockReservedEvent() {
                var result = stockWith(100, 0).reserve(reserveCommandWith(10));

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockReservedEvent.class);
            }

            @Test
            @DisplayName("Event reservedQuantity matches increment")
            void eventReservedQuantityMatches() {
                var result = stockWith(100, 0).reserve(reserveCommandWith(10));
                var event = (ProductStockReservedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getReservedQuantity()).isEqualTo(10);
            }

            @Test
            @DisplayName("Event newReservedQuantity matches resulting reserved")
            void eventNewReservedQuantityMatches() {
                var result = stockWith(100, 5).reserve(reserveCommandWith(10));
                var event = (ProductStockReservedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getNewReservedQuantity()).isEqualTo(15);
            }

            @Test
            @DisplayName("Event aggregateId matches stock ID")
            void eventAggregateIdMatchesStockId() {
                var result = stockWith(100, 0).reserve(reserveCommandWith(10));
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when reserve exceeds available")
            void throwsWhenReserveExceedsAvailable() {
                assertThatThrownBy(() -> stockWith(100, 95).reserve(reserveCommandWith(10)))
                        .isInstanceOf(ProductStockDomainException.class);
            }

            @Test
            @DisplayName("Throws when no available stock")
            void throwsWhenNoAvailableStock() {
                assertThatThrownBy(() -> stockWith(100, 100).reserve(reserveCommandWith(1)))
                        .isInstanceOf(ProductStockDomainException.class);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // release
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("release()")
    class Release {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Subtracts from reserved quantity")
            void subtractsFromReservedQuantity() {
                var result = stockWith(100, 15).release(releaseCommandWith(5));

                assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(10));
            }

            @Test
            @DisplayName("Does not change total quantity")
            void doesNotChangeTotalQuantity() {
                var result = stockWith(100, 15).release(releaseCommandWith(5));

                assertThat(result.getQuantity()).isEqualTo(Quantity.of(100));
            }

            @Test
            @DisplayName("Releases all reserved")
            void releasesAllReserved() {
                var result = stockWith(100, 15).release(releaseCommandWith(15));

                assertThat(result.getReservedQuantity()).isEqualTo(Quantity.ZERO);
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = stockWith(100, 15);
                original.release(releaseCommandWith(5));

                assertThat(original.getReservedQuantity()).isEqualTo(Quantity.of(15));
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsArePreserved() {
                var original = stockWith(100, 15);
                var result = original.release(releaseCommandWith(5));

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getVariantId()).isEqualTo(original.getVariantId());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = stockWith(100, 15).release(releaseCommandWith(5));

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductStockReleasedEvent")
            void registeredEventIsProductStockReleasedEvent() {
                var result = stockWith(100, 15).release(releaseCommandWith(5));

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockReleasedEvent.class);
            }

            @Test
            @DisplayName("Event releasedQuantity matches")
            void eventReleasedQuantityMatches() {
                var result = stockWith(100, 15).release(releaseCommandWith(5));
                var event = (ProductStockReleasedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getReleasedQuantity()).isEqualTo(5);
            }

            @Test
            @DisplayName("Event newReservedQuantity matches resulting reserved")
            void eventNewReservedQuantityMatches() {
                var result = stockWith(100, 15).release(releaseCommandWith(5));
                var event = (ProductStockReleasedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getNewReservedQuantity()).isEqualTo(10);
            }

            @Test
            @DisplayName("Event aggregateId matches stock ID")
            void eventAggregateIdMatchesStockId() {
                var result = stockWith(100, 15).release(releaseCommandWith(5));
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when release exceeds reserved")
            void throwsWhenReleaseExceedsReserved() {
                assertThatThrownBy(() -> stockWith(100, 3).release(releaseCommandWith(5)))
                        .isInstanceOf(ProductStockDomainException.class);
            }

            @Test
            @DisplayName("Throws when nothing is reserved")
            void throwsWhenNothingIsReserved() {
                assertThatThrownBy(() -> stockWith(100, 0).release(releaseCommandWith(1)))
                        .isInstanceOf(ProductStockDomainException.class);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // Event sourcing — replay
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Event sourcing replay")
    class EventSourcingReplay {

        private static ProductStockCreatedEvent createdEvent(ProductStockId id, UUID variantId, int qty) {
            return new ProductStockCreatedEvent(
                    UUID.randomUUID(), id.value().toString(), OffsetDateTime.now(), 1, variantId, qty);
        }

        private static ProductStockIncreasedEvent increasedEvent(ProductStockId id, int added, int newQty) {
            return new ProductStockIncreasedEvent(
                    UUID.randomUUID(), id.value().toString(), OffsetDateTime.now(), 1, added, newQty);
        }

        private static ProductStockDecreasedEvent decreasedEvent(ProductStockId id, int removed, int newQty) {
            return new ProductStockDecreasedEvent(
                    UUID.randomUUID(), id.value().toString(), OffsetDateTime.now(), 1, removed, newQty);
        }

        private static ProductStockReservedEvent reservedEvent(ProductStockId id, int reserved, int newReserved) {
            return new ProductStockReservedEvent(
                    UUID.randomUUID(), id.value().toString(), OffsetDateTime.now(), 1, reserved, newReserved);
        }

        private static ProductStockReleasedEvent releasedEvent(ProductStockId id, int released, int newReserved) {
            return new ProductStockReleasedEvent(
                    UUID.randomUUID(), id.value().toString(), OffsetDateTime.now(), 1, released, newReserved);
        }

        private ProductStockAggregateRoot emptyStock() {
            return ProductStockAggregateRoot.builder()
                    .id(stockId)
                    .variantId(variantId)
                    .quantity(Quantity.ZERO)
                    .reservedQuantity(Quantity.ZERO)
                    .build();
        }

        @Test
        @DisplayName("Replay of CreatedEvent builds initial state")
        void replayOfCreatedEventBuildsInitialState() {
            var events = List.<ProductStockEvent>of(
                    createdEvent(stockId, variantId.value(), 100));

            var result = emptyStock().replay(events);

            assertThat(result.getQuantity()).isEqualTo(Quantity.of(100));
            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.ZERO);
            assertThat(result.getVariantId()).isEqualTo(variantId);
        }

        @Test
        @DisplayName("Replay of empty list preserves initial builder state")
        void replayOfEmptyListPreservesState() {
            var result = emptyStock().replay(List.of());

            assertThat(result.getQuantity()).isEqualTo(Quantity.ZERO);
            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.ZERO);
            assertThat(result.getRootID()).isEqualTo(stockId);
        }

        @Test
        @DisplayName("Replay of IncreasedEvent updates quantity")
        void replayOfIncreasedEventUpdatesQuantity() {
            var events = List.<ProductStockEvent>of(
                    createdEvent(stockId, variantId.value(), 100),
                    increasedEvent(stockId, 50, 150));

            var result = emptyStock().replay(events);

            assertThat(result.getQuantity()).isEqualTo(Quantity.of(150));
        }

        @Test
        @DisplayName("Replay of DecreasedEvent updates quantity")
        void replayOfDecreasedEventUpdatesQuantity() {
            var events = List.<ProductStockEvent>of(
                    createdEvent(stockId, variantId.value(), 100),
                    decreasedEvent(stockId, 30, 70));

            var result = emptyStock().replay(events);

            assertThat(result.getQuantity()).isEqualTo(Quantity.of(70));
        }

        @Test
        @DisplayName("Replay of ReservesEvent updates reserved quantity")
        void replayOfReservedEventUpdatesReserved() {
            var events = List.<ProductStockEvent>of(
                    createdEvent(stockId, variantId.value(), 100),
                    reservedEvent(stockId, 10, 10));

            var result = emptyStock().replay(events);

            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(10));
        }

        @Test
        @DisplayName("Replay of ReleasedEvent updates reserved quantity")
        void replayOfReleasedEventUpdatesReserved() {
            var events = List.<ProductStockEvent>of(
                    createdEvent(stockId, variantId.value(), 100),
                    reservedEvent(stockId, 30, 30),
                    releasedEvent(stockId, 10, 20));

            var result = emptyStock().replay(events);

            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(20));
        }

        @Test
        @DisplayName("Replay of full lifecycle rebuilds correct state")
        void replayOfFullLifecycleRebuildsCorrectState() {
            var events = List.<ProductStockEvent>of(
                    createdEvent(stockId, variantId.value(), 200),
                    increasedEvent(stockId, 100, 300),
                    decreasedEvent(stockId, 50, 250),
                    reservedEvent(stockId, 80, 80),
                    reservedEvent(stockId, 20, 100),
                    releasedEvent(stockId, 30, 70));

            var result = emptyStock().replay(events);

            assertThat(result.getQuantity()).isEqualTo(Quantity.of(250));
            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(70));
        }

        @Test
        @DisplayName("Replay does not leave uncommitted events")
        void replayDoesNotLeaveUncommittedEvents() {
            var events = List.<ProductStockEvent>of(
                    createdEvent(stockId, variantId.value(), 100));

            var result = emptyStock().replay(events);

            assertThat(result.getUncommittedEvents()).isEmpty();
        }
    }
}
