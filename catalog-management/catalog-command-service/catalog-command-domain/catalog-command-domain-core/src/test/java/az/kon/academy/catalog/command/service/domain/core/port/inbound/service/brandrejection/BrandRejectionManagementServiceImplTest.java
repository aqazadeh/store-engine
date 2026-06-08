package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonSolveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.domain.core.SeDomainContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("BrandRejectionManagementServiceImpl")
class BrandRejectionManagementServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private BrandRejectionReasonQueryOutboundPort rejectionReasonQuery;

    private BrandRejectionManagementServiceImpl service;

    private BrandRejectionReasonId rejectionReasonId;
    private BrandRejectionReasonSolveCommand solveCommand;

    @BeforeEach
    void setUp() {
        service = new BrandRejectionManagementServiceImpl();
        rejectionReasonId = BrandRejectionReasonId.from(UUID.randomUUID());

        solveCommand = BrandRejectionReasonSolveCommand.builder()
                .brandRejectionReasonId(rejectionReasonId)
                .merchantId(MerchantId.from(UUID.randomUUID()))
                .build();

        lenient().when(context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class))
                .thenReturn(rejectionReasonQuery);
    }

    private BrandRejectionReasonRoot freshRejection() {
        return BrandRejectionReasonRoot.initialize(
                BrandRejectionReasonAddCommand.builder()
                        .brandId(BrandId.from(UUID.randomUUID()))
                        .moderatorId(ModeratorId.from(UUID.randomUUID()))
                        .reason("Brand name is too similar to trademark")
                        .build()
        );
    }

    @Test
    @DisplayName("Fetches rejection reason and delegates to markAsSolved")
    void fetchesAndDelegatesToMarkAsSolved() {
        var rejection = freshRejection();
        when(rejectionReasonQuery.fetchByIdAndRowStatusActive(rejectionReasonId))
                .thenReturn(rejection);

        var result = service.solve(context, solveCommand);

        assertThat(result.getSolved()).isTrue();
    }

    @Test
    @DisplayName("Throws when rejection reason is not found")
    void throwsWhenNotFound() {
        when(rejectionReasonQuery.fetchByIdAndRowStatusActive(rejectionReasonId))
                .thenThrow(new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND,
                        List.of(rejectionReasonId.toString())));

        assertThatThrownBy(() -> service.solve(context, solveCommand))
                .isInstanceOf(BrandDomainException.class);
    }
}
