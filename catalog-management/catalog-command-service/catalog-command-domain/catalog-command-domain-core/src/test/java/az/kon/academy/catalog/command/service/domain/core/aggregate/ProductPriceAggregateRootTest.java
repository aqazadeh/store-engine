package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangeActualPriceCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceToggleAutoPriceCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductPriceDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.product.price.ProductPriceActualPriceChangedEvent;
import az.kon.academy.catalog.event.product.price.ProductPriceAutoPriceToggledEvent;
import az.kon.academy.catalog.event.product.price.ProductPriceCreatedEvent;
import az.kon.academy.catalog.event.product.price.ProductPriceEvent;
import az.kon.academy.catalog.event.product.price.ProductPriceUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProductPriceAggregateRoot")
class ProductPriceAggregateRootTest {

    private ProductVariantId variantId;
    private ProductPriceId priceId;

    @BeforeEach
    void setUp() {
        variantId = ProductVariantId.random();
        priceId = ProductPriceId.random();
    }

    private ProductPriceCreateCommand createCommand(BigDecimal min, BigDecimal max, BigDecimal defaultPrice) {
        return ProductPriceCreateCommand.builder()
                .variantId(variantId)
                .minPrice(Money.of(min))
                .maxPrice(Money.of(max))
                .defaultPrice(Money.of(defaultPrice))
                .autoPriceUpdateEnabled(true)
                .build();
    }

    private ProductPriceCreateCommand createCommand(BigDecimal min, BigDecimal max) {
        return createCommand(min, max, max);
    }

    private ProductPriceAggregateRoot priceWith(BigDecimal min, BigDecimal max, BigDecimal defaultPrice,
                                                  BigDecimal actualPrice, Boolean autoEnabled) {
        return ProductPriceAggregateRoot.builder()
                .id(priceId)
                .variantId(variantId)
                .minPrice(Money.of(min))
                .maxPrice(Money.of(max))
                .defaultPrice(Money.of(defaultPrice))
                .actualPrice(Money.of(actualPrice))
                .autoPriceChangeEnabled(autoEnabled)
                .build();
    }

    private ProductPriceAggregateRoot priceWith(BigDecimal min, BigDecimal max) {
        return priceWith(min, max, max, max, false);
    }

    private ProductPriceChangedCommand changedCommand(BigDecimal min, BigDecimal max) {
        return ProductPriceChangedCommand.builder()
                .priceId(priceId)
                .minPrice(Money.of(min))
                .maxPrice(Money.of(max))
                .build();
    }

    private ProductPriceChangeActualPriceCommand actualPriceCommand(BigDecimal actual) {
        return ProductPriceChangeActualPriceCommand.builder()
                .priceId(priceId)
                .actualPrice(Money.of(actual))
                .build();
    }

    private ProductPriceToggleAutoPriceCommand toggleCommand(boolean enabled) {
        return ProductPriceToggleAutoPriceCommand.builder()
                .priceId(priceId)
                .enabled(enabled)
                .build();
    }

    // ═════════════════════════════════════════════════════════════════════
    // from
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("from()")
    class From {

