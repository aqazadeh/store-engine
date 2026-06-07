package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductRejectionReasonMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductRejectionReasonCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import org.jooq.DSLContext;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_REJECTION_REASON;

@CommandAdapter
public class ProductRejectionReasonCommandOutboundAdapter implements ProductRejectionReasonCommandOutboundPort {

    private final DSLContext dsl;
    private final ProductRejectionReasonMapper mapper;

    public ProductRejectionReasonCommandOutboundAdapter(DSLContext dsl, ProductRejectionReasonMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

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
