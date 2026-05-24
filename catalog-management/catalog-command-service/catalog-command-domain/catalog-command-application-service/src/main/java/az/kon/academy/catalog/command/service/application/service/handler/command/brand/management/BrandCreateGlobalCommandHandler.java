package az.kon.academy.catalog.command.service.application.service.handler.command.brand.management;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateForGlobalCommand;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Qualifier;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Brand.BRAND_MANAGEMENT_CREATE)
public class BrandCreateGlobalCommandHandler implements AbstractCommandHandler<BrandCreateForGlobalCommand, BrandCreateCommandResult> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandManagementDomainService brandManagementDomainService;

    public BrandCreateGlobalCommandHandler(SeDomainContext domainContext,
                                           DomainEventPublisher domainEventPublisher,
                                           @Qualifier("brandManagementDomainService") BrandManagementDomainService brandManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandManagementDomainService = brandManagementDomainService;
    }

    @Override
    public BrandCreateCommandResult handle(BrandCreateForGlobalCommand command) {

        var brand = this.brandManagementDomainService.createGlobalBrand(domainContext, command);
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
        var savedBrand = brandCommandPort.save(brand);

        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return BrandCreateCommandResult.of(savedBrand);
    }
}
