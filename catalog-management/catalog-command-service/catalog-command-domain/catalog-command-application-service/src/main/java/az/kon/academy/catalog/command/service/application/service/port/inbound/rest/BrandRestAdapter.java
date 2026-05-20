package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandCreateGlobalRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.BrandCreateForMerchantRequest;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.management.BrandCreateGlobalCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant.BrandCreateMerchantCommandHandler;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateForGlobalCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandCreateForMerchantCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandPath;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.domain.core.security.SeSecurityContextHolder;

import java.util.Objects;

@InputAdapter
class BrandRestAdapter implements BrandRestPort {

    private final SeSecurityContextHolder securityContextHolder;
    private final BrandCreateGlobalCommandHandler brandCreateGlobalCommandHandler;
    private final BrandCreateMerchantCommandHandler brandCreateMerchantCommandHandler;

    public BrandRestAdapter(SeSecurityContextHolder securityContextHolder,
                            BrandCreateGlobalCommandHandler brandCreateGlobalCommandHandler,
                            BrandCreateMerchantCommandHandler brandCreateMerchantCommandHandler) {
        this.brandCreateGlobalCommandHandler = brandCreateGlobalCommandHandler;
        this.brandCreateMerchantCommandHandler = brandCreateMerchantCommandHandler;
        this.securityContextHolder = securityContextHolder;
    }

    @Override
    public BrandCreateCommandResult createGlobalBrand(BrandCreateGlobalRequest request) {

        var command = BrandCreateForGlobalCommand.builder()
                .owner(Objects.isNull(request.getOwner()) ? null : MerchantId.from(request.getOwner()))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .path(BrandPath.create(request.getName()))
                .build();

        return this.brandCreateGlobalCommandHandler.handle(command);
    }

    @Override
    public BrandCreateCommandResult createMerchantBrand(BrandCreateForMerchantRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = BrandCreateForMerchantCommand.builder()
                .owner(MerchantId.from(currentUser))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .path(BrandPath.create(request.getName()))
                .build();
        return this.brandCreateMerchantCommandHandler.handle(command);
    }
}
