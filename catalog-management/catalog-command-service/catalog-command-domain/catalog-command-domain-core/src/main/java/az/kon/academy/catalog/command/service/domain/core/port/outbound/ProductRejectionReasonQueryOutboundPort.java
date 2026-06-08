package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.domain.core.BaseQueryPort;

public interface ProductRejectionReasonQueryOutboundPort extends BaseQueryPort {


    ProductRejectionReasonRoot fetchById(ProductRejectionReasonId id);

    void checkExistsByIdAndMerchantId(ProductRejectionReasonId id, MerchantId merchantId);
}
