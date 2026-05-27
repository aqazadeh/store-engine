package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductCategoryMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_CATEGORY;

@Component
@RequiredArgsConstructor
public class ProductCategoryQueryAdapter implements ProductCategoryQueryPort {

    private final DSLContext dsl;
    private final ProductCategoryMapper mapper;

    @Override
    public Optional<ProductCategoryRoot> findByIdAndRowStatusActive(ProductCategoryId id) {
        return dsl.selectFrom(PRODUCT_CATEGORY)
                .where(PRODUCT_CATEGORY.ID.eq(id.value())
                        .and(PRODUCT_CATEGORY.ROW_STATUS.eq(RowStatus.ACTIVE.name())))
                .fetchOptional()
                .map(mapper::toDomain);
    }
}
