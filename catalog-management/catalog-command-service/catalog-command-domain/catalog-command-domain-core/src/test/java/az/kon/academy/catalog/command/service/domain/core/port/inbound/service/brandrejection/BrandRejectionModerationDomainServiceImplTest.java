package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BrandRejectionModerationDomainServiceImpl")
class BrandRejectionModerationDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private BrandRejectionReasonQueryOutboundPort rejectionReasonQuery;

    private BrandRejectionModerationDomainServiceImpl service;

    private BrandId brandId;
    private ModeratorId moderatorId;
    private String reason;
    private BrandRejectionReasonId rejectionReasonId;
    private BrandRejectionReasonAddCommand addCommand;

    @BeforeEach
    void setUp() {
        service = new BrandRejectionModerationDomainServiceImpl();

        brandId = BrandId.from(UUID.randomUUID());
        moderatorId = ModeratorId.from(UUID.randomUUID());
        reason = "Brand name is too similar to trademark";
        rejectionReasonId = BrandRejectionReasonId.from(UUID.randomUUID());

        addCommand = BrandRejectionReasonAddCommand.builder()
                .brandId(brandId)
                .moderatorId(moderatorId)
                .reason(reason)
                .build();

        lenient().when(context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class))
                .thenReturn(rejectionReasonQuery);
    }

    private BrandRejectionReasonRoot freshRejection() {
        return BrandRejectionReasonRoot.initialize(addCommand);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // add
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("add()")
    class Add {

        @Test
        @DisplayName("Creates rejection reason via initialize")
        void createsRejectionReason() {
            var result = service.add(context, addCommand);

            assertThat(result.getRootID()).isNotNull();
            assertThat(result.getBrandId()).isEqualTo(brandId);
            assertThat(result.getReason()).isEqualTo(reason);
            assertThat(result.getSolved()).isFalse();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // remove
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("remove()")
    class Remove {

        private BrandRejectionReasonRemoveCommand removeCommand;

        @BeforeEach
        void setUp() {
            removeCommand = BrandRejectionReasonRemoveCommand.builder()
                    .brandRejectionReasonId(rejectionReasonId)
                    .moderatorId(ModeratorId.from(UUID.randomUUID()))
                    .build();
        }

        @Test
        @DisplayName("Fetches and delegates to remove, row status becomes DELETED")
        void fetchesAndDelegatesToRemove() {
            var rejection = freshRejection();
            when(rejectionReasonQuery.fetchByIdAndRowStatusActive(rejectionReasonId))
                    .thenReturn(rejection);

            var result = service.remove(context, removeCommand);

            assertThat(result.getRowStatus()).isEqualTo(RowStatus.DELETED);
        }

        @Test
        @DisplayName("Throws when rejection reason is not found")
        void throwsWhenNotFound() {
            when(rejectionReasonQuery.fetchByIdAndRowStatusActive(rejectionReasonId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(rejectionReasonId.toString())));

            assertThatThrownBy(() -> service.remove(context, removeCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeReason
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeReason()")
    class ChangeReason {

        private String newReason;
        private BrandRejectionReasonChangeReasonCommand changeCommand;

        @BeforeEach
        void setUp() {
            newReason = "Brand logo infringes on copyright";
            changeCommand = BrandRejectionReasonChangeReasonCommand.builder()
                    .brandRejectionReasonId(rejectionReasonId)
                    .reason(newReason)
                    .moderatorId(ModeratorId.from(UUID.randomUUID()))
                    .build();
        }

        @Test
        @DisplayName("Fetches and delegates to changeReason")
        void fetchesAndDelegatesToChangeReason() {
            var rejection = freshRejection();
            when(rejectionReasonQuery.fetchByIdAndRowStatusActive(rejectionReasonId))
                    .thenReturn(rejection);

            var result = service.changeReason(context, changeCommand);

            assertThat(result.getReason()).isEqualTo(newReason);
        }

        @Test
        @DisplayName("Throws when rejection reason is not found")
        void throwsWhenNotFound() {
            when(rejectionReasonQuery.fetchByIdAndRowStatusActive(rejectionReasonId))
                    .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(rejectionReasonId.toString())));

            assertThatThrownBy(() -> service.changeReason(context, changeCommand))
                    .isInstanceOf(BrandDomainException.class);
        }
    }
}
