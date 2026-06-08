package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductCategoryMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_CATEGORY;

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
                        ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND, List.of(productCategoryId.toString()))
                );
    }

    @Override
    public void checkExitsById(ProductCategoryId productCategoryId) {
        if (!dsl.fetchExists(
                PRODUCT_CATEGORY,
                PRODUCT_CATEGORY.ID.eq(productCategoryId.value())
                        .and(PRODUCT_CATEGORY.ROW_STATUS.eq(RowStatusType.ACTIVE))
        )) {
            throw new ProductCategoryEntityNotFoundException(
                    ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                    List.of(productCategoryId.toString())
            );
        }
    }

    @Override
    public void checkIsNotDescendant(ProductCategoryId categoryId, ProductCategoryId parentId) {
        if (parentId == null) {
            return;
        }
        if (categoryId.value().equals(parentId.value())) {
            throw new ProductCategoryDomainException(
                    ProductCategoryDomainErrorCodes.CIRCULAR_PARENT_REFERENCE,
                    List.of(categoryId.toString(), parentId.toString()));
        }
        var currentId = parentId.value();
        while (currentId != null) {
            var parent = dsl.select(PRODUCT_CATEGORY.PARENT_ID)
                    .from(PRODUCT_CATEGORY)
                    .where(PRODUCT_CATEGORY.ID.eq(currentId)
                            .and(PRODUCT_CATEGORY.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                    .fetchOne();
            if (parent == null || parent.value1() == null) {
                break;
            }
            if (parent.value1().equals(categoryId.value())) {
                throw new ProductCategoryDomainException(
                        ProductCategoryDomainErrorCodes.CIRCULAR_PARENT_REFERENCE,
                        List.of(categoryId.toString(), parentId.toString()));
            }
            currentId = parent.value1();
        }
    }
}
