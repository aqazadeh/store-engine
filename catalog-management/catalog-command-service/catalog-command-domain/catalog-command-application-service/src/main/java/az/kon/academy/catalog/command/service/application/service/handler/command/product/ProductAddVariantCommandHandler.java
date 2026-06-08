package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantAddCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general.ProductManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Product.PRODUCT_ADD_VARIANT)
public class ProductAddVariantCommandHandler implements AbstractCommandHandler<ProductVariantAddCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductManagementDomainService productManagementDomainService;

    public ProductAddVariantCommandHandler(SeDomainContext domainContext,
                                           DomainEventPublisher domainEventPublisher,
                                           ProductManagementDomainService productManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productManagementDomainService = productManagementDomainService;
    }

    @Override
    public Void handle(ProductVariantAddCommand command) {
//        var aggregate = this.productManagementDomainService.addVariant(domainContext, command);
//        var port = this.domainContext.getCommandPort(ProductCommandOutboundPort.class);
//        port.save(aggregate);
//        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
