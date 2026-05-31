package az.kon.academy.catalog.command.service.application.service.handler.command.specification;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductSpecificationCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationDeleteCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.ProductSpecification.PRODUCT_SPECIFICATION_MODERATION_DELETE;
import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR;

@CommandHandler(roles = ROLE_DOMAIN_MODERATOR, permissions = PRODUCT_SPECIFICATION_MODERATION_DELETE)
public class ProductSpecificationDeleteCommandHandler implements AbstractCommandHandler<ProductSpecificationDeleteCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductSpecificationModerationDomainService productSpecificationModerationDomainService;

    public ProductSpecificationDeleteCommandHandler(SeDomainContext domainContext,
                                                    DomainEventPublisher domainEventPublisher,
                                                    ProductSpecificationModerationDomainService productSpecificationModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productSpecificationModerationDomainService = productSpecificationModerationDomainService;
    }

    @Override
    public Void handle(ProductSpecificationDeleteCommand command) {
        var productSpecification = this.productSpecificationModerationDomainService.delete(domainContext, command);
        var productSpecificationPort = this.domainContext.getCommandPort(ProductSpecificationCommandOutboundPort.class);
        var savedProductSpecification = productSpecificationPort.save(productSpecification);
        this.domainEventPublisher.publish(productSpecification.getUncommittedEvents());
        return null;
    }
}
