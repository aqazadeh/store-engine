package az.kon.academy.catalog.command.service.application.service.handler.event;

import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandRejectCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonAddedEvent;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.BaseEventHandler;
import az.kon.academy.event.handler.DomainEventPublisher;
import az.kon.academy.event.handler.autoconfiguration.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Qualifier;

@EventHandler
public class BrandRejectionReasonCreatedEventHandler implements BaseEventHandler<BrandRejectionReasonAddedEvent> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandModerationDomainService brandModerationDomainService;

    public BrandRejectionReasonCreatedEventHandler(SeDomainContext domainContext,
                                                   DomainEventPublisher domainEventPublisher,
                                                   BrandModerationDomainService brandModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandModerationDomainService = brandModerationDomainService;
    }

    @Override
    public void handle(BrandRejectionReasonAddedEvent event) {
        var command = BrandRejectCommand.builder()
                .brandId(BrandId.from(event.getBrandId()))
                .build();

        var brand = this.brandModerationDomainService.reject(domainContext, command);
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
        var savedBrand = brandCommandPort.save(brand);

        this.domainEventPublisher.publish(brand.getUncommittedEvents());
    }
}
