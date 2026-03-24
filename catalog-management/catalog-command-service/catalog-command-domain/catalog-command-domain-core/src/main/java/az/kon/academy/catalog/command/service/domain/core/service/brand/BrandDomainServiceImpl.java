package az.kon.academy.catalog.command.service.domain.core.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.*;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryPort;
import az.kon.academy.catalog.command.service.domain.core.rules.BrandDomainRules;

import java.util.List;

public final class BrandDomainServiceImpl implements BrandModificationDomainService, BrandManagementDomainService {

    private final BrandQueryPort brandQueryPort;

    public BrandDomainServiceImpl(BrandQueryPort brandQueryPort) {
        this.brandQueryPort = brandQueryPort;
    }

    @Override
    public BrandRoot approve(BrandApproveCommand command) {
        var brand = this.brandQueryPort.fetchByIdAndStatusSentToApproval(command.getBrandId());
        return brand.approve();
    }

    @Override
    public BrandRoot reject(BrandRejectCommand command) {
        var brand = this.brandQueryPort.fetchByIdAndStatusSentToApproval(command.getBrandId());
        return brand.reject();
    }

    @Override
    public BrandRoot createBrand(BrandCreateCommand command) {
        var merchantBrands = this.brandQueryPort.fetchAllByMerchantIdAndRowStatusActive(command.getMerchantId());
        if (merchantBrands.size() >= BrandDomainRules.MAX_BRANDS_PER_MERCHANT) {
            throw new BrandDomainException(BrandDomainErrorCodes.TOO_MANY_BRANDS_FOR_MERCHANT, List.of(command.getMerchantId().toString()));
        }
        var existsByName = this.brandQueryPort.existsByNameAndRowStatusActive(command.getName());
        if (existsByName) {
            throw new BrandDomainException(BrandDomainErrorCodes.NAME_ALREADY_EXISTS, List.of(command.getName().value()));
        }
        return BrandRoot.initialize(command);
    }

    @Override
    public BrandRoot changeInformation(BrandChangeInformationCommand command) {
        var brand = this.brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.changeInformation(command);
    }

    @Override
    public BrandRoot changeImage(BrandChangeImageCommand command) {
        var brand = this.brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.changeImage(command);
    }

    @Override
    public BrandRoot sentToApproval(BrandSentToApprovalCommand command) {
        var brand = this.brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.sentToApproval();
    }

    @Override
    public BrandRoot moveToDraft(BrandSentToApprovalCommand command) {
        var brand = this.brandQueryPort.fetchByIdAndRowStatusActive(command.getBrandId());
        return brand.moveToDraft();
    }
}
