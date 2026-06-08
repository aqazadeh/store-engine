package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.aggragate.valueobject.Quantity;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.domain.core.SeDomainContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductStockManagementDomainServiceImpl")
class ProductStockManagementDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductStockQueryOutboundPort stockQuery;

    private ProductStockManagementDomainServiceImpl service;

    private MerchantId merchantId;
    private ProductStockId stockId;
    private ProductVariantId variantId;

    @BeforeEach
    void setUp() {
        service = new ProductStockManagementDomainServiceImpl();

        merchantId = MerchantId.from(UUID.randomUUID());
        stockId = ProductStockId.random();
        variantId = ProductVariantId.random();

        when(context.getQueryPort(ProductStockQueryOutboundPort.class)).thenReturn(stockQuery);
    }

    private ProductStockAggregateRoot stockWith(int quantity, int reservedQuantity) {
        return ProductStockAggregateRoot.builder()
                .id(stockId)
                .variantId(variantId)
                .quantity(Quantity.of(quantity))
                .reservedQuantity(Quantity.of(reservedQuantity))
                .build();
    }

    // ═════════════════════════════════════════════════════════════════════
    // createStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("createStock()")
    class CreateStock {

        @Test
        @DisplayName("Initializes stock aggregate with variantId and quantity")
        void initializesStockWithVariantIdAndQuantity() {
            var command = ProductStockCreateCommand.builder()
                    .variantId(variantId)
                    .quantity(Quantity.of(100))
                    .build();

            var result = service.createStock(context, command);

            assertThat(result.getRootID()).isNotNull();
            assertThat(result.getVariantId()).isEqualTo(variantId);
            assertThat(result.getQuantity()).isEqualTo(Quantity.of(100));
            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.ZERO);
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Provides zero initial reserve")
        void providesZeroInitialReserve() {
            var command = ProductStockCreateCommand.builder()
                    .variantId(ProductVariantId.random())
                    .quantity(Quantity.of(50))
                    .build();

            var result = service.createStock(context, command);

            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.ZERO);
            assertThat(result.getQuantity()).isEqualTo(Quantity.of(50));
        }

        @Test
        @DisplayName("Supports zero initial quantity")
        void supportsZeroInitialQuantity() {
            var command = ProductStockCreateCommand.builder()
                    .variantId(ProductVariantId.random())
                    .quantity(Quantity.ZERO)
                    .build();

            var result = service.createStock(context, command);

            assertThat(result.getQuantity()).isEqualTo(Quantity.ZERO);
            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.ZERO);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // increaseStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("increaseStock()")
    class IncreaseStock {

        @Test
        @DisplayName("Fetches stock by id and merchant and delegates to increase")
        void fetchesStockByIdAndMerchantAndDelegates() {
            var stock = stockWith(100, 10);
            when(stockQuery.fetchByIdAndMerchantId(stockId, merchantId)).thenReturn(stock);
            var command = ProductStockIncreaseCommand.builder()
                    .merchantId(merchantId)
                    .stockId(stockId)
                    .quantity(Quantity.of(50))
                    .build();

            var result = service.increaseStock(context, command);

            assertThat(result.getQuantity()).isEqualTo(Quantity.of(150));
            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(10));
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when stock not found for merchant")
        void throwsWhenStockNotFound() {
            var ex = new RuntimeException("stock not found");
            when(stockQuery.fetchByIdAndMerchantId(stockId, merchantId)).thenThrow(ex);
            var command = ProductStockIncreaseCommand.builder()
                    .merchantId(merchantId)
                    .stockId(stockId)
                    .quantity(Quantity.of(50))
                    .build();

            assertThatThrownBy(() -> service.increaseStock(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // decreaseStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("decreaseStock()")
    class DecreaseStock {

        @Test
        @DisplayName("Fetches stock by id and merchant and delegates to decrease")
        void fetchesStockByIdAndMerchantAndDelegates() {
            var stock = stockWith(100, 10);
            when(stockQuery.fetchByIdAndMerchantId(stockId, merchantId)).thenReturn(stock);
            var command = ProductStockDecreaseCommand.builder()
                    .merchantId(merchantId)
                    .stockId(stockId)
                    .quantity(Quantity.of(30))
                    .build();

            var result = service.decreaseStock(context, command);

            assertThat(result.getQuantity()).isEqualTo(Quantity.of(70));
            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(10));
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when insufficient stock")
        void throwsWhenInsufficientStock() {
            var stock = stockWith(20, 0);
            when(stockQuery.fetchByIdAndMerchantId(stockId, merchantId)).thenReturn(stock);
            var command = ProductStockDecreaseCommand.builder()
                    .merchantId(merchantId)
                    .stockId(stockId)
                    .quantity(Quantity.of(30))
                    .build();

            assertThatThrownBy(() -> service.decreaseStock(context, command))
                    .isInstanceOf(ProductStockDomainException.class);
        }

        @Test
        @DisplayName("Throws when stock not found for merchant")
        void throwsWhenStockNotFound() {
            var ex = new RuntimeException("stock not found");
            when(stockQuery.fetchByIdAndMerchantId(stockId, merchantId)).thenThrow(ex);
            var command = ProductStockDecreaseCommand.builder()
                    .merchantId(merchantId)
                    .stockId(stockId)
                    .quantity(Quantity.of(30))
                    .build();

            assertThatThrownBy(() -> service.decreaseStock(context, command))
                    .isSameAs(ex);
        }
    }
}
