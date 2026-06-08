package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductModerationDomainServiceImpl implements ProductModerationDomainService {

    @Override
    public ProductRoot approve(SeDomainContext context, ProductApproveCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.approve();
    }

    @Override
    public ProductRoot reject(SeDomainContext context, ProductRejectCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.reject();
    }

    @Override
    public ProductRoot moveToInReview(SeDomainContext context, ProductMoveToInReviewCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.moveToInReview();
    }
}
