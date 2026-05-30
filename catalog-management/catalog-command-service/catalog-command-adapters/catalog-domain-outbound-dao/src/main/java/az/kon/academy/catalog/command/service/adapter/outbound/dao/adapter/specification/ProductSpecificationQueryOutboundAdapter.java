package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.specification;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductSpecificationMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import org.jooq.DSLContext;

import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_SPECIFICATION;
import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT;

@QueryAdapter
public class ProductSpecificationQueryOutboundAdapter implements ProductSpecificationQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductSpecificationMapper mapper;

    public ProductSpecificationQueryOutboundAdapter(DSLContext dsl, ProductSpecificationMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductSpecificationRoot> findByIdAndRowStatusActive(ProductSpecificationId id) {
        return dsl.selectFrom(PRODUCT_SPECIFICATION)
                .where(PRODUCT_SPECIFICATION.ID.eq(id.value())
                        .and(PRODUCT_SPECIFICATION.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                .fetchOptional()
                .map(record -> {
                    var categories = dsl.selectFrom(PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT)
                            .where(PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT.SPECIFICATION_ID.eq(record.getId()))
                            .fetch();
                    return mapper.toDomain(record, categories);
                });
    }
}
