package az.kon.academy.catalog.avro.mapper.management.specification;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.specification.ProductSpecificationDeletedAvroModel;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationDeletedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSpecificationDeletedEventAvroMapper {

    ProductSpecificationDeletedEventAvroMapper INSTANCE = Mappers.getMapper(ProductSpecificationDeletedEventAvroMapper.class);

    ProductSpecificationDeletedAvroModel toAvro(ProductSpecificationDeletedEvent event);

    ProductSpecificationDeletedEvent toEvent(ProductSpecificationDeletedAvroModel avroModel);
}
