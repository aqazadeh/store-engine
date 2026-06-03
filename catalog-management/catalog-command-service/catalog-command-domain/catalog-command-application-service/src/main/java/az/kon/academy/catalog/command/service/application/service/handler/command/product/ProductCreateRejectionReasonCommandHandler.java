package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductRejectionReasonCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general.ProductManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection.ProductRejectionModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Product.PRODUCT_CREATE_REJECTION_REASON)
public class ProductCreateRejectionReasonCommandHandler implements AbstractCommandHandler<ProductCreateRejectionReasonCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductRejectionModerationDomainService productRejectionModerationDomainService;

    public ProductCreateRejectionReasonCommandHandler(SeDomainContext domainContext,
                                                      DomainEventPublisher domainEventPublisher,
                                                      ProductRejectionModerationDomainService productRejectionModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productRejectionModerationDomainService = productRejectionModerationDomainService;
    }

    @Override
    public Void handle(ProductCreateRejectionReasonCommand command) {
        var aggregate = this.productRejectionModerationDomainService.createRejectionReason(domainContext, command);
        var port = this.domainContext.getCommandPort(ProductRejectionReasonCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
