package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductBrandAssignedAvroModel;
import az.kon.academy.catalog.event.product.ProductBrandAssignedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductBrandAssignedEventAvroMapper {

    ProductBrandAssignedEventAvroMapper INSTANCE = Mappers.getMapper(ProductBrandAssignedEventAvroMapper.class);

    ProductBrandAssignedAvroModel toAvro(ProductBrandAssignedEvent event);

    ProductBrandAssignedEvent toEvent(ProductBrandAssignedAvroModel avroModel);
}
