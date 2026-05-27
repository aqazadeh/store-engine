package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductRejectionReasonMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductRejectionReasonCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_REJECTION_REASON;

@Component
@RequiredArgsConstructor
public class ProductRejectionReasonCommandAdapter implements ProductRejectionReasonCommandPort {

    private final DSLContext dsl;
    private final ProductRejectionReasonMapper mapper;

    @Override
    public ProductRejectionReasonRoot save(ProductRejectionReasonRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(PRODUCT_REJECTION_REASON)
                .set(record)
                .onConflict(PRODUCT_REJECTION_REASON.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
