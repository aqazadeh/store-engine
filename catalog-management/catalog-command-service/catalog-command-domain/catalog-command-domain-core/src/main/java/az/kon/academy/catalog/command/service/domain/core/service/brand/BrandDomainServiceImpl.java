package az.kon.academy.catalog.command.service.domain.core.service.brand;

import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.*;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.MerchantQueryPort;
import az.kon.academy.catalog.command.service.domain.core.rules.BrandDomainRules;

import java.util.List;
import java.util.Objects;

public final class BrandDomainServiceImpl implements BrandModificationDomainService, BrandManagementDomainService {

    @Override
    public BrandRoot createGlobalBrand(DomainContext context, BrandCreateCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var merchantQueryPort = context.getQueryPort(MerchantQueryPort.class);
        if(!Objects.isNull(command.getOwner())) {
            var merchantIsExists = merchantQueryPort.isMerchantExists(command.getOwner());
            if(merchantIsExists == Boolean.FALSE){
                throw new BrandDomainException(BrandDomainErrorCodes.MERCHANT_NOT_FOUND, List.of(command.getOwner().toString()));
            }
        }

        if (brandQueryPort.existsByNameAndRowStatusActive(command.getName())) {
            throw new BrandDomainException(BrandDomainErrorCodes.NAME_ALREADY_EXISTS, List.of(command.getName().value()));
        }

        return BrandRoot.initializeGlobal(command);
    }

    @Override
    public BrandRoot changeBrandOwner(DomainContext context, BrandChangeOwnerCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var merchantQueryPort = context.getQueryPort(MerchantQueryPort.class);
        if(!Objects.isNull(command.getOwner())) {
            var merchantIsExists = merchantQueryPort.isMerchantExists(command.getOwner());
            if(merchantIsExists == Boolean.FALSE){
                throw new BrandDomainException(BrandDomainErrorCodes.MERCHANT_NOT_FOUND, List.of(command.getOwner().toString()));
            }
        }
        var brand = brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.changeOwner(command);
    }

    @Override
    public BrandRoot approve(DomainContext context, BrandApproveCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndStatusSentToApproval(command.getBrandId());
        return brand.approve();
    }

    @Override
    public BrandRoot reject(DomainContext context, BrandRejectCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndStatusSentToApproval(command.getBrandId());
        return brand.reject();
    }

    @Override
    public BrandRoot createBrand(DomainContext context, BrandCreateCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var merchantBrands = brandQueryPort.fetchAllByMerchantIdAndRowStatusActive(command.getOwner());
        if (merchantBrands.size() >= BrandDomainRules.MAX_BRANDS_PER_MERCHANT) {
            throw new BrandDomainException(BrandDomainErrorCodes.TOO_MANY_BRANDS_FOR_MERCHANT, List.of(command.getOwner().toString()));
        }
        var existsByName = brandQueryPort.existsByNameAndRowStatusActive(command.getName());
        if (existsByName) {
            throw new BrandDomainException(BrandDomainErrorCodes.NAME_ALREADY_EXISTS, List.of(command.getName().value()));
        }
        return BrandRoot.initializeForMerchant(command);
    }

    @Override
    public BrandRoot changeInformation(DomainContext context, BrandChangeInformationCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.changeInformation(command);
    }

    @Override
    public BrandRoot changeImage(DomainContext context, BrandChangeImageCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.changeImage(command);
    }

    @Override
    public BrandRoot sentToApproval(DomainContext context, BrandSentToApprovalCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.sentToApproval();
    }

    @Override
    public BrandRoot moveToDraft(DomainContext context, BrandSentToApprovalCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.moveToDraft();
    }
}
