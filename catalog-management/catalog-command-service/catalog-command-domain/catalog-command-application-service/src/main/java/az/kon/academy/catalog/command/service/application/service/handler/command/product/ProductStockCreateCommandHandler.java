package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductStockCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.ProductDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Product.PRODUCT_STOCK_CREATE)
public class ProductStockCreateCommandHandler implements AbstractCommandHandler<ProductStockCreateCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductDomainService productDomainService;

    public ProductStockCreateCommandHandler(SeDomainContext domainContext,
                                            DomainEventPublisher domainEventPublisher,
                                            ProductDomainService productDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productDomainService = productDomainService;
    }

    @Override
    public Void handle(ProductStockCreateCommand command) {
        var aggregate = this.productDomainService.createStock(domainContext, command);
        var port = this.domainContext.getCommandPort(ProductStockCommandPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
