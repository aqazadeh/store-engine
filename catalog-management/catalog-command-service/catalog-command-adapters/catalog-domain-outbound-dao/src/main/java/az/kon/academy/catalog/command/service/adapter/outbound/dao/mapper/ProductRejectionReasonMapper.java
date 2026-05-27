package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.tables.records.ProductRejectionReasonRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import org.springframework.stereotype.Component;

@Component
public class ProductRejectionReasonMapper {

    public ProductRejectionReasonRecord toRecord(ProductRejectionReasonRoot root) {
        return new ProductRejectionReasonRecord()
                .setId(root.getRootID().value())
                .setProductId(root.getProductId().value())
                .setReason(root.getReason())
                .setModeratedBy(root.getModeratedBy().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(root.getProcessStatus().name())
                .setRowStatus(root.getRowStatus().name())
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime());
    }

    public ProductRejectionReasonRoot toDomain(ProductRejectionReasonRecord r) {
        return ProductRejectionReasonRoot.builder()
                .id(ProductRejectionReasonId.from(r.getId()))
                .productId(ProductId.from(r.getProductId()))
                .reason(r.getReason())
                .moderatedBy(ModeratorId.from(r.getModeratedBy()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))
                .build();
    }
}
