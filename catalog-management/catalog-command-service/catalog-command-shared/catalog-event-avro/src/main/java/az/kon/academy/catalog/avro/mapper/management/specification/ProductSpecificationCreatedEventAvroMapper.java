package az.kon.academy.catalog.avro.mapper.management.specification;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.specification.ProductSpecificationCreatedAvroModel;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSpecificationCreatedEventAvroMapper {

    ProductSpecificationCreatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductSpecificationCreatedEventAvroMapper.class);

    ProductSpecificationCreatedAvroModel toAvro(ProductSpecificationCreatedEvent event);

    ProductSpecificationCreatedEvent toEvent(ProductSpecificationCreatedAvroModel avroModel);
}
