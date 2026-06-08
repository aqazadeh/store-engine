package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;

@QueryAdapter
public class ProductRejectionReasonQueryOutboundAdapter implements ProductRejectionReasonQueryOutboundPort {
    @Override
    public ProductRejectionReasonRoot fetchById(ProductRejectionReasonId id) {
        return null;
    }

    @Override
    public void checkExistsByIdAndMerchantId(ProductRejectionReasonId id, MerchantId merchantId) {

    }
}
