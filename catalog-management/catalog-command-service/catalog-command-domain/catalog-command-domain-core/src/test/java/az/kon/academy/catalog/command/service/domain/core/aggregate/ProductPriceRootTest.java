//package az.kon.academy.catalog.command.service.domain.core.aggregate;
//
//import az.kon.academy.aggragate.valueobject.Money;
//import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceCreateCommand;
//import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceChangedCommand;
//import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
//import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
//import az.kon.academy.catalog.event.product.price.ProductPriceCreatedEvent;
//import az.kon.academy.catalog.event.product.price.ProductPriceUpdatedEvent;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("ProductPriceRoot")
//class ProductPriceRootTest {
//
//    private ProductVariantId variantId;
//    private Money minPrice;
//    private Money maxPrice;
//    private ProductPriceCreateCommand createCommand;
//
//    @BeforeEach
//    void setUp() {
//        variantId = ProductVariantId.random();
//        minPrice = Money.of(new BigDecimal("10.00"));
//        maxPrice = Money.of(new BigDecimal("99.99"));
//        createCommand = ProductPriceCreateCommand.builder()
//                .variantId(variantId)
//                .minPrice(minPrice)
//                .maxPrice(maxPrice)
//                .build();
//    }
//
//    private ProductPriceAggregateRoot freshPrice() {
//        return ProductPriceAggregateRoot.initialize(createCommand);
//    }
//
//    @Nested
//    @DisplayName("initialize()")
//    class Initialize {
//
//        @Nested
//        @DisplayName("Aggregate state")
//        class AggregateState {
//
//            @Test
//            @DisplayName("Assigns a random ID")
//            void assignsRandomId() {
//                assertThat(freshPrice().getRootID()).isNotNull();
//            }
//
//            @Test
//            @DisplayName("Two instances have different IDs")
//            void differentIds() {
//                assertThat(freshPrice().getRootID()).isNotEqualTo(freshPrice().getRootID());
//            }
//
//            @Test
//            @DisplayName("Sets variantId from command")
//            void setsVariantId() {
//                assertThat(freshPrice().getVariantId()).isEqualTo(variantId);
//            }
//
//            @Test
//            @DisplayName("Sets minPrice from command")
//            void setsMinPrice() {
//                assertThat(freshPrice().getMinPrice()).isEqualTo(minPrice);
//            }
//
//            @Test
//            @DisplayName("Sets maxPrice from command")
//            void setsMaxPrice() {
//                assertThat(freshPrice().getMaxPrice()).isEqualTo(maxPrice);
//            }
//
//            @Test
//            @DisplayName("Sets modificationTs")
//            void setsModificationTs() {
//                assertThat(freshPrice().getModificationTs()).isNotNull();
//            }
//        }
//
//        @Nested
//        @DisplayName("Domain events")
//        class DomainEvents {
//
//            @Test
//            @DisplayName("Fires exactly one event")
//            void firesOneEvent() {
//                assertThat(freshPrice().getUncommittedEvents()).hasSize(1);
//            }
//
//            @Test
//            @DisplayName("Fires ProductPriceCreatedEvent")
//            void firesCorrectEventType() {
//                assertThat(freshPrice().getUncommittedEvents().getFirst())
//                        .isInstanceOf(ProductPriceCreatedEvent.class);
//            }
//
//            @Test
//            @DisplayName("Event has correct aggregateId")
//            void eventAggregateId() {
//                var price = freshPrice();
//                var event = (ProductPriceCreatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getAggregateId()).isEqualTo(price.getRootID().value().toString());
//            }
//
//            @Test
//            @DisplayName("Event has correct variantId")
//            void eventVariantId() {
//                var price = freshPrice();
//                var event = (ProductPriceCreatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getVariantId()).isEqualTo(variantId.value());
//            }
//
//            @Test
//            @DisplayName("Event has correct minPrice as string")
//            void eventMinPrice() {
//                var price = freshPrice();
//                var event = (ProductPriceCreatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getMinPrice()).isEqualTo(minPrice.toString());
//            }
//
//            @Test
//            @DisplayName("Event has correct maxPrice as string")
//            void eventMaxPrice() {
//                var price = freshPrice();
//                var event = (ProductPriceCreatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getMaxPrice()).isEqualTo(maxPrice.toString());
//            }
//
//            @Test
//            @DisplayName("Event has timestamp")
//            void eventTimestamp() {
//                var price = freshPrice();
//                var event = (ProductPriceCreatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getTimestamp()).isNotNull();
//            }
//
//            @Test
//            @DisplayName("Event has version set to 1")
//            void eventVersion() {
//                var price = freshPrice();
//                var event = (ProductPriceCreatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getVersion()).isEqualTo(1);
//            }
//        }
//    }
//
//    @Nested
//    @DisplayName("update()")
//    class Update {
//
//        private Money newMinPrice;
//        private Money newMaxPrice;
//        private ProductPriceChangedCommand updateCommand;
//
//        @BeforeEach
//        void setUpCommand() {
//            newMinPrice = Money.of(new BigDecimal("20.00"));
//            newMaxPrice = Money.of(new BigDecimal("199.99"));
//            updateCommand = ProductPriceChangedCommand.builder()
//                    .priceId(ProductPriceId.random())
//                    .minPrice(newMinPrice)
//                    .maxPrice(newMaxPrice)
//                    .build();
//        }
//
//        @Nested
//        @DisplayName("Aggregate state")
//        class AggregateState {
//
//            @Test
//            @DisplayName("Updates minPrice to new value")
//            void updatesMinPrice() {
//                var price = freshPrice().update(updateCommand);
//                assertThat(price.getMinPrice()).isEqualTo(newMinPrice);
//            }
//
//            @Test
//            @DisplayName("Updates maxPrice to new value")
//            void updatesMaxPrice() {
//                var price = freshPrice().update(updateCommand);
//                assertThat(price.getMaxPrice()).isEqualTo(newMaxPrice);
//            }
//
//            @Test
//            @DisplayName("Preserves variantId")
//            void preservesVariantId() {
//                var price = freshPrice().update(updateCommand);
//                assertThat(price.getVariantId()).isEqualTo(variantId);
//            }
//
//            @Test
//            @DisplayName("Preserves aggregate ID")
//            void preservesId() {
//                var original = freshPrice();
//                var updated = original.update(updateCommand);
//                assertThat(updated.getRootID()).isEqualTo(original.getRootID());
//            }
//
//            @Test
//            @DisplayName("Updates modificationTs")
//            void updatesModificationTs() {
//                var price = freshPrice().update(updateCommand);
//                assertThat(price.getModificationTs()).isNotNull();
//            }
//
//            @Test
//            @DisplayName("Returns a new instance (immutability)")
//            void returnsNewInstance() {
//                var original = freshPrice();
//                var updated = original.update(updateCommand);
//                assertThat(updated).isNotSameAs(original);
//            }
//
//            @Test
//            @DisplayName("Original prices are unchanged (immutability)")
//            void originalUnchanged() {
//                var original = freshPrice();
//                original.update(updateCommand);
//                assertThat(original.getMinPrice()).isEqualTo(minPrice);
//                assertThat(original.getMaxPrice()).isEqualTo(maxPrice);
//            }
//
//            @Test
//            @DisplayName("Multiple updates keep only the latest prices")
//            void multipleUpdatesKeepLatest() {
//                var secondUpdate = ProductPriceChangedCommand.builder()
//                        .priceId(ProductPriceId.random())
//                        .minPrice(Money.of(new BigDecimal("5.00")))
//                        .maxPrice(Money.of(new BigDecimal("50.00")))
//                        .build();
//
//                var price = freshPrice().update(updateCommand).update(secondUpdate);
//
//                assertThat(price.getMinPrice()).isEqualTo(Money.of(new BigDecimal("5.00")));
//                assertThat(price.getMaxPrice()).isEqualTo(Money.of(new BigDecimal("50.00")));
//            }
//        }
//
//        @Nested
//        @DisplayName("Domain events")
//        class DomainEvents {
//
//            @Test
//            @DisplayName("Fires exactly one event")
//            void firesOneEvent() {
//                var price = freshPrice().update(updateCommand);
//                assertThat(price.getUncommittedEvents()).hasSize(1);
//            }
//
//            @Test
//            @DisplayName("Fires ProductPriceUpdatedEvent")
//            void firesCorrectEventType() {
//                var price = freshPrice().update(updateCommand);
//                assertThat(price.getUncommittedEvents().getFirst())
//                        .isInstanceOf(ProductPriceUpdatedEvent.class);
//            }
//
//            @Test
//            @DisplayName("Event has correct aggregateId")
//            void eventAggregateId() {
//                var original = freshPrice();
//                var updated = original.update(updateCommand);
//                var event = (ProductPriceUpdatedEvent) updated.getUncommittedEvents().getFirst();
//                assertThat(event.getAggregateId()).isEqualTo(original.getRootID().value().toString());
//            }
//
//            @Test
//            @DisplayName("Event has correct minPrice as string")
//            void eventMinPrice() {
//                var price = freshPrice().update(updateCommand);
//                var event = (ProductPriceUpdatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getMinPrice()).isEqualTo(newMinPrice.toString());
//            }
//
//            @Test
//            @DisplayName("Event has correct maxPrice as string")
//            void eventMaxPrice() {
//                var price = freshPrice().update(updateCommand);
//                var event = (ProductPriceUpdatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getMaxPrice()).isEqualTo(newMaxPrice.toString());
//            }
//
//            @Test
//            @DisplayName("Event has timestamp")
//            void eventTimestamp() {
//                var price = freshPrice().update(updateCommand);
//                var event = (ProductPriceUpdatedEvent) price.getUncommittedEvents().getFirst();
//                assertThat(event.getTimestamp()).isNotNull();
//            }
//
//            @Test
//            @DisplayName("Each update fires its own single event on the returned instance")
//            void eachUpdateFiresOwnEvent() {
//                var first = freshPrice().update(updateCommand);
//                var second = first.update(updateCommand);
//
//                assertThat(first.getUncommittedEvents()).hasSize(1)
//                        .first().isInstanceOf(ProductPriceUpdatedEvent.class);
//                assertThat(second.getUncommittedEvents()).hasSize(1)
//                        .first().isInstanceOf(ProductPriceUpdatedEvent.class);
//            }
//        }
//    }
//}