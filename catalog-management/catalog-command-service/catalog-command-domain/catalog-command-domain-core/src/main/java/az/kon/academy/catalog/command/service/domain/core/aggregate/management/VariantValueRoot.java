package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValue;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.event.management.variant.VariantValueCreatedEvent;
import az.kon.academy.catalog.event.management.variant.VariantValueNameChangedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class VariantValueRoot extends AggregateRoot<VariantValueRoot, VariantValueId> {

    @Getter private VariantKeyId keyId;
    @Getter private VariantValue name;

    public static VariantValueRoot initialize(VariantValueCreateCommand command) {
        var variantValue = VariantValueRoot.builder()
                .id(VariantValueId.random())
                .keyId(command.getKeyId())
                .name(command.getName())
                .build();

        var event = VariantValueCreatedEvent.of(
                variantValue.getRootID().value().toString(),
                variantValue.getModificationTs().toOffsetDateTime(),
                variantValue.getKeyId().value(),
                variantValue.getName().value()
        );

        variantValue.addEvent(event);
        return variantValue;
    }

    public VariantValueRoot changeName(VariantValueChangeNameCommand command) {
        var variantValue = this.toBuilder()
                .name(command.getName())
                .modificationTs(SeDateTime.now())
                .build();

        var event = VariantValueNameChangedEvent.of(
                variantValue.getRootID().value().toString(),
                variantValue.getModificationTs().toOffsetDateTime(),
                variantValue.getName().value()
        );

        variantValue.addEvent(event);
        return variantValue;
    }
}