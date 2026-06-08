package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductStockCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock.ProductStockModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Product.PRODUCT_STOCK_DECREASE)
public class ProductStockDecreaseCommandHandler implements AbstractCommandHandler<ProductStockDecreaseCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductStockModerationDomainService productStockModerationDomainService;

    public ProductStockDecreaseCommandHandler(SeDomainContext domainContext,
                                              DomainEventPublisher domainEventPublisher,
                                              ProductStockModerationDomainService productStockModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productStockModerationDomainService = productStockModerationDomainService;
    }

    @Override
    public Void handle(ProductStockDecreaseCommand command) {
        var aggregate = this.productStockModerationDomainService.decreaseStock(domainContext, command);
        var port = this.domainContext.getCommandPort(ProductStockCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
