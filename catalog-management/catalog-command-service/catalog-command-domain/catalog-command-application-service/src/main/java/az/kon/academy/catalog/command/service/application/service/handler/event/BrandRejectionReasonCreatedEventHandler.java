package az.kon.academy.catalog.command.service.application.service.handler.event;

import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandRejectCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.event.management.rejection.BrandRejectionReasonCreatedEvent;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.BaseEventHandler;
import az.kon.academy.event.handler.DomainEventPublisher;
import az.kon.academy.event.handler.autoconfiguration.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Qualifier;

@EventHandler
public class BrandRejectionReasonCreatedEventHandler implements BaseEventHandler<BrandRejectionReasonCreatedEvent> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandManagementDomainService brandManagementDomainService;

    public BrandRejectionReasonCreatedEventHandler(SeDomainContext domainContext,
                                                   DomainEventPublisher domainEventPublisher,
                                                   @Qualifier("brandManagementDomainService") BrandManagementDomainService brandManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandManagementDomainService = brandManagementDomainService;
    }

    @Override
    public void handle(BrandRejectionReasonCreatedEvent event) {
        var command = BrandRejectCommand.builder()
                .brandId(BrandId.from(event.getBrandId()))
                .build();

        var brand = this.brandManagementDomainService.reject(domainContext, command);
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
        var savedBrand = brandCommandPort.save(brand);

        this.domainEventPublisher.publish(brand.getUncommittedEvents());
    }
}
