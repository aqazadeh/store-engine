package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.ProductDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Product.PRODUCT_CHANGE_INFORMATION)
public class ProductChangeInformationCommandHandler implements AbstractCommandHandler<ProductChangeInformationCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductDomainService productDomainService;

    public ProductChangeInformationCommandHandler(SeDomainContext domainContext,
                                                  DomainEventPublisher domainEventPublisher,
                                                  ProductDomainService productDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productDomainService = productDomainService;
    }

    @Override
    public Void handle(ProductChangeInformationCommand command) {
        var aggregate = this.productDomainService.changeInformation(domainContext, command);
        var port = this.domainContext.getCommandPort(ProductCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
