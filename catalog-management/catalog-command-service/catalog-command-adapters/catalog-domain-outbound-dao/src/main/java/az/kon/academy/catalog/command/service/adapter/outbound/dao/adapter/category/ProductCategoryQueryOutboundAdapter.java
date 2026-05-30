package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductCategoryMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import org.jooq.DSLContext;

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
    public Optional<ProductCategoryRoot> findByIdAndRowStatusActive(ProductCategoryId id) {
        return dsl.selectFrom(PRODUCT_CATEGORY)
                .where(PRODUCT_CATEGORY.ID.eq(id.value())
                        .and(PRODUCT_CATEGORY.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                .fetchOptional()
                .map(mapper::toDomain);
    }
}
