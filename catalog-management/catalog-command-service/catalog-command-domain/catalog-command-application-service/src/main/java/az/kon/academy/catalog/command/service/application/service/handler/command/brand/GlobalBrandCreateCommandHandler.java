package az.kon.academy.catalog.command.service.application.service.handler.command.brand;

import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandModificationDomainService;
import org.springframework.stereotype.Component;

//FIXME: When security enabled add permission check in here
@Component //FIXME: Maybe change this custom annotation @CommandHandler and we need to brain storm about transaction is starts here or inbound port
public class GlobalBrandCreateCommandHandler implements AbstractCommandHandler<BrandCreateCommand, BrandCreateCommandResult> {
    private final DomainContext domainContext;
    private final BrandManagementDomainService brandManagementDomainService;
//    private final DomainEventPublisher domainEventPublisher; // FIXME: When event system implement use it

    public GlobalBrandCreateCommandHandler(DomainContext domainContext,
                                           BrandManagementDomainService brandManagementDomainService) {
        this.domainContext = domainContext;
        this.brandManagementDomainService = brandManagementDomainService;
    }

    @Override
    public BrandCreateCommandResult handle(BrandCreateCommand command) {
        var brand = this.brandManagementDomainService.createGlobalBrand(domainContext, command);
//        this.domainEventPublisher.publish(brand.getUncommitedEvents); // FIXME: When event system implement use it
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
        var savedBrand = brandCommandPort.save(brand);
        return BrandCreateCommandResult.of(savedBrand);
    }
}
