package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.Optional;

public interface ProductRejectionReasonQueryOutboundPort extends BaseQueryPort {

    Optional<ProductRejectionReasonRoot> findByIdRowStatusActive(ProductRejectionReasonId id);

    ProductRejectionReasonRoot fetchByIdAndRowStatusActive(ProductRejectionReasonId id);

    void checkExistsByIdAndMerchantId(ProductRejectionReasonId id, MerchantId merchantId);
}
