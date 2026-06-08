package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandApproveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeGlobalCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandMoveToInReviewCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandRejectCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.MerchantQueryOutboundPort;
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
@DisplayName("BrandModerationDomainServiceImpl")
class BrandModerationDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private BrandQueryOutboundPort brandQuery;

    @Mock
    private BrandRejectionReasonQueryOutboundPort rejectionReasonQuery;

    @Mock
    private MerchantQueryOutboundPort merchantQuery;

    private BrandModerationDomainServiceImpl service;

    private MerchantId owner;
    private BrandId brandId;
    private BrandName name;
    private BrandDescription description;
    private BrandPath path;
    private BrandCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        service = new BrandModerationDomainServiceImpl();

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

        lenient().when(context.getQueryPort(BrandQueryOutboundPort.class)).thenReturn(brandQuery);
        lenient().when(context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class)).thenReturn(rejectionReasonQuery);
        lenient().when(context.getQueryPort(MerchantQueryOutboundPort.class)).thenReturn(merchantQuery);
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

    private BrandRoot brandInSentToApprovalState() {
        return BrandRoot.builder()
                .id(brandId)
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.SENT_TO_APPROVAL)
                .build();
    }

    private BrandRoot brandInInReviewState() {
        return BrandRoot.builder()
                .id(brandId)
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.IN_REVIEW)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // create
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("Checks merchant is active and creates global brand")
        void checksMerchantAndCreates() {
            var result = service.create(context, createCommand);

            verify(merchantQuery).checkMerchantIsActive(owner);
            verify(brandQuery).checkExistsByName(name);
            assertThat(result.getRootID()).isNotNull();
            assertThat(result.getIsGlobal()).isTrue();
            assertThat(result.getStatus()).isEqualTo(BrandStatus.APPROVED);
        }

        @Nested
        @DisplayName("Guard: name already exists")
        class NameAlreadyExists {

            @Test
            @DisplayName("Throws when checkExistsByName throws")
            void throwsWhenNameExists() {
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
        @DisplayName("Fetches global brand and delegates to aggregate changeInformation")
        void fetchesAndDelegates() {
            var brand = brandInDraftState();
            when(brandQuery.fetchByIdAndIsGlobalTrue(brandId)).thenReturn(brand);

            var result = service.changeInformation(context, changeCommand);

            assertThat(result.getName()).isEqualTo(newName);
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchByIdAndIsGlobalTrue(brandId))
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
        @DisplayName("Fetches global brand and delegates to aggregate changeImage")
        void fetchesAndDelegates() {
            var brand = brandInDraftState();
            when(brandQuery.fetchByIdAndIsGlobalTrue(brandId)).thenReturn(brand);

            var result = service.changeImage(context, imageCommand);

            assertThat(result.getImage()).isEqualTo(newImage);
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchByIdAndIsGlobalTrue(brandId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.changeImage(context, imageCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeOwner
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeOwner()")
    class ChangeOwner {

        private MerchantId newOwner;
        private BrandChangeOwnerCommand ownerCommand;

        @BeforeEach
        void setUp() {
            newOwner = MerchantId.from(UUID.randomUUID());
            ownerCommand = BrandChangeOwnerCommand.builder()
                    .brandId(brandId)
                    .owner(newOwner)
                    .build();
        }

        @Test
        @DisplayName("Fetches global brand and delegates to aggregate changeOwner")
        void fetchesAndDelegates() {
            var brand = brandInDraftState();
            when(brandQuery.fetchByIdAndIsGlobalTrue(brandId)).thenReturn(brand);

            var result = service.changeOwner(context, ownerCommand);

            assertThat(result.getOwner()).isEqualTo(newOwner);
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchByIdAndIsGlobalTrue(brandId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.changeOwner(context, ownerCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // approve
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("approve()")
    class Approve {

        private BrandApproveCommand approveCommand;

        @BeforeEach
        void setUp() {
            approveCommand = BrandApproveCommand.builder()
                    .brandId(brandId)
                    .build();
        }

        @Test
        @DisplayName("Approves brand when there are no unsolved rejection reasons")
        void approvesWhenNoUnsolvedReasons() {
            var brand = brandInInReviewState();
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(false);
            when(brandQuery.fetchById(brandId)).thenReturn(brand);

            var result = service.approve(context, approveCommand);

            assertThat(result.getStatus()).isEqualTo(BrandStatus.APPROVED);
        }

        @Test
        @DisplayName("Throws when there are unsolved rejection reasons")
        void throwsWhenHasUnsolvedReasons() {
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(true);

            assertThatThrownBy(() -> service.approve(context, approveCommand))
                    .isInstanceOf(BrandDomainException.class)
                    .matches(ex -> ((BrandDomainException) ex).getCode()
                            .equals(BrandDomainErrorCodes.HAS_UNSOLVED_REASON));
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(false);
            when(brandQuery.fetchById(brandId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.approve(context, approveCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // reject
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("reject()")
    class Reject {

        private BrandRejectCommand rejectCommand;

        @BeforeEach
        void setUp() {
            rejectCommand = BrandRejectCommand.builder()
                    .brandId(brandId)
                    .build();
        }

        @Test
        @DisplayName("Rejects brand when there is at least one unsolved rejection reason")
        void rejectsWhenHasUnsolvedReasons() {
            var brand = brandInInReviewState();
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(true);
            when(brandQuery.fetchById(brandId)).thenReturn(brand);

            var result = service.reject(context, rejectCommand);

            assertThat(result.getStatus()).isEqualTo(BrandStatus.REJECTED);
        }

        @Test
        @DisplayName("Throws when there are no unsolved rejection reasons")
        void throwsWhenNoUnsolvedReasons() {
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(false);

            assertThatThrownBy(() -> service.reject(context, rejectCommand))
                    .isInstanceOf(BrandDomainException.class)
                    .matches(ex -> ((BrandDomainException) ex).getCode()
                            .equals(BrandDomainErrorCodes.AT_LEAST_ONE_REJECTION_REASON_REQUIRED));
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(rejectionReasonQuery.existsByBrandIdAndNotSolved(brandId)).thenReturn(true);
            when(brandQuery.fetchById(brandId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.reject(context, rejectCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeToGlobal
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeToGlobal()")
    class ChangeToGlobal {

        private BrandChangeGlobalCommand globalCommand;

        @BeforeEach
        void setUp() {
            globalCommand = BrandChangeGlobalCommand.builder()
                    .brandId(brandId)
                    .build();
        }

        @Test
        @DisplayName("Fetches brand and delegates to aggregate changeGlobal")
        void fetchesAndDelegates() {
            var brand = brandInDraftState();
            when(brandQuery.fetchById(brandId)).thenReturn(brand);

            var result = service.changeToGlobal(context, globalCommand);

            assertThat(result.getIsGlobal()).isTrue();
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchById(brandId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.changeToGlobal(context, globalCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // moveToInReview
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("moveToInReview()")
    class MoveToInReview {

        private BrandMoveToInReviewCommand moveToInReviewCommand;

        @BeforeEach
        void setUp() {
            moveToInReviewCommand = BrandMoveToInReviewCommand.builder()
                    .brandId(brandId)
                    .build();
        }

        @Test
        @DisplayName("Fetches brand and delegates to aggregate moveToInReview")
        void fetchesAndDelegates() {
            var brand = brandInSentToApprovalState();
            when(brandQuery.fetchById(brandId)).thenReturn(brand);

            var result = service.moveToInReview(context, moveToInReviewCommand);

            assertThat(result.getStatus()).isEqualTo(BrandStatus.IN_REVIEW);
        }

        @Test
        @DisplayName("Throws when brand is not found")
        void throwsWhenBrandNotFound() {
            when(brandQuery.fetchById(brandId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));

            assertThatThrownBy(() -> service.moveToInReview(context, moveToInReviewCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }
}
