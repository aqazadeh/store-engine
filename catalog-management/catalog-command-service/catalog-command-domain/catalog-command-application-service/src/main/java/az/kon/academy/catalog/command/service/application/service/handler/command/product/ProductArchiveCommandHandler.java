package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductArchiveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general.ProductModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Product.PRODUCT_ARCHIVE)
public class ProductArchiveCommandHandler implements AbstractCommandHandler<ProductArchiveCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductModerationDomainService productModerationDomainService;

    public ProductArchiveCommandHandler(SeDomainContext domainContext,
                                        DomainEventPublisher domainEventPublisher,
                                        ProductModerationDomainService productModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productModerationDomainService = productModerationDomainService;
    }

    @Override
    public Void handle(ProductArchiveCommand command) {
        var aggregate = this.productModerationDomainService.archive(domainContext, command);
        var port = this.domainContext.getCommandPort(ProductCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