        @Test
        @DisplayName("Factory method creates aggregate with given ID and replays events")
        void createsWithGivenIdAndReplaysEvents() {
            var event = new ProductPriceCreatedEvent(
                    UUID.randomUUID(), priceId.value().toString(), OffsetDateTime.now(), 1,
                    variantId.value(),
                    new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"), new BigDecimal("30"), false);

            var result = ProductPriceAggregateRoot.from(priceId, List.of(event));

            assertThat(result.getRootID()).isEqualTo(priceId);
            assertThat(result.getMinPrice()).isEqualTo(Money.of(new BigDecimal("10")));
            assertThat(result.getMaxPrice()).isEqualTo(Money.of(new BigDecimal("50")));
            assertThat(result.getActualPrice()).isEqualTo(Money.of(new BigDecimal("30")));
            assertThat(result.getAutoPriceChangeEnabled()).isFalse();
            assertThat(result.getUncommittedEvents()).isEmpty();
        }

        @Test
        @DisplayName("Factory with empty event list produces aggregate with only ID set")
        void withEmptyEventList() {
            var result = ProductPriceAggregateRoot.from(priceId, List.of());

            assertThat(result.getRootID()).isEqualTo(priceId);
            assertThat(result.getMinPrice()).isNull();
            assertThat(result.getMaxPrice()).isNull();
            assertThat(result.getUncommittedEvents()).isEmpty();
        }
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
            @DisplayName("Sets all price fields from command")
            void setsAllPriceFields() {
                var price = ProductPriceAggregateRoot.initialize(createCommand(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30")));

                assertThat(price.getMinPrice()).isEqualTo(Money.of(new BigDecimal("10")));
                assertThat(price.getMaxPrice()).isEqualTo(Money.of(new BigDecimal("50")));
                assertThat(price.getDefaultPrice()).isEqualTo(Money.of(new BigDecimal("30")));
                assertThat(price.getActualPrice()).isEqualTo(Money.of(new BigDecimal("30")));
            }

            @Test
            @DisplayName("Sets autoPriceChangeEnabled from command")
            void setsAutoPriceEnabled() {
                var cmd = ProductPriceCreateCommand.builder()
                        .variantId(variantId)
                        .minPrice(Money.of(new BigDecimal("10")))
                        .maxPrice(Money.of(new BigDecimal("50")))
                        .defaultPrice(Money.of(new BigDecimal("30")))
                        .autoPriceUpdateEnabled(false)
                        .build();

                var price = ProductPriceAggregateRoot.initialize(cmd);

                assertThat(price.getAutoPriceChangeEnabled()).isFalse();
            }

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var price = ProductPriceAggregateRoot.initialize(createCommand(new BigDecimal("10"), new BigDecimal("50")));

                assertThat(price.getRootID()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers ProductPriceCreatedEvent")
            void registersCreatedEvent() {
                var price = ProductPriceAggregateRoot.initialize(createCommand(new BigDecimal("10"), new BigDecimal("50")));

                assertThat(price.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductPriceCreatedEvent.class);
            }

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var price = ProductPriceAggregateRoot.initialize(createCommand(new BigDecimal("10"), new BigDecimal("50")));

                assertThat(price.getUncommittedEvents()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when minPrice greater than maxPrice")
            void throwsWhenMinGreaterThanMax() {
                var cmd = createCommand(new BigDecimal("100"), new BigDecimal("50"));

                assertThatThrownBy(() -> ProductPriceAggregateRoot.initialize(cmd))
                        .isInstanceOf(ProductPriceDomainException.class);
            }

            @Test
            @DisplayName("Accepts when minPrice equals maxPrice")
            void acceptsWhenMinEqualsMax() {
                var price = ProductPriceAggregateRoot.initialize(createCommand(new BigDecimal("50"), new BigDecimal("50")));

                assertThat(price.getMinPrice()).isEqualTo(price.getMaxPrice());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changePrice
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changePrice()")
    class ChangePrice {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates minPrice and maxPrice")
            void updatesPrices() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"))
                        .changePrice(changedCommand(new BigDecimal("20"), new BigDecimal("80")));

                assertThat(result.getMinPrice()).isEqualTo(Money.of(new BigDecimal("20")));
                assertThat(result.getMaxPrice()).isEqualTo(Money.of(new BigDecimal("80")));
            }

            @Test
            @DisplayName("Preserves other fields")
            void preservesOtherFields() {
                var original = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                        new BigDecimal("25"), true);
                var result = original.changePrice(changedCommand(new BigDecimal("20"), new BigDecimal("80")));

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getVariantId()).isEqualTo(original.getVariantId());
                assertThat(result.getDefaultPrice()).isEqualTo(original.getDefaultPrice());
                assertThat(result.getActualPrice()).isEqualTo(original.getActualPrice());
                assertThat(result.getAutoPriceChangeEnabled()).isEqualTo(original.getAutoPriceChangeEnabled());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = priceWith(new BigDecimal("10"), new BigDecimal("50"));
                original.changePrice(changedCommand(new BigDecimal("20"), new BigDecimal("80")));

                assertThat(original.getMinPrice()).isEqualTo(Money.of(new BigDecimal("10")));
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers ProductPriceUpdatedEvent")
            void registersUpdatedEvent() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"))
                        .changePrice(changedCommand(new BigDecimal("20"), new BigDecimal("80")));

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductPriceUpdatedEvent.class);
                assertThat(result.getUncommittedEvents()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when minPrice greater than maxPrice")
            void throwsWhenMinGreaterThanMax() {
                assertThatThrownBy(() -> priceWith(new BigDecimal("10"), new BigDecimal("50"))
                        .changePrice(changedCommand(new BigDecimal("100"), new BigDecimal("50"))))
                        .isInstanceOf(ProductPriceDomainException.class);
            }

            @Test
            @DisplayName("Accepts when minPrice equals maxPrice")
            void acceptsWhenMinEqualsMax() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"))
                        .changePrice(changedCommand(new BigDecimal("30"), new BigDecimal("30")));

                assertThat(result.getMinPrice()).isEqualTo(result.getMaxPrice());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changeActualPrice
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changeActualPrice()")
    class ChangeActualPrice {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates actualPrice")
            void updatesActualPrice() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                                new BigDecimal("30"), false)
                        .changeActualPrice(actualPriceCommand(new BigDecimal("25")));

                assertThat(result.getActualPrice()).isEqualTo(Money.of(new BigDecimal("25")));
            }

            @Test
            @DisplayName("Preserves other fields")
            void preservesOtherFields() {
                var original = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                        new BigDecimal("30"), false);
                var result = original.changeActualPrice(actualPriceCommand(new BigDecimal("25")));

                assertThat(result.getMinPrice()).isEqualTo(original.getMinPrice());
                assertThat(result.getMaxPrice()).isEqualTo(original.getMaxPrice());
                assertThat(result.getDefaultPrice()).isEqualTo(original.getDefaultPrice());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                        new BigDecimal("30"), false);
                original.changeActualPrice(actualPriceCommand(new BigDecimal("25")));

                assertThat(original.getActualPrice()).isEqualTo(Money.of(new BigDecimal("30")));
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers ProductPriceActualPriceChangedEvent")
            void registersEvent() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                                new BigDecimal("30"), false)
                        .changeActualPrice(actualPriceCommand(new BigDecimal("25")));

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductPriceActualPriceChangedEvent.class);
                assertThat(result.getUncommittedEvents()).hasSize(1);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // toggleAutoPriceChange
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("toggleAutoPriceChange()")
    class ToggleAutoPriceChange {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Enables auto price change")
            void enablesAutoPriceChange() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"))
                        .toggleAutoPriceChange(toggleCommand(true));

                assertThat(result.getAutoPriceChangeEnabled()).isTrue();
            }

            @Test
            @DisplayName("Disables auto price change")
            void disablesAutoPriceChange() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("50"),
                                new BigDecimal("50"), true)
                        .toggleAutoPriceChange(toggleCommand(false));

