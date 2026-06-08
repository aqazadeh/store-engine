package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandMoveToDraftCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandSentToApprovalCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.rules.BrandDomainRules;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandPath;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandStatus;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BrandManagementDomainServiceImpl")
class BrandManagementDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private BrandQueryOutboundPort brandQuery;

    @Mock
    private BrandRejectionReasonQueryOutboundPort rejectionReasonQuery;

    private BrandManagementDomainServiceImpl service;

    private MerchantId owner;
    private BrandId brandId;
    private BrandName name;
    private BrandDescription description;
    private BrandPath path;
    private BrandCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        service = new BrandManagementDomainServiceImpl();

        owner = MerchantId.from(UUID.randomUUID());
        brandId = BrandId.from(UUID.randomUUID());
        name = new BrandName("Nike Brand");
        description = new BrandDescription("A well-known global sports brand");
        path = new BrandPath("nike-brand");

        createCommand = BrandCreateCommand.builder()
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .build();

        when(context.getQueryPort(BrandQueryOutboundPort.class)).thenReturn(brandQuery);
        lenient().when(context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class)).thenReturn(rejectionReasonQuery);
    }

    private BrandRoot brandInDraftState() {
        return BrandRoot.builder()
                .id(brandId)
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.DRAFT)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // create
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("create()")
    class Create {

        @Nested
        @DisplayName("Happy path")
        class HappyPath {

            @Test
            @DisplayName("Creates brand when merchant is under limit")
            void createsBrandWhenUnderLimit() {
                when(brandQuery.fetchCountByMerchantId(owner)).thenReturn(0);

                var result = service.create(context, createCommand);

                assertThat(result.getRootID()).isNotNull();
                assertThat(result.getOwner()).isEqualTo(owner);
                assertThat(result.getName()).isEqualTo(name);
                assertThat(result.getStatus()).isEqualTo(BrandStatus.DRAFT);
            }

            @Test
            @DisplayName("Creates brand when merchant is at limit boundary")
            void createsBrandWhenAtLimitBoundary() {
                when(brandQuery.fetchCountByMerchantId(owner))
                        .thenReturn(BrandDomainRules.MAX_BRANDS_PER_MERCHANT - 1);

                var result = service.create(context, createCommand);

                assertThat(result.getRootID()).isNotNull();
                assertThat(result.getOwner()).isEqualTo(owner);
            }

            @Test
            @DisplayName("Calls checkExistsByName with command name")
            void callsCheckExistsByName() {
                when(brandQuery.fetchCountByMerchantId(owner)).thenReturn(1);

                service.create(context, createCommand);

                verify(brandQuery).checkExistsByName(name);
            }
        }

        @Nested
        @DisplayName("Guard: too many brands")
        class TooManyBrands {

            @Test
            @DisplayName("Throws BrandDomainException when merchant is at max limit")
            void throwsWhenAtMaxLimit() {
                when(brandQuery.fetchCountByMerchantId(owner))
                        .thenReturn(BrandDomainRules.MAX_BRANDS_PER_MERCHANT);

                assertThatThrownBy(() -> service.create(context, createCommand))
                        .isInstanceOf(BrandDomainException.class)
                        .matches(ex -> ((BrandDomainException) ex).getCode()
                                .equals(BrandDomainErrorCodes.TOO_MANY_BRANDS_FOR_MERCHANT));
            }

            @Test
            @DisplayName("Throws BrandDomainException when merchant exceeds max limit")
            void throwsWhenExceedsMaxLimit() {
                when(brandQuery.fetchCountByMerchantId(owner))
                        .thenReturn(BrandDomainRules.MAX_BRANDS_PER_MERCHANT + 1);

                assertThatThrownBy(() -> service.create(context, createCommand))
                        .isInstanceOf(BrandDomainException.class)
                        .matches(ex -> ((BrandDomainException) ex).getCode()
                                .equals(BrandDomainErrorCodes.TOO_MANY_BRANDS_FOR_MERCHANT));
            }
        }

        @Nested
        @DisplayName("Guard: name already exists")
        class NameAlreadyExists {

            @Test
            @DisplayName("Throws when checkExistsByName throws")
            void throwsWhenNameExists() {
                when(brandQuery.fetchCountByMerchantId(owner)).thenReturn(0);
                doThrow(new BrandDomainException(BrandDomainErrorCodes.NAME_ALREADY_EXISTS, List.of(name.value())))
                        .when(brandQuery).checkExistsByName(any());

                assertThatThrownBy(() -> service.create(context, createCommand))
                        .isInstanceOf(BrandDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeInformation
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private BrandName newName;
        private BrandDescription newDescription;
        private BrandChangeInformationCommand changeCommand;

        @BeforeEach
        void setUp() {
            newName = new BrandName("Adidas Brand");
            newDescription = new BrandDescription("A well-known German sports brand");
            changeCommand = BrandChangeInformationCommand.builder()
                    .brandId(brandId)
                    .owner(owner)
                    .name(newName)
                    .description(newDescription)
                    .build();
        }

        @Test
        @DisplayName("Fetches brand and delegates to aggregate changeInformation")
        void fetchesAndDelegates() {
            var brand = brandInDraftState();
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner)).thenReturn(brand);

            var result = service.changeInformation(context, changeCommand);

            assertThat(result.getName()).isEqualTo(newName);
            assertThat(result.getDescription()).isEqualTo(newDescription);
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.changeInformation(context, changeCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeImage
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeImage()")
    class ChangeImage {

        private String newImage;
        private BrandChangeImageCommand imageCommand;

        @BeforeEach
        void setUp() {
            newImage = "https://cdn.example.com/brands/nike-logo.png";
            imageCommand = BrandChangeImageCommand.builder()
                    .brandId(brandId)
                    .owner(owner)
                    .image(newImage)
                    .build();
        }

        @Test
        @DisplayName("Fetches brand and delegates to aggregate changeImage")
        void fetchesAndDelegates() {
            var brand = brandInDraftState();
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner)).thenReturn(brand);

            var result = service.changeImage(context, imageCommand);

            assertThat(result.getImage()).isEqualTo(newImage);
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.changeImage(context, imageCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // sentToApproval
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("sentToApproval()")
    class SentToApproval {

        private BrandSentToApprovalCommand sentToApprovalCommand;

        @BeforeEach
        void setUp() {
            sentToApprovalCommand = BrandSentToApprovalCommand.builder()
                    .brandId(brandId)
                    .owner(owner)
                    .build();
        }

        @Test
        @DisplayName("Fetches brand and delegates to aggregate sentToApproval")
        void fetchesAndDelegates() {
            var brand = brandInDraftState();
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner)).thenReturn(brand);
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(false);

            var result = service.sentToApproval(context, sentToApprovalCommand);

            assertThat(result.getStatus()).isEqualTo(BrandStatus.SENT_TO_APPROVAL);
        }

        @Test
        @DisplayName("Throws when brand has unsolved rejection reasons")
        void throwsWhenHasUnsolvedRejectionReasons() {
            var brand = brandInDraftState();
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner)).thenReturn(brand);
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(true);

            assertThatThrownBy(() -> service.sentToApproval(context, sentToApprovalCommand))
                    .isInstanceOf(BrandDomainException.class)
                    .matches(ex -> ((BrandDomainException) ex).getCode()
                            .equals(BrandDomainErrorCodes.HAS_UNSOLVED_REASON));
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.sentToApproval(context, sentToApprovalCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // moveToDraft
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("moveToDraft()")
    class MoveToDraft {

        private BrandMoveToDraftCommand moveToDraftCommand;

        @BeforeEach
        void setUp() {
            moveToDraftCommand = BrandMoveToDraftCommand.builder()
                    .brandId(brandId)
                    .owner(owner)
                    .build();
        }

        @Test
        @DisplayName("Fetches brand and delegates to aggregate moveToDraft")
        void fetchesAndDelegates() {
            var brand = BrandRoot.builder()
                    .id(brandId)
                    .owner(owner)
                    .name(name)
                    .description(description)
                    .path(path)
                    .isGlobal(Boolean.FALSE)
                    .status(BrandStatus.SENT_TO_APPROVAL)
                    .build();
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner)).thenReturn(brand);

            var result = service.moveToDraft(context, moveToDraftCommand);

            assertThat(result.getStatus()).isEqualTo(BrandStatus.DRAFT);
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchByIdAndMerchantId(brandId, owner))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.moveToDraft(context, moveToDraftCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }
}
