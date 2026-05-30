package az.kon.academy.catalog.command.service.application.service.handler.command.category;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCategoryCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category.ProductCategoryDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.ProductCategory.PRODUCT_CATEGORY_CREATE)
public class ProductCategoryCreateCommandHandler implements AbstractCommandHandler<ProductCategoryCreateCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductCategoryDomainService productCategoryDomainService;

    public ProductCategoryCreateCommandHandler(SeDomainContext domainContext,
                                               DomainEventPublisher domainEventPublisher,
                                               ProductCategoryDomainService productCategoryDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productCategoryDomainService = productCategoryDomainService;
    }

    @Override
    public Void handle(ProductCategoryCreateCommand command) {
        var productCategory = this.productCategoryDomainService.createCategory(domainContext, command);
        var productCategoryPort = this.domainContext.getCommandPort(ProductCategoryCommandOutboundPort.class);
        var savedProductCategory = productCategoryPort.save(productCategory);
        this.domainEventPublisher.publish(productCategory.getUncommittedEvents());
        return null;
    }
}
