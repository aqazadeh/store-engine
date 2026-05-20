package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandCreateGlobalRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.BrandCreateForMerchantRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;

public interface BrandRestPort {
    BrandCreateCommandResult createGlobalBrand(BrandCreateGlobalRequest request);

    BrandCreateCommandResult createMerchantBrand(BrandCreateForMerchantRequest request);

}
