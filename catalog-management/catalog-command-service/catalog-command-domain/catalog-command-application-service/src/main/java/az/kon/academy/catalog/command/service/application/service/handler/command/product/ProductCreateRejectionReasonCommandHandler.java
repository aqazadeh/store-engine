package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductRejectionReasonCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.ProductDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Product.PRODUCT_CREATE_REJECTION_REASON)
public class ProductCreateRejectionReasonCommandHandler implements AbstractCommandHandler<ProductCreateRejectionReasonCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductDomainService productDomainService;

    public ProductCreateRejectionReasonCommandHandler(SeDomainContext domainContext,
                                                      DomainEventPublisher domainEventPublisher,
                                                      ProductDomainService productDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productDomainService = productDomainService;
    }

    @Override
    public Void handle(ProductCreateRejectionReasonCommand command) {
        var aggregate = this.productDomainService.createRejectionReason(domainContext, command);
        var port = this.domainContext.getCommandPort(ProductRejectionReasonCommandPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
