package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;

import java.util.List;
import java.util.Optional;

public interface BrandQueryPort {
    Optional<BrandRoot> fetchById(BrandId id);

    default BrandRoot fetchByIdAndStatusSentToApproval(BrandId id){
        return this.fetchById(id).orElseThrow(() ->
                new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString())));
    }

    default BrandRoot fetchByIdAndMerchantIdAndRowStatusActive(BrandId brandId, MerchantId merchantId) {
        return this.findByIdAndMerchantIdAndRowStatusActive(brandId, merchantId).orElseThrow(() ->
                new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND, List.of(brandId.toString())));
    }
    Optional<BrandRoot> findByIdAndMerchantIdAndRowStatusActive(BrandId brandId, MerchantId merchantId);

    BrandRoot fetchByIdAndRowStatusActive(BrandId id);

    List<BrandRoot> fetchAllByMerchantIdAndRowStatusActive(MerchantId merchantId);

    Boolean existsByNameAndRowStatusActive(BrandName name);
}
