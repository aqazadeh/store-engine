package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyChangeDescriptionCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import az.kon.academy.catalog.event.management.variant.VariantKeyCreatedEvent;
import az.kon.academy.catalog.event.management.variant.VariantKeyDescriptionChangedEvent;
import az.kon.academy.catalog.event.management.variant.VariantKeyNameChangedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class VariantKeyRoot extends AggregateRoot<VariantKeyRoot, VariantKeyId> {

    @Getter private VariantName name;
    @Getter private String description;

    public static VariantKeyRoot initialize(VariantKeyCreateCommand command) {
        var variantKey = VariantKeyRoot.builder()
                .id(VariantKeyId.random())
                .name(command.getName())
                .description(command.getDescription())
                .build();

        var event = VariantKeyCreatedEvent.of(
                variantKey.getRootID().value().toString(),
                variantKey.getModificationTs().toOffsetDateTime(),
                variantKey.getName().value(),
                variantKey.getDescription()
        );

        variantKey.addEvent(event);
        return variantKey;
    }

    public VariantKeyRoot changeName(VariantKeyChangeNameCommand command) {
        var variantKey = this.toBuilder()
                .name(command.getName())
                .modificationTs(SeDateTime.now())
                .build();

        var event = VariantKeyNameChangedEvent.of(
                variantKey.getRootID().value().toString(),
                variantKey.getModificationTs().toOffsetDateTime(),
                variantKey.getName().value()
        );

        variantKey.addEvent(event);
        return variantKey;
    }

    public VariantKeyRoot changeDescription(VariantKeyChangeDescriptionCommand command) {
        var variantKey = this.toBuilder()
                .description(command.getDescription())
                .modificationTs(SeDateTime.now())
                .build();

        var event = VariantKeyDescriptionChangedEvent.of(
                variantKey.getRootID().value().toString(),
                variantKey.getModificationTs().toOffsetDateTime(),
                variantKey.getDescription()
        );

        variantKey.addEvent(event);
        return variantKey;
    }
}