package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantKeyChangeDescriptionRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantKeyChangeNameRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantKeyCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantValueChangeNameRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantValueCreateRequest;
import az.kon.academy.catalog.command.service.application.service.handler.command.variant.VariantKeyChangeDescriptionCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.variant.VariantKeyChangeNameCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.variant.VariantKeyCreateCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.variant.VariantValueChangeNameCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.variant.VariantValueCreateCommandHandler;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyChangeDescriptionCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValue;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;

@InputAdapter
class VariantRestAdapter implements VariantRestPort {

    private final VariantKeyCreateCommandHandler variantKeyCreateCommandHandler;
    private final VariantKeyChangeNameCommandHandler variantKeyChangeNameCommandHandler;
    private final VariantKeyChangeDescriptionCommandHandler variantKeyChangeDescriptionCommandHandler;
    private final VariantValueCreateCommandHandler variantValueCreateCommandHandler;
    private final VariantValueChangeNameCommandHandler variantValueChangeNameCommandHandler;

    public VariantRestAdapter(VariantKeyCreateCommandHandler variantKeyCreateCommandHandler,
                              VariantKeyChangeNameCommandHandler variantKeyChangeNameCommandHandler,
                              VariantKeyChangeDescriptionCommandHandler variantKeyChangeDescriptionCommandHandler,
                              VariantValueCreateCommandHandler variantValueCreateCommandHandler,
                              VariantValueChangeNameCommandHandler variantValueChangeNameCommandHandler) {
        this.variantKeyCreateCommandHandler = variantKeyCreateCommandHandler;
        this.variantKeyChangeNameCommandHandler = variantKeyChangeNameCommandHandler;
        this.variantKeyChangeDescriptionCommandHandler = variantKeyChangeDescriptionCommandHandler;
        this.variantValueCreateCommandHandler = variantValueCreateCommandHandler;
        this.variantValueChangeNameCommandHandler = variantValueChangeNameCommandHandler;
    }

    @Override
    public void createKey(VariantKeyCreateRequest request) {
        var command = VariantKeyCreateCommand.builder()
                .name(VariantName.of(request.getName()))
                .description(request.getDescription())
                .build();
        this.variantKeyCreateCommandHandler.handle(command);
    }

    @Override
    public void changeKeyName(VariantKeyChangeNameRequest request) {
        var command = VariantKeyChangeNameCommand.builder()
                .variantKeyId(VariantKeyId.from(request.getVariantKeyId()))
                .name(VariantName.of(request.getName()))
                .build();
        this.variantKeyChangeNameCommandHandler.handle(command);
    }

    @Override
    public void changeKeyDescription(VariantKeyChangeDescriptionRequest request) {
        var command = VariantKeyChangeDescriptionCommand.builder()
                .variantKeyId(VariantKeyId.from(request.getVariantKeyId()))
                .description(request.getDescription())
                .build();
        this.variantKeyChangeDescriptionCommandHandler.handle(command);
    }

    @Override
    public void createValue(VariantValueCreateRequest request) {
        var command = VariantValueCreateCommand.builder()
                .keyId(VariantKeyId.from(request.getKeyId()))
                .name(VariantValue.of(request.getName()))
                .build();
        this.variantValueCreateCommandHandler.handle(command);
    }

    @Override
    public void changeValueName(VariantValueChangeNameRequest request) {
        var command = VariantValueChangeNameCommand.builder()
                .variantValueId(VariantValueId.from(request.getVariantValueId()))
                .name(VariantValue.of(request.getName()))
                .build();
        this.variantValueChangeNameCommandHandler.handle(command);
    }
}
