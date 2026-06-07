package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductCategoryMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_CATEGORY;

@QueryAdapter
public class ProductCategoryQueryOutboundAdapter implements ProductCategoryQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductCategoryMapper mapper;

    public ProductCategoryQueryOutboundAdapter(DSLContext dsl, ProductCategoryMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductCategoryRoot> findById(ProductCategoryId productCategoryId) {
        return dsl.selectFrom(PRODUCT_CATEGORY)
                .where(PRODUCT_CATEGORY.ID.eq(productCategoryId.value())
                        .and(PRODUCT_CATEGORY.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public ProductCategoryRoot fetchById(ProductCategoryId productCategoryId) {
        return this.findById(productCategoryId)
                .orElseThrow(() -> new ProductCategoryEntityNotFoundException(
                        ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND, List.of(productCategoryId.value().toString()))
                );
    }

    @Override
    public Boolean exitsByCategoryId(ProductCategoryId productCategoryId) {
        return dsl.fetchExists(
                PRODUCT_CATEGORY,
                PRODUCT_CATEGORY.ID.eq(productCategoryId.value())
                        .and(PRODUCT_CATEGORY.ROW_STATUS.eq(RowStatusType.ACTIVE))
        );
    }
}
