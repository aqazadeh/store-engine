package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.Quantity;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.sql.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.sql.dal.tables.records.ProductStockRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import org.springframework.stereotype.Component;

@Component
public class ProductStockMapper {

    public ProductStockRecord toRecord(ProductStockAggregateRoot root) {
        return new ProductStockRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setVariantId(root.getVariantId().value())
                .setQuantity(root.getQuantity().intValue());
    }

    public ProductStockAggregateRoot toDomain(ProductStockRecord r) {
        return ProductStockAggregateRoot.builder()
                .id(ProductStockId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .variantId(ProductVariantId.from(r.getVariantId()))
                .quantity(Quantity.of(r.getQuantity()))
                .build();
    }
}
