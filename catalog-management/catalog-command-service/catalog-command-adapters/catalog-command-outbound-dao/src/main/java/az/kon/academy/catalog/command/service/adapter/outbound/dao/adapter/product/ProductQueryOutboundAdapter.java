package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductMapper;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductVariantMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

@QueryAdapter
public class ProductQueryOutboundAdapter implements ProductQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductMapper mapper;
    private final ProductVariantMapper variantMapper;

    public ProductQueryOutboundAdapter(DSLContext dsl, ProductMapper mapper, ProductVariantMapper variantMapper) {
        this.dsl = dsl;
        this.mapper = mapper;
        this.variantMapper = variantMapper;
    }

    @Override
    public Optional<ProductRoot> findById(ProductId id) {
        return Optional.empty();
    }

    @Override
    public ProductRoot fetchById(ProductId id) {
        return this.findById(id).orElseThrow(() ->
                new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString())));
    }

    @Override
    public ProductRoot fetchByIdAndMerchantId(ProductId id, MerchantId merchantId) {
        return null;
    }

    @Override
    public void checkExistsByIdAndMerchantId(ProductId id, MerchantId merchantId) {

    }

    @Override
    public void checkExistsById(ProductId id) {

    }

    @Override
    public Boolean exitsByCategoryId(ProductCategoryId productCategoryId) {
        return null;
    }
}
