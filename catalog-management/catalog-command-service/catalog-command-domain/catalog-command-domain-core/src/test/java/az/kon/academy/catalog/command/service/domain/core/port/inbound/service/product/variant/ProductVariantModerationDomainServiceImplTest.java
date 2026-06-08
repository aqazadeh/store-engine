package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantArchiveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantDiscontinueCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantMarkOutOfStockCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantSku;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantStatus;
import az.kon.academy.domain.core.SeDomainContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductVariantModerationDomainServiceImpl")
class ProductVariantModerationDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductQueryOutboundPort productQuery;

    @Mock
    private ProductVariantQueryOutboundPort variantQuery;

    private ProductVariantModerationDomainServiceImpl service;

    private MerchantId merchantId;
    private ProductId productId;
    private ProductVariantId variantId;
    private ProductVariantAssignment assignment;
    private Barcode barcode;
    private ProductVariantSku sku;

    @BeforeEach
    void setUp() {
        service = new ProductVariantModerationDomainServiceImpl();

        merchantId = MerchantId.from(UUID.randomUUID());
        productId = ProductId.from(UUID.randomUUID());
        variantId = ProductVariantId.random();
        assignment = ProductVariantAssignment.of(VariantKeyId.random(), VariantValueId.random());
        barcode = Barcode.of("1234567890123");
        sku = ProductVariantSku.of("SKU-TEST-001");

        when(context.getQueryPort(ProductQueryOutboundPort.class)).thenReturn(productQuery);
        lenient().when(context.getQueryPort(ProductVariantQueryOutboundPort.class)).thenReturn(variantQuery);
    }

    private ProductVariantRoot variantInActive() {
        return ProductVariantRoot.builder()
                .id(variantId)
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .images(List.of())
                .status(ProductVariantStatus.ACTIVE)
                .build();
    }

    // ═════════════════════════════════════════════════════════════════════
    // markOutOfStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("markOutOfStock()")
    class MarkOutOfStock {

        private ProductVariantMarkOutOfStockCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantMarkOutOfStockCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to markOutOfStock")
        void checksProductAndDelegates() {
            var variant = variantInActive();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.markOutOfStock(context, command);

            verify(productQuery).checkExistsById(productId);
            assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.OUT_OF_STOCK);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsById(productId);

            assertThatThrownBy(() -> service.markOutOfStock(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // discontinue
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("discontinue()")
    class Discontinue {

        private ProductVariantDiscontinueCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantDiscontinueCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to discontinue")
        void checksProductAndDelegates() {
            var variant = variantInActive();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.discontinue(context, command);

            verify(productQuery).checkExistsById(productId);
            assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.DISCONTINUED);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsById(productId);

            assertThatThrownBy(() -> service.discontinue(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // archive
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("archive()")
    class Archive {

        private ProductVariantArchiveCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantArchiveCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to archive")
        void checksProductAndDelegates() {
            var variant = ProductVariantRoot.builder()
                    .id(variantId)
                    .productId(productId)
                    .assignments(List.of(assignment))
                    .barcode(barcode)
                    .sku(sku)
                    .images(List.of())
                    .status(ProductVariantStatus.DISCONTINUED)
                    .build();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.archive(context, command);

            verify(productQuery).checkExistsById(productId);
            assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.ARCHIVED);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsById(productId);

            assertThatThrownBy(() -> service.archive(context, command))
                    .isSameAs(ex);
        }
    }
}