                assertThat(result.getAutoPriceChangeEnabled()).isFalse();
            }

            @Test
            @DisplayName("Preserves other fields")
            void preservesOtherFields() {
                var original = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                        new BigDecimal("25"), true);
                var result = original.toggleAutoPriceChange(toggleCommand(false));

                assertThat(result.getMinPrice()).isEqualTo(original.getMinPrice());
                assertThat(result.getMaxPrice()).isEqualTo(original.getMaxPrice());
                assertThat(result.getDefaultPrice()).isEqualTo(original.getDefaultPrice());
                assertThat(result.getActualPrice()).isEqualTo(original.getActualPrice());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("50"),
                        new BigDecimal("50"), true);
                original.toggleAutoPriceChange(toggleCommand(false));

                assertThat(original.getAutoPriceChangeEnabled()).isTrue();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers ProductPriceAutoPriceToggledEvent")
            void registersEvent() {
                var result = priceWith(new BigDecimal("10"), new BigDecimal("50"))
                        .toggleAutoPriceChange(toggleCommand(true));

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductPriceAutoPriceToggledEvent.class);
                assertThat(result.getUncommittedEvents()).hasSize(1);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // Event sourcing — replay
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Event sourcing replay")
    class EventSourcingReplay {

        private ProductPriceAggregateRoot emptyPrice() {
            return ProductPriceAggregateRoot.builder()
                    .id(priceId)
                    .variantId(variantId)
                    .build();
        }

        @Test
        @DisplayName("Replay of CreatedEvent builds initial state")
        void replayCreatedEvent() {
            var event = new ProductPriceCreatedEvent(
                    UUID.randomUUID(), priceId.value().toString(), OffsetDateTime.now(), 1,
                    variantId.value(),
                    new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"), new BigDecimal("30"), false);

            var result = emptyPrice().replay(List.of(event));

            assertThat(result.getMinPrice()).isEqualTo(Money.of(new BigDecimal("10")));
            assertThat(result.getMaxPrice()).isEqualTo(Money.of(new BigDecimal("50")));
            assertThat(result.getActualPrice()).isEqualTo(Money.of(new BigDecimal("30")));
            assertThat(result.getAutoPriceChangeEnabled()).isFalse();
        }

        @Test
        @DisplayName("Replay of full lifecycle rebuilds correct state")
        void replayFullLifecycle() {
            var events = List.<ProductPriceEvent>of(
                    new ProductPriceCreatedEvent(
                            UUID.randomUUID(), priceId.value().toString(), OffsetDateTime.now(), 1,
                            variantId.value(),
                            new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                            new BigDecimal("30"), false),
                    new ProductPriceUpdatedEvent(
                            UUID.randomUUID(), priceId.value().toString(), OffsetDateTime.now(), 1,
                            new BigDecimal("20"), new BigDecimal("80")),
                    new ProductPriceActualPriceChangedEvent(
                            UUID.randomUUID(), priceId.value().toString(), OffsetDateTime.now(), 1,
                            new BigDecimal("25")),
                    new ProductPriceAutoPriceToggledEvent(
                            UUID.randomUUID(), priceId.value().toString(), OffsetDateTime.now(), 1,
                            true)
            );

            var result = emptyPrice().replay(events);

            assertThat(result.getMinPrice()).isEqualTo(Money.of(new BigDecimal("20")));
            assertThat(result.getMaxPrice()).isEqualTo(Money.of(new BigDecimal("80")));
            assertThat(result.getActualPrice()).isEqualTo(Money.of(new BigDecimal("25")));
            assertThat(result.getAutoPriceChangeEnabled()).isTrue();
        }

        @Test
        @DisplayName("Replay does not leave uncommitted events")
        void replayNoUncommittedEvents() {
            var event = new ProductPriceCreatedEvent(
                    UUID.randomUUID(), priceId.value().toString(), OffsetDateTime.now(), 1,
                    variantId.value(),
                    new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"), new BigDecimal("30"), false);

            var result = emptyPrice().replay(List.of(event));

            assertThat(result.getUncommittedEvents()).isEmpty();
        }
    }
}
