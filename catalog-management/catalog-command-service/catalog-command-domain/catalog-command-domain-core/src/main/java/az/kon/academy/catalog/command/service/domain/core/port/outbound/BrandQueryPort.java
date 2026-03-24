package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;

import java.util.List;

public interface BrandQueryPort {
    BrandRoot fetchByIdAndStatusSentToApproval(BrandId id);

    BrandRoot fetchByIdAndRowStatusActive(BrandId id);

    List<BrandRoot> fetchAllByMerchantIdAndRowStatusActive(MerchantId merchantId);

    Boolean existsByNameAndRowStatusActive(BrandName name);
}
