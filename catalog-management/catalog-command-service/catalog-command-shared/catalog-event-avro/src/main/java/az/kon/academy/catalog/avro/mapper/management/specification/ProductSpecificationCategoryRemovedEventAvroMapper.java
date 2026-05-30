package az.kon.academy.catalog.avro.mapper.management.specification;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.specification.ProductSpecificationCategoryRemovedAvroModel;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCategoryRemovedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSpecificationCategoryRemovedEventAvroMapper {

    ProductSpecificationCategoryRemovedEventAvroMapper INSTANCE = Mappers.getMapper(ProductSpecificationCategoryRemovedEventAvroMapper.class);

    ProductSpecificationCategoryRemovedAvroModel toAvro(ProductSpecificationCategoryRemovedEvent event);

    ProductSpecificationCategoryRemovedEvent toEvent(ProductSpecificationCategoryRemovedAvroModel avroModel);
}
