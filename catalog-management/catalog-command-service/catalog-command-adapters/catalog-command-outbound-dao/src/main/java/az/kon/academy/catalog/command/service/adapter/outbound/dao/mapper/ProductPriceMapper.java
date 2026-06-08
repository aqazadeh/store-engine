package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.sql.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.sql.dal.tables.records.ProductPriceRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceMapper {

    public ProductPriceRecord toRecord(ProductPriceAggregateRoot root) {
        return new ProductPriceRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setVariantId(root.getVariantId().value())
                .setMinPrice(root.getMinPrice().value())
                .setMaxPrice(root.getMaxPrice().value());
    }

    public ProductPriceAggregateRoot toDomain(ProductPriceRecord r) {
        return ProductPriceAggregateRoot.builder()
                .id(ProductPriceId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .variantId(ProductVariantId.from(r.getVariantId()))
                .minPrice(Money.of(r.getMinPrice()))
                .maxPrice(Money.of(r.getMaxPrice()))
                .build();
    }
}
