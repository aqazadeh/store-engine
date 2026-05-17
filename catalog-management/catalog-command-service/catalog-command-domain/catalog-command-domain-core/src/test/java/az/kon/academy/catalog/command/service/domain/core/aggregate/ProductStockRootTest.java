package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.product.stock.ProductStockCreatedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockDecreasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockIncreasedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProductStockRoot")
class ProductStockRootTest {

    private ProductVariantId variantId;
    private ProductStockCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        variantId = ProductVariantId.random();
        createCommand = ProductStockCreateCommand.builder()
                .variantId(variantId)
                .quantity(100)
                .build();
    }

    private ProductStockRoot freshStock() {
        return ProductStockRoot.initialize(createCommand);
    }

    @Nested
    @DisplayName("initialize()")
    class Initialize {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a random ID")
            void assignsRandomId() {
                assertThat(freshStock().getRootID()).isNotNull();
            }

            @Test
            @DisplayName("Two instances have different IDs")
            void differentIds() {
                assertThat(freshStock().getRootID()).isNotEqualTo(freshStock().getRootID());
            }

            @Test
            @DisplayName("Sets variantId from command")
            void setsVariantId() {
                assertThat(freshStock().getVariantId()).isEqualTo(variantId);
            }

            @Test
            @DisplayName("Sets quantity from command")
            void setsQuantity() {
                assertThat(freshStock().getQuantity()).isEqualTo(100);
            }

            @Test
            @DisplayName("Sets modificationTs")
            void setsModificationTs() {
                assertThat(freshStock().getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Domain events")
        class DomainEvents {

            @Test
            @DisplayName("Fires exactly one event")
            void firesOneEvent() {
                assertThat(freshStock().getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Fires ProductStockCreatedEvent")
            void firesCorrectEventType() {
                assertThat(freshStock().getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockCreatedEvent.class);
            }

            @Test
            @DisplayName("Event has correct aggregateId")
            void eventAggregateId() {
                var stock = freshStock();
                var event = (ProductStockCreatedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getAggregateId()).isEqualTo(stock.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has correct variantId")
            void eventVariantId() {
                var stock = freshStock();
                var event = (ProductStockCreatedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getVariantId()).isEqualTo(variantId.value());
            }

            @Test
            @DisplayName("Event has correct quantity")
            void eventQuantity() {
                var stock = freshStock();
                var event = (ProductStockCreatedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getQuantity()).isEqualTo(100);
            }

            @Test
            @DisplayName("Event has timestamp")
            void eventTimestamp() {
                var stock = freshStock();
                var event = (ProductStockCreatedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getTimestamp()).isNotNull();
            }

            @Test
            @DisplayName("Event has version set to 1")
            void eventVersion() {
                var stock = freshStock();
                var event = (ProductStockCreatedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getVersion()).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("increase()")
    class Increase {

        private ProductStockIncreaseCommand increaseCommand;

        @BeforeEach
        void setUpCommand() {
            increaseCommand = ProductStockIncreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(50)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Quantity increases by the given amount")
            void quantityIncreased() {
                var stock = freshStock().increase(increaseCommand);
                assertThat(stock.getQuantity()).isEqualTo(150);
            }

            @Test
            @DisplayName("Preserves variantId")
            void preservesVariantId() {
                var stock = freshStock().increase(increaseCommand);
                assertThat(stock.getVariantId()).isEqualTo(variantId);
            }

            @Test
            @DisplayName("Preserves aggregate ID")
            void preservesId() {
                var original = freshStock();
                var stock = original.increase(increaseCommand);
                assertThat(stock.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("Updates modificationTs")
            void updatesModificationTs() {
                var original = freshStock();
                var stock = original.increase(increaseCommand);
                assertThat(stock.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Returns a new instance (immutability)")
            void returnsNewInstance() {
                var original = freshStock();
                var increased = original.increase(increaseCommand);
                assertThat(increased).isNotSameAs(original);
            }

            @Test
            @DisplayName("Original quantity is unchanged (immutability)")
            void originalUnchanged() {
                var original = freshStock();
                original.increase(increaseCommand);
                assertThat(original.getQuantity()).isEqualTo(100);
            }

            @Test
            @DisplayName("Multiple increases accumulate correctly")
            void multipleIncreases() {
                var stock = freshStock()
                        .increase(increaseCommand)
                        .increase(increaseCommand);
                assertThat(stock.getQuantity()).isEqualTo(200);
            }
        }

        @Nested
        @DisplayName("Domain events")
        class DomainEvents {

            @Test
            @DisplayName("Fires exactly one event")
            void firesOneEvent() {
                var stock = freshStock().increase(increaseCommand);
                assertThat(stock.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Fires ProductStockIncreasedEvent")
            void firesCorrectEventType() {
                var stock = freshStock().increase(increaseCommand);
                assertThat(stock.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockIncreasedEvent.class);
            }

            @Test
            @DisplayName("Event has correct aggregateId")
            void eventAggregateId() {
                var stock = freshStock();
                var increased = stock.increase(increaseCommand);
                var event = (ProductStockIncreasedEvent) increased.getUncommittedEvents().getFirst();
                assertThat(event.getAggregateId()).isEqualTo(stock.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has correct addedQuantity")
            void eventAddedQuantity() {
                var stock = freshStock().increase(increaseCommand);
                var event = (ProductStockIncreasedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getAddedQuantity()).isEqualTo(50);
            }

            @Test
            @DisplayName("Event has correct newQuantity")
            void eventNewQuantity() {
                var stock = freshStock().increase(increaseCommand);
                var event = (ProductStockIncreasedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getNewQuantity()).isEqualTo(150);
            }

            @Test
            @DisplayName("Event has timestamp")
            void eventTimestamp() {
                var stock = freshStock().increase(increaseCommand);
                var event = (ProductStockIncreasedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getTimestamp()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("decrease()")
    class Decrease {

        private ProductStockDecreaseCommand decreaseCommand;

        @BeforeEach
        void setUpCommand() {
            decreaseCommand = ProductStockDecreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(30)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Quantity decreases by the given amount")
            void quantityDecreased() {
                var stock = freshStock().decrease(decreaseCommand);
                assertThat(stock.getQuantity()).isEqualTo(70);
            }

            @Test
            @DisplayName("Preserves variantId")
            void preservesVariantId() {
                var stock = freshStock().decrease(decreaseCommand);
                assertThat(stock.getVariantId()).isEqualTo(variantId);
            }

            @Test
            @DisplayName("Preserves aggregate ID")
            void preservesId() {
                var original = freshStock();
                var stock = original.decrease(decreaseCommand);
                assertThat(stock.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("Updates modificationTs")
            void updatesModificationTs() {
                var stock = freshStock().decrease(decreaseCommand);
                assertThat(stock.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Returns a new instance (immutability)")
            void returnsNewInstance() {
                var original = freshStock();
                var decreased = original.decrease(decreaseCommand);
                assertThat(decreased).isNotSameAs(original);
            }

            @Test
            @DisplayName("Original quantity is unchanged (immutability)")
            void originalUnchanged() {
                var original = freshStock();
                original.decrease(decreaseCommand);
                assertThat(original.getQuantity()).isEqualTo(100);
            }

            @Test
            @DisplayName("Quantity can reach zero")
            void quantityReachesZero() {
                var cmd = ProductStockDecreaseCommand.builder()
                        .stockId(ProductStockId.random())
                        .quantity(100)
                        .build();
                var stock = freshStock().decrease(cmd);
                assertThat(stock.getQuantity()).isEqualTo(0);
            }

            @Test
            @DisplayName("Multiple decreases accumulate correctly")
            void multipleDecreases() {
                var stock = freshStock()
                        .decrease(decreaseCommand)
                        .decrease(decreaseCommand);
                assertThat(stock.getQuantity()).isEqualTo(40);
            }
        }

        @Nested
        @DisplayName("Domain events")
        class DomainEvents {

            @Test
            @DisplayName("Fires exactly one event")
            void firesOneEvent() {
                var stock = freshStock().decrease(decreaseCommand);
                assertThat(stock.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Fires ProductStockDecreasedEvent")
            void firesCorrectEventType() {
                var stock = freshStock().decrease(decreaseCommand);
                assertThat(stock.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductStockDecreasedEvent.class);
            }

            @Test
            @DisplayName("Event has correct aggregateId")
            void eventAggregateId() {
                var stock = freshStock();
                var decreased = stock.decrease(decreaseCommand);
                var event = (ProductStockDecreasedEvent) decreased.getUncommittedEvents().getFirst();
                assertThat(event.getAggregateId()).isEqualTo(stock.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has correct removedQuantity")
            void eventRemovedQuantity() {
                var stock = freshStock().decrease(decreaseCommand);
                var event = (ProductStockDecreasedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getRemovedQuantity()).isEqualTo(30);
            }

            @Test
            @DisplayName("Event has correct newQuantity")
            void eventNewQuantity() {
                var stock = freshStock().decrease(decreaseCommand);
                var event = (ProductStockDecreasedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getNewQuantity()).isEqualTo(70);
            }

            @Test
            @DisplayName("Event has timestamp")
            void eventTimestamp() {
                var stock = freshStock().decrease(decreaseCommand);
                var event = (ProductStockDecreasedEvent) stock.getUncommittedEvents().getFirst();
                assertThat(event.getTimestamp()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Guard — insufficient stock")
        class Guard {

            @Test
            @DisplayName("Throws when decrease amount exceeds quantity")
            void throwsWhenInsufficient() {
                var cmd = ProductStockDecreaseCommand.builder()
                        .stockId(ProductStockId.random())
                        .quantity(101)
                        .build();

                assertThatThrownBy(() -> freshStock().decrease(cmd))
                        .isInstanceOf(ProductStockDomainException.class);
            }

            @Test
            @DisplayName("Throws with INSUFFICIENT_STOCK error code")
            void throwsWithCorrectCode() {
                var cmd = ProductStockDecreaseCommand.builder()
                        .stockId(ProductStockId.random())
                        .quantity(101)
                        .build();

                assertThatThrownBy(() -> freshStock().decrease(cmd))
                        .isInstanceOf(ProductStockDomainException.class)
                        .hasMessageContaining(ProductStockDomainErrorCodes.INSUFFICIENT_STOCK);
            }

            @Test
            @DisplayName("Exact match on quantity does not throw")
            void exactMatchDoesNotThrow() {
                var cmd = ProductStockDecreaseCommand.builder()
                        .stockId(ProductStockId.random())
                        .quantity(100)
                        .build();

                assertThat(freshStock().decrease(cmd).getQuantity()).isZero();
            }

            @Test
            @DisplayName("Does not mutate state when exception is thrown")
            void noMutationOnException() {
                var original = freshStock();
                var cmd = ProductStockDecreaseCommand.builder()
                        .stockId(ProductStockId.random())
                        .quantity(999)
                        .build();

                assertThatThrownBy(() -> original.decrease(cmd))
                        .isInstanceOf(ProductStockDomainException.class);

                assertThat(original.getQuantity()).isEqualTo(100);
            }

            @Test
            @DisplayName("Does not fire event when exception is thrown")
            void noEventOnException() {
                var original = freshStock();
                var cmd = ProductStockDecreaseCommand.builder()
                        .stockId(ProductStockId.random())
                        .quantity(999)
                        .build();

                assertThatThrownBy(() -> original.decrease(cmd))
                        .isInstanceOf(ProductStockDomainException.class);

                assertThat(original.getUncommittedEvents()).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("Mixed operations")
    class MixedOperations {

        @Test
        @DisplayName("Increase then decrease yields correct quantity")
        void increaseThenDecrease() {
            var increaseCmd = ProductStockIncreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(50)
                    .build();
            var decreaseCmd = ProductStockDecreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(80)
                    .build();

            var stock = freshStock().increase(increaseCmd).decrease(decreaseCmd);
            assertThat(stock.getQuantity()).isEqualTo(70);
        }

        @Test
        @DisplayName("Decrease then increase yields correct quantity")
        void decreaseThenIncrease() {
            var decreaseCmd = ProductStockDecreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(60)
                    .build();
            var increaseCmd = ProductStockIncreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(200)
                    .build();

            var stock = freshStock().decrease(decreaseCmd).increase(increaseCmd);
            assertThat(stock.getQuantity()).isEqualTo(240);
        }

        @Test
        @DisplayName("Each operation fires its own single event on the returned instance")
        void eachOperationFiresOwnEvent() {
            var increaseCmd = ProductStockIncreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(10)
                    .build();
            var decreaseCmd = ProductStockDecreaseCommand.builder()
                    .stockId(ProductStockId.random())
                    .quantity(5)
                    .build();

            var increased = freshStock().increase(increaseCmd);
            var decreased = increased.decrease(decreaseCmd);

            assertThat(increased.getUncommittedEvents()).hasSize(1)
                    .first().isInstanceOf(ProductStockIncreasedEvent.class);
            assertThat(decreased.getUncommittedEvents()).hasSize(1)
                    .first().isInstanceOf(ProductStockDecreasedEvent.class);
        }
    }
}