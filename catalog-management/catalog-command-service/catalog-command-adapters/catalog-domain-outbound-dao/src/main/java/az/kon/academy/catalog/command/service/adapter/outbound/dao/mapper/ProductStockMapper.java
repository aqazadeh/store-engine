package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.dal.tables.records.ProductStockRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import org.springframework.stereotype.Component;

@Component
public class ProductStockMapper {

    public ProductStockRecord toRecord(ProductStockRoot root) {
        return new ProductStockRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setVariantId(root.getVariantId().value())
                .setQuantity(root.getQuantity());
    }

    public ProductStockRoot toDomain(ProductStockRecord r) {
        return ProductStockRoot.builder()
                .id(ProductStockId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .variantId(ProductVariantId.from(r.getVariantId()))
                .quantity(r.getQuantity())
                .build();
    }
}
