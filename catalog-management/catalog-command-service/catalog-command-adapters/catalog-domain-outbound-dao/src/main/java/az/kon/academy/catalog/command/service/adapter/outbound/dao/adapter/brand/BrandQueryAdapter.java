package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component // FIXME change to custom annotation @QueryAdapter
public class BrandQueryAdapter implements BrandQueryPort {
    @Override
    public Optional<BrandRoot> fetchById(BrandId id) {
        return Optional.empty();
    }

    @Override
    public Optional<BrandRoot> findByIdAndMerchantIdAndRowStatusActive(BrandId brandId, MerchantId merchantId) {
        return Optional.empty();
    }

    @Override
    public Optional<BrandRoot> findByIdAndRowStatusActive(BrandId id) {
        return Optional.empty();
    }

    @Override
    public List<BrandRoot> fetchAllByMerchantIdAndRowStatusActive(MerchantId merchantId) {
        return List.of();
    }

    @Override
    public Boolean existsByNameAndRowStatusActive(BrandName name) {
        return null;
    }
}
