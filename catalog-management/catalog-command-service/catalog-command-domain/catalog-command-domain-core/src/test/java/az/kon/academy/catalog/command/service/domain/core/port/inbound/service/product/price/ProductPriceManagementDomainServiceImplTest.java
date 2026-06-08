package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangeActualPriceCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceToggleAutoPriceCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.domain.core.SeDomainContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductPriceManagementDomainServiceImpl")
class ProductPriceManagementDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductQueryOutboundPort productQuery;

    @Mock
    private ProductPriceQueryOutboundPort priceQuery;

    @Mock
    private ProductVariantQueryOutboundPort variantQuery;

    private ProductPriceManagementDomainServiceImpl service;

    private MerchantId merchantId;
    private ProductId productId;
    private ProductVariantId variantId;
    private ProductPriceId priceId;

    @BeforeEach
    void setUp() {
        service = new ProductPriceManagementDomainServiceImpl();

        merchantId = MerchantId.from(UUID.randomUUID());
        productId = ProductId.from(UUID.randomUUID());
        variantId = ProductVariantId.random();
        priceId = ProductPriceId.random();

        when(context.getQueryPort(ProductQueryOutboundPort.class)).thenReturn(productQuery);
        when(context.getQueryPort(ProductPriceQueryOutboundPort.class)).thenReturn(priceQuery);
        lenient().when(context.getQueryPort(ProductVariantQueryOutboundPort.class)).thenReturn(variantQuery);
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

    // ═════════════════════════════════════════════════════════════════════
    // createPrice
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("createPrice()")
    class CreatePrice {

        private ProductPriceCreateCommand command;

        @BeforeEach
        void setUp() {
            command = ProductPriceCreateCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .variantId(variantId)
                    .minPrice(Money.of(new BigDecimal("10")))
                    .maxPrice(Money.of(new BigDecimal("50")))
                    .defaultPrice(Money.of(new BigDecimal("30")))
                    .autoPriceUpdateEnabled(true)
                    .build();
        }

        @Test
        @DisplayName("Checks product, variant active and initializes price")
        void checksProductAndVariantAndInitializes() {
            var result = service.createPrice(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            verify(variantQuery).checkExistsByIdAndProductIdAndMerchantId(variantId, productId, merchantId);
            assertThat(result.getRootID()).isNotNull();
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.createPrice(context, command))
                    .isSameAs(ex);
        }

        @Test
        @DisplayName("Throws when variant does not exist")
        void throwsWhenVariantNotFound() {
            var ex = new RuntimeException("variant not found");
            doThrow(ex).when(variantQuery).checkExistsByIdAndProductIdAndMerchantId(variantId, productId, merchantId);

            assertThatThrownBy(() -> service.createPrice(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changePrice
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changePrice()")
    class ChangePrice {

        @Test
        @DisplayName("Fetches by id and merchant and delegates to changePrice")
        void fetchesByIdAndMerchantAndDelegates() {
            var price = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                    new BigDecimal("30"), false);
            when(priceQuery.fetchByIdAndMerchantId(priceId, merchantId)).thenReturn(price);
            var command = ProductPriceChangedCommand.builder()
                    .merchantId(merchantId)
                    .priceId(priceId)
                    .minPrice(Money.of(new BigDecimal("20")))
                    .maxPrice(Money.of(new BigDecimal("80")))
                    .build();

            var result = service.changePrice(context, command);

            assertThat(result.getMinPrice()).isEqualTo(Money.of(new BigDecimal("20")));
            assertThat(result.getMaxPrice()).isEqualTo(Money.of(new BigDecimal("80")));
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when price not found for merchant")
        void throwsWhenPriceNotFound() {
            var ex = new RuntimeException("price not found");
            when(priceQuery.fetchByIdAndMerchantId(priceId, merchantId)).thenThrow(ex);
            var command = ProductPriceChangedCommand.builder()
                    .merchantId(merchantId)
                    .priceId(priceId)
                    .minPrice(Money.of(new BigDecimal("20")))
                    .maxPrice(Money.of(new BigDecimal("80")))
                    .build();

            assertThatThrownBy(() -> service.changePrice(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changeActualPrice
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changeActualPrice()")
    class ChangeActualPrice {

        @Test
        @DisplayName("Fetches by id and delegates to changeActualPrice")
        void fetchesAndDelegates() {
            var price = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("30"),
                    new BigDecimal("30"), false);
            when(priceQuery.fetchById(priceId)).thenReturn(price);
            var command = ProductPriceChangeActualPriceCommand.builder()
                    .priceId(priceId)
                    .actualPrice(Money.of(new BigDecimal("25")))
                    .build();

            var result = service.changeActualPrice(context, command);

            assertThat(result.getActualPrice()).isEqualTo(Money.of(new BigDecimal("25")));
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when price not found")
        void throwsWhenPriceNotFound() {
            var ex = new RuntimeException("price not found");
            when(priceQuery.fetchById(priceId)).thenThrow(ex);
            var command = ProductPriceChangeActualPriceCommand.builder()
                    .priceId(priceId)
                    .actualPrice(Money.of(new BigDecimal("25")))
                    .build();

            assertThatThrownBy(() -> service.changeActualPrice(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // toggleAutoPriceChange
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("toggleAutoPriceChange()")
    class ToggleAutoPriceChange {

        @Test
        @DisplayName("Fetches by id and delegates to toggleAutoPriceChange")
        void fetchesAndDelegates() {
            var price = priceWith(new BigDecimal("10"), new BigDecimal("50"), new BigDecimal("50"),
                    new BigDecimal("50"), false);
            when(priceQuery.fetchById(priceId)).thenReturn(price);
            var command = ProductPriceToggleAutoPriceCommand.builder()
                    .priceId(priceId)
                    .enabled(true)
                    .build();

            var result = service.toggleAutoPriceChange(context, command);

            assertThat(result.getAutoPriceChangeEnabled()).isTrue();
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when price not found")
        void throwsWhenPriceNotFound() {
            var ex = new RuntimeException("price not found");
            when(priceQuery.fetchById(priceId)).thenThrow(ex);
            var command = ProductPriceToggleAutoPriceCommand.builder()
                    .priceId(priceId)
                    .enabled(false)
                    .build();

            assertThatThrownBy(() -> service.toggleAutoPriceChange(context, command))
                    .isSameAs(ex);
        }
    }
}
