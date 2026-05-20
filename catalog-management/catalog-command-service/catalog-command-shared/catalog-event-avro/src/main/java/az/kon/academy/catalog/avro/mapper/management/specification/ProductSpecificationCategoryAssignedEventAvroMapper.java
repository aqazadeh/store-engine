package az.kon.academy.catalog.avro.mapper.management.specification;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.specification.ProductSpecificationCategoryAssignedAvroModel;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCategoryAssignedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSpecificationCategoryAssignedEventAvroMapper {

    ProductSpecificationCategoryAssignedEventAvroMapper INSTANCE = Mappers.getMapper(ProductSpecificationCategoryAssignedEventAvroMapper.class);

    ProductSpecificationCategoryAssignedAvroModel toAvro(ProductSpecificationCategoryAssignedEvent event);

    ProductSpecificationCategoryAssignedEvent toEvent(ProductSpecificationCategoryAssignedAvroModel avroModel);
}
