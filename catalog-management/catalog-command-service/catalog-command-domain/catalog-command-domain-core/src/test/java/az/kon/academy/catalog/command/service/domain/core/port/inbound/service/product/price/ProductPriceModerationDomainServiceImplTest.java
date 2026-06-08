package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductPriceModerationDomainServiceImpl")
class ProductPriceModerationDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductPriceQueryOutboundPort priceQuery;

    private ProductPriceModerationDomainServiceImpl service;

    private ProductPriceId priceId;
    private ProductVariantId variantId;

    @BeforeEach
    void setUp() {
        service = new ProductPriceModerationDomainServiceImpl();

        priceId = ProductPriceId.random();
        variantId = ProductVariantId.random();

        when(context.getQueryPort(ProductPriceQueryOutboundPort.class)).thenReturn(priceQuery);
    }

    private ProductPriceAggregateRoot priceWith(BigDecimal min, BigDecimal max) {
        return ProductPriceAggregateRoot.builder()
                .id(priceId)
                .variantId(variantId)
                .minPrice(Money.of(min))
                .maxPrice(Money.of(max))
                .defaultPrice(Money.of(max))
                .actualPrice(Money.of(max))
                .autoPriceChangeEnabled(false)
                .build();
    }

    // ═════════════════════════════════════════════════════════════════════
    // changePrice
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changePrice()")
    class ChangePrice {

        @Test
        @DisplayName("Fetches by id without merchant check and delegates to changePrice")
        void fetchesByIdWithoutMerchantAndDelegates() {
            var price = priceWith(new BigDecimal("10"), new BigDecimal("50"));
            when(priceQuery.fetchById(priceId)).thenReturn(price);
            var command = ProductPriceChangedCommand.builder()
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
        @DisplayName("Throws when price not found")
        void throwsWhenPriceNotFound() {
            var ex = new RuntimeException("price not found");
            when(priceQuery.fetchById(priceId)).thenThrow(ex);
            var command = ProductPriceChangedCommand.builder()
                    .priceId(priceId)
                    .minPrice(Money.of(new BigDecimal("20")))
                    .maxPrice(Money.of(new BigDecimal("80")))
                    .build();

            assertThatThrownBy(() -> service.changePrice(context, command))
                    .isSameAs(ex);
        }
    }
}
