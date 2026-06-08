package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.*;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductVariantDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantValueQueryOutboundPort;
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
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductVariantManagementDomainServiceImpl")
class ProductVariantManagementDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductQueryOutboundPort productQuery;

    @Mock
    private ProductVariantQueryOutboundPort variantQuery;

    @Mock
    private ProductVariantValueQueryOutboundPort variantValueQuery;

    private ProductVariantManagementDomainServiceImpl service;

    private MerchantId merchantId;
    private ProductId productId;
    private ProductVariantId variantId;
    private ProductVariantAssignment assignment;
    private Barcode barcode;
    private ProductVariantSku sku;
    private ProductVariantAddCommand addCommand;
    private String image;

    @BeforeEach
    void setUp() {
        service = new ProductVariantManagementDomainServiceImpl();

        merchantId = MerchantId.from(UUID.randomUUID());
        productId = ProductId.from(UUID.randomUUID());
        variantId = ProductVariantId.random();
        assignment = ProductVariantAssignment.of(VariantKeyId.random(), VariantValueId.random());
        barcode = Barcode.of("1234567890123");
        sku = ProductVariantSku.of("SKU-TEST-001");
        image = "https://cdn.example.com/variant/image.png";

        addCommand = ProductVariantAddCommand.builder()
                .merchantId(merchantId)
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .build();

        when(context.getQueryPort(ProductQueryOutboundPort.class)).thenReturn(productQuery);
        lenient().when(context.getQueryPort(ProductVariantQueryOutboundPort.class)).thenReturn(variantQuery);
        lenient().when(context.getQueryPort(ProductVariantValueQueryOutboundPort.class)).thenReturn(variantValueQuery);
    }

    private ProductVariantRoot variantInDraft() {
        return ProductVariantRoot.builder()
                .id(variantId)
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .images(List.of())
                .status(ProductVariantStatus.DRAFT)
                .build();
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
    // addVariant
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("addVariant()")
    class AddVariant {

        @Test
        @DisplayName("Checks product exists, validates assignments and initializes variant")
        void checksProductAndInitializes() {
            var result = service.addVariant(context, addCommand);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            verify(variantValueQuery).checkAllAssignmentsExist(Map.of(
                    assignment.getVariantKeyId(),
                    List.of(assignment.getVariantValueId())
            ));
            assertThat(result.getRootID()).isNotNull();
            assertThat(result.getProductId()).isEqualTo(productId);
            assertThat(result.getBarcode()).isEqualTo(barcode);
            assertThat(result.getSku()).isEqualTo(sku);
            assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.DRAFT);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.addVariant(context, addCommand))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // removeVariant
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("removeVariant()")
    class RemoveVariant {

        private ProductVariantRemoveCommand removeCommand;

        @BeforeEach
        void setUp() {
            removeCommand = ProductVariantRemoveCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .variantId(variantId)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to remove")
        void checksProductAndRemoves() {
            var variant = variantInDraft();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.removeVariant(context, removeCommand);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Throws when variant is not DRAFT")
        void throwsWhenNotDraft() {
            var variant = variantInActive();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            assertThatThrownBy(() -> service.removeVariant(context, removeCommand))
                    .isInstanceOf(ProductVariantDomainException.class);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.removeVariant(context, removeCommand))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changeBarcode
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changeBarcode()")
    class ChangeBarcode {

        private Barcode newBarcode;
        private ProductVariantChangeBarcodeCommand command;

        @BeforeEach
        void setUp() {
            newBarcode = Barcode.of("9876543210987");
            command = ProductVariantChangeBarcodeCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .barcode(newBarcode)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to changeBarcode")
        void checksProductAndDelegates() {
            var variant = variantInDraft();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.changeBarcode(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getBarcode()).isEqualTo(newBarcode);
        }

        @Test
        @DisplayName("Throws when variant is not DRAFT")
        void throwsWhenNotDraft() {
            var variant = variantInActive();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            assertThatThrownBy(() -> service.changeBarcode(context, command))
                    .isInstanceOf(ProductVariantDomainException.class);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.changeBarcode(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // addImage
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("addImage()")
    class AddImage {

        private ProductVariantAddImageCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantAddImageCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .image(image)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to addImage")
        void checksProductAndDelegates() {
            var variant = variantInDraft();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.addImage(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getImages()).contains(image);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.addImage(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // removeImage
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("removeImage()")
    class RemoveImage {

        private ProductVariantRemoveImageCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantRemoveImageCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .image(image)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to removeImage")
        void checksProductAndDelegates() {
            var variant = ProductVariantRoot.builder()
                    .id(variantId)
                    .productId(productId)
                    .assignments(List.of(assignment))
                    .barcode(barcode)
                    .sku(sku)
                    .images(List.of(image, "other.png"))
                    .status(ProductVariantStatus.DRAFT)
                    .build();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.removeImage(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getImages()).doesNotContain(image);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.removeImage(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // markImagePrimary
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("markImagePrimary()")
    class MarkImagePrimary {

        private ProductVariantMarkImagePrimaryCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantMarkImagePrimaryCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .image(image)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to markImagePrimary")
        void checksProductAndDelegates() {
            var variant = ProductVariantRoot.builder()
                    .id(variantId)
                    .productId(productId)
                    .assignments(List.of(assignment))
                    .barcode(barcode)
                    .sku(sku)
                    .images(List.of("other.png", image))
                    .status(ProductVariantStatus.DRAFT)
                    .build();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.markImagePrimary(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getImages().getFirst()).isEqualTo(image);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.markImagePrimary(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // activate
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("activate()")
    class Activate {

        private ProductVariantActivateCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantActivateCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to activate")
        void checksProductAndDelegates() {
            var variant = variantInDraft();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.activate(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.activate(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // deactivate
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("deactivate()")
    class Deactivate {

        private ProductVariantDeactivateCommand command;

        @BeforeEach
        void setUp() {
            command = ProductVariantDeactivateCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to deactivate")
        void checksProductAndDelegates() {
            var variant = variantInActive();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.deactivate(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.INACTIVE);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.deactivate(context, command))
                    .isSameAs(ex);
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changeSku
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changeSku()")
    class ChangeSku {

        private ProductVariantSku newSku;
        private ProductVariantChangeSkuCommand command;

        @BeforeEach
        void setUp() {
            newSku = ProductVariantSku.of("SKU-NEW-001");
            command = ProductVariantChangeSkuCommand.builder()
                    .merchantId(merchantId)
                    .productId(productId)
                    .productVariantId(variantId)
                    .sku(newSku)
                    .build();
        }

        @Test
        @DisplayName("Checks product, fetches variant and delegates to changeSku")
        void checksProductAndDelegates() {
            var variant = variantInDraft();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            var result = service.changeSku(context, command);

            verify(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);
            assertThat(result.getSku()).isEqualTo(newSku);
        }

        @Test
        @DisplayName("Throws when variant is not DRAFT")
        void throwsWhenNotDraft() {
            var variant = variantInActive();
            when(variantQuery.fetchByIdAndProductIdAndMerchantId(variantId, productId, merchantId))
                    .thenReturn(variant);

            assertThatThrownBy(() -> service.changeSku(context, command))
                    .isInstanceOf(ProductVariantDomainException.class);
        }

        @Test
        @DisplayName("Throws when product does not exist")
        void throwsWhenProductNotFound() {
            var ex = new RuntimeException("product not found");
            doThrow(ex).when(productQuery).checkExistsByIdAndMerchantId(productId, merchantId);

            assertThatThrownBy(() -> service.changeSku(context, command))
                    .isSameAs(ex);
        }
    }
}
