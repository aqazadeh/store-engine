package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.specification;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.sql.dal.tables.records.ProductSpecificationCategoryAssignmentRecord;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductSpecificationMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.specification.ProductSpecificationDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.specification.ProductSpecificationDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_SPECIFICATION;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.selectFrom;

@QueryAdapter
public class ProductSpecificationQueryOutboundAdapter implements ProductSpecificationQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductSpecificationMapper mapper;

    public ProductSpecificationQueryOutboundAdapter(DSLContext dsl, ProductSpecificationMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductSpecificationRoot> findById(ProductSpecificationId productSpecificationId) {
        var categoriesField = multiset(
                selectFrom(PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT)
                        .where(PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT.SPECIFICATION_ID
                                .eq(PRODUCT_SPECIFICATION.ID))
        )
                .as("categories")
                .convertFrom(r -> r.into(ProductSpecificationCategoryAssignmentRecord.class));

        return dsl.select(
                        PRODUCT_SPECIFICATION.asterisk(),
                        categoriesField
                )
                .from(PRODUCT_SPECIFICATION)
                .where(PRODUCT_SPECIFICATION.ID.eq(productSpecificationId.value()))
                .and(PRODUCT_SPECIFICATION.ROW_STATUS.eq(RowStatusType.ACTIVE))
                .fetchOptional()
                .map(r -> mapper.toDomain(r.into(PRODUCT_SPECIFICATION), r.get(categoriesField)));
    }

    @Override
    public ProductSpecificationRoot fetchById(ProductSpecificationId productSpecificationId) {
        return this.findById(productSpecificationId)
                .orElseThrow(() -> new ProductSpecificationDomainException(
                        ProductSpecificationDomainErrorCodes.ENTITY_NOT_FOUND,
                        List.of(productSpecificationId.toString())
                ));
    }

    @Override
    public Boolean existsAssignmentByCategoryId(ProductCategoryId categoryId) {
        return dsl.fetchExists(
                PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT,
                PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT.CATEGORY_ID.eq(categoryId.value())
        );
    }

    @Override
    public List<ProductSpecificationId> findRequiredByCategoryId(ProductCategoryId categoryId) {
        return List.of();
    }

    @Override
    public void checkAllExistByIds(List<ProductSpecificationId> ids) {

    }
}
