package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.sql.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.sql.dal.tables.records.ProductCategoryRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryPath;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryMapper {

    public ProductCategoryRecord toRecord(ProductCategoryRoot root) {
        return new ProductCategoryRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setParentId(root.getParent() != null ? root.getParent().value() : null)
                .setName(root.getName().value())
                .setPath(root.getPath().value())
                .setDescription(root.getDescription().value())
                .setImage(root.getImage());
    }

    public ProductCategoryRoot toDomain(ProductCategoryRecord r) {
        return ProductCategoryRoot.builder()
                .id(ProductCategoryId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .parent(r.getParentId() != null ? ProductCategoryId.from(r.getParentId()) : null)
                .name(ProductCategoryName.of(r.getName()))
                .path(ProductCategoryPath.of(r.getPath()))
                .description(ProductCategoryDescription.of(r.getDescription()))
                .image(r.getImage())
                .build();
    }
}
