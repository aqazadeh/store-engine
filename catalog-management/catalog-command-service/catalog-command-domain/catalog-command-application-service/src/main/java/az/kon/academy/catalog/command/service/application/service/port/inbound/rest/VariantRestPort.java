package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantKeyChangeDescriptionRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantKeyChangeNameRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantKeyCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantValueChangeNameRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.VariantValueCreateRequest;

public interface VariantRestPort {

    void createKey(VariantKeyCreateRequest request);

    void changeKeyName(VariantKeyChangeNameRequest request);

    void changeKeyDescription(VariantKeyChangeDescriptionRequest request);

    void createValue(VariantValueCreateRequest request);

    void changeValueName(VariantValueChangeNameRequest request);
}
