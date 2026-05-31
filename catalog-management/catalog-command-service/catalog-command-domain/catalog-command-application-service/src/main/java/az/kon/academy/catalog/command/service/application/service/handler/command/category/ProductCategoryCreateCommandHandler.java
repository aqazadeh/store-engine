package az.kon.academy.catalog.command.service.application.service.handler.command.category;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCategoryCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category.ProductCategoryModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.ProductCategory.PRODUCT_CATEGORY_MODERATION_CREATE;
import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR;

@CommandHandler(roles = ROLE_DOMAIN_MODERATOR, permissions = PRODUCT_CATEGORY_MODERATION_CREATE)
public class ProductCategoryCreateCommandHandler implements AbstractCommandHandler<ProductCategoryCreateCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductCategoryModerationDomainService productCategoryModerationDomainService;

    public ProductCategoryCreateCommandHandler(SeDomainContext domainContext,
                                               DomainEventPublisher domainEventPublisher,
                                               ProductCategoryModerationDomainService productCategoryModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productCategoryModerationDomainService = productCategoryModerationDomainService;
    }

    @Override
    public Void handle(ProductCategoryCreateCommand command) {
        var productCategory = this.productCategoryModerationDomainService.createCategory(domainContext, command);
        var productCategoryPort = this.domainContext.getCommandPort(ProductCategoryCommandOutboundPort.class);
        var savedProductCategory = productCategoryPort.save(productCategory);
        this.domainEventPublisher.publish(productCategory.getUncommittedEvents());
        return null;
    }
}
