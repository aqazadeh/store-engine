package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandApproveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateForGlobalCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandRejectCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.*;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.MerchantQueryPort;
import az.kon.academy.catalog.command.service.domain.core.rules.BrandDomainRules;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;
import java.util.Objects;

public final class BrandDomainServiceImpl implements BrandModificationDomainService, BrandManagementDomainService {

    @Override
    public BrandRoot createGlobalBrand(SeDomainContext context, BrandCreateForGlobalCommand command) {
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

        return BrandRoot.initializeForGlobal(command);
    }

    @Override
    public BrandRoot changeBrandOwner(SeDomainContext context, BrandChangeOwnerCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var merchantQueryPort = context.getQueryPort(MerchantQueryPort.class);
        var merchantIsExists = merchantQueryPort.isMerchantExists(command.getOwner());
        if(merchantIsExists == Boolean.FALSE){
            throw new BrandDomainException(BrandDomainErrorCodes.MERCHANT_NOT_FOUND, List.of(command.getOwner().toString()));
        }
        var brand = brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.changeOwner(command);
    }

    @Override
    public BrandRoot approve(SeDomainContext context, BrandApproveCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndStatusSentToApproval(command.getBrandId());
        return brand.approve();
    }

    @Override
    public BrandRoot reject(SeDomainContext context, BrandRejectCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndStatusSentToApproval(command.getBrandId());
        return brand.reject();
    }

    @Override
    public BrandRoot createForMerchant(SeDomainContext context, BrandCreateForMerchantCommand command) {
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
    public BrandRoot changeInformation(SeDomainContext context, BrandChangeInformationCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndMerchantIdAndRowStatusActive(command.getBrandId(), command.getOwner());
        return brand.changeInformation(command);
    }

    @Override
    public BrandRoot changeImage(SeDomainContext context, BrandChangeImageCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndMerchantIdAndRowStatusActive(command.getBrandId(), command.getOwner());
        return brand.changeImage(command);
    }

    @Override
    public BrandRoot sentToApproval(SeDomainContext context, BrandSentToApprovalCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndMerchantIdAndRowStatusActive(command.getBrandId(), command.getOwner());
        return brand.sentToApproval();
    }

    @Override
    public BrandRoot moveToDraft(SeDomainContext context, BrandMoveToDraftCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryPort.class);
        var brand = brandQueryPort.fetchByIdAndMerchantIdAndRowStatusActive(command.getBrandId(), command.getOwner());
        return brand.moveToDraft();
    }

    @Override
    public BrandRejectionReasonRoot rejectReason(SeDomainContext context, BrandCreateRejectionReasonCommand command) {
        return BrandRejectionReasonRoot.initialize(command);
    }
}
