package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.sql.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.sql.dal.tables.records.ProductSpecificationCategoryAssignmentRecord;
import az.kon.academy.catalog.sql.dal.tables.records.ProductSpecificationRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationCategoryAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductSpecificationMapper {

    public ProductSpecificationRecord toRecord(ProductSpecificationRoot root) {
        return new ProductSpecificationRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setName(root.getName().value())
                .setDescription(root.getDescription().value());
    }

    public ProductSpecificationCategoryAssignmentRecord toCategoryAssignmentRecord(
            UUID specificationId, ProductSpecificationCategoryAssignment assignment) {
        return new ProductSpecificationCategoryAssignmentRecord()
                .setSpecificationId(specificationId)
                .setCategoryId(assignment.getCategoryId().value())
                .setIsRequired(assignment.isRequired());
    }

    public ProductSpecificationCategoryAssignment toAssignment(ProductSpecificationCategoryAssignmentRecord r) {
        return ProductSpecificationCategoryAssignment.initialize(
                ProductCategoryId.from(r.getCategoryId()),
                r.getIsRequired()
        );
    }

    public ProductSpecificationRoot toDomain(
            ProductSpecificationRecord r,
            List<ProductSpecificationCategoryAssignmentRecord> categories) {
        Set<ProductSpecificationCategoryAssignment> assignments = categories.stream()
                .map(this::toAssignment)
                .collect(Collectors.toSet());

        return ProductSpecificationRoot.builder()
                .id(ProductSpecificationId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .name(SpecificationName.of(r.getName()))
                .description(SpecificationDescription.of(r.getDescription()))
                .categories(assignments)
                .build();
    }
}
