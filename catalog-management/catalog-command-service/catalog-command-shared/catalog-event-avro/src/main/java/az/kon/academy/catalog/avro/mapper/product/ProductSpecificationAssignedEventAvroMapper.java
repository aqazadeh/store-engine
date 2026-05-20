package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductSpecificationAssignedAvroModel;
import az.kon.academy.catalog.event.product.ProductSpecificationAssignedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSpecificationAssignedEventAvroMapper {

    ProductSpecificationAssignedEventAvroMapper INSTANCE = Mappers.getMapper(ProductSpecificationAssignedEventAvroMapper.class);

    ProductSpecificationAssignedAvroModel toAvro(ProductSpecificationAssignedEvent event);

    ProductSpecificationAssignedEvent toEvent(ProductSpecificationAssignedAvroModel avroModel);
}
