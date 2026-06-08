package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductStockCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock.ProductStockManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Product.PRODUCT_STOCK_INCREASE)
public class ProductStockIncreaseCommandHandler implements AbstractCommandHandler<ProductStockIncreaseCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductStockManagementDomainService productStockManagementDomainService;

    public ProductStockIncreaseCommandHandler(SeDomainContext domainContext,
                                              DomainEventPublisher domainEventPublisher,
                                              ProductStockManagementDomainService productStockManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productStockManagementDomainService = productStockManagementDomainService;
    }

    @Override
    public Void handle(ProductStockIncreaseCommand command) {
        var aggregate = this.productStockManagementDomainService.increaseStock(domainContext, command);
        var port = this.domainContext.getCommandPort(ProductStockCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
