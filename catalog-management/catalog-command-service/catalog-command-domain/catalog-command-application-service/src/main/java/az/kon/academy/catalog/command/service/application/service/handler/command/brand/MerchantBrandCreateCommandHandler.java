package az.kon.academy.catalog.command.service.application.service.handler.command.brand;

import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandModificationDomainService;

//FIXME: When security implemented add permission check in here
public class MerchantBrandCreateCommandHandler implements AbstractCommandHandler<BrandCreateCommand, BrandCreateCommandResult> {
    private final DomainContext domainContext;
    private final BrandModificationDomainService brandModificationDomainService;
//    private final DomainEventPublisher domainEventPublisher; // FIXME: When event system implement use it

    public MerchantBrandCreateCommandHandler(DomainContext domainContext,
                                             BrandModificationDomainService brandModificationDomainService) {
        this.domainContext = domainContext;
        this.brandModificationDomainService = brandModificationDomainService;
    }

    @Override
    public BrandCreateCommandResult handle(BrandCreateCommand command) {
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
//        this.domainEventPublisher.publish(brand.getUncommitedEvents); // FIXME: When event system implement use it maybe add command_id to event for correlation_id
        var brand = this.brandModificationDomainService.createBrand(domainContext, command);
        var savedBrand = brandCommandPort.save(brand);
        return BrandCreateCommandResult.of(savedBrand);
    }
}
