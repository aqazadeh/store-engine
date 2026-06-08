package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.aggragate.valueobject.Quantity;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReleaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReserveCommand;
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
@DisplayName("ProductStockModerationDomainServiceImpl")
class ProductStockModerationDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductStockQueryOutboundPort stockQuery;

    private ProductStockModerationDomainServiceImpl service;

    private MerchantId merchantId;
    private ProductStockId stockId;
    private ProductVariantId variantId;

    @BeforeEach
    void setUp() {
        service = new ProductStockModerationDomainServiceImpl();

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
    // increaseStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("increaseStock()")
    class IncreaseStock {

        @Test
        @DisplayName("Fetches stock and delegates to increase")
        void fetchesStockAndDelegates() {
            var stock = stockWith(100, 10);
            when(stockQuery.fetchById(stockId)).thenReturn(stock);
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
        @DisplayName("Throws when stock not found")
        void throwsWhenStockNotFound() {
            var ex = new RuntimeException("stock not found");
            when(stockQuery.fetchById(stockId)).thenThrow(ex);
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
        @DisplayName("Fetches stock and delegates to decrease")
        void fetchesStockAndDelegates() {
            var stock = stockWith(100, 10);
            when(stockQuery.fetchById(stockId)).thenReturn(stock);
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
            when(stockQuery.fetchById(stockId)).thenReturn(stock);
            var command = ProductStockDecreaseCommand.builder()
                    .merchantId(merchantId)
                    .stockId(stockId)
                    .quantity(Quantity.of(30))
                    .build();

            assertThatThrownBy(() -> service.decreaseStock(context, command))
                    .isInstanceOf(ProductStockDomainException.class);
        }

        @Test
        @DisplayName("Throws when stock not found")
        void throwsWhenStockNotFound() {
            var ex = new RuntimeException("stock not found");
            when(stockQuery.fetchById(stockId)).thenThrow(ex);
            var command = ProductStockDecreaseCommand.builder()
                    .merchantId(merchantId)
                    .stockId(stockId)
                    .quantity(Quantity.of(30))
                    .build();

            assertThatThrownBy(() -> service.decreaseStock(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // reserveStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("reserveStock()")
    class ReserveStock {

        @Test
        @DisplayName("Fetches stock and delegates to reserve")
        void fetchesStockAndDelegates() {
            var stock = stockWith(100, 5);
            when(stockQuery.fetchById(stockId)).thenReturn(stock);
            var command = ProductStockReserveCommand.builder()
                    .stockId(stockId)
                    .quantity(Quantity.of(10))
                    .build();

            var result = service.reserveStock(context, command);

            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(15));
            assertThat(result.getQuantity()).isEqualTo(Quantity.of(100));
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when insufficient available stock")
        void throwsWhenInsufficientAvailableStock() {
            var stock = stockWith(100, 95);
            when(stockQuery.fetchById(stockId)).thenReturn(stock);
            var command = ProductStockReserveCommand.builder()
                    .stockId(stockId)
                    .quantity(Quantity.of(10))
                    .build();

            assertThatThrownBy(() -> service.reserveStock(context, command))
                    .isInstanceOf(ProductStockDomainException.class);
        }

        @Test
        @DisplayName("Throws when stock not found")
        void throwsWhenStockNotFound() {
            var ex = new RuntimeException("stock not found");
            when(stockQuery.fetchById(stockId)).thenThrow(ex);
            var command = ProductStockReserveCommand.builder()
                    .stockId(stockId)
                    .quantity(Quantity.of(10))
                    .build();

            assertThatThrownBy(() -> service.reserveStock(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // releaseStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("releaseStock()")
    class ReleaseStock {

        @Test
        @DisplayName("Fetches stock and delegates to release")
        void fetchesStockAndDelegates() {
            var stock = stockWith(100, 15);
            when(stockQuery.fetchById(stockId)).thenReturn(stock);
            var command = ProductStockReleaseCommand.builder()
                    .stockId(stockId)
                    .quantity(Quantity.of(5))
                    .build();

            var result = service.releaseStock(context, command);

            assertThat(result.getReservedQuantity()).isEqualTo(Quantity.of(10));
            assertThat(result.getQuantity()).isEqualTo(Quantity.of(100));
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when releasing more than reserved")
        void throwsWhenReleasingMoreThanReserved() {
            var stock = stockWith(100, 2);
            when(stockQuery.fetchById(stockId)).thenReturn(stock);
            var command = ProductStockReleaseCommand.builder()
                    .stockId(stockId)
                    .quantity(Quantity.of(5))
                    .build();

            assertThatThrownBy(() -> service.releaseStock(context, command))
                    .isInstanceOf(ProductStockDomainException.class);
        }

        @Test
        @DisplayName("Throws when stock not found")
        void throwsWhenStockNotFound() {
            var ex = new RuntimeException("stock not found");
            when(stockQuery.fetchById(stockId)).thenThrow(ex);
            var command = ProductStockReleaseCommand.builder()
                    .stockId(stockId)
                    .quantity(Quantity.of(5))
                    .build();

            assertThatThrownBy(() -> service.releaseStock(context, command))
                    .isSameAs(ex);
        }
    }
}
