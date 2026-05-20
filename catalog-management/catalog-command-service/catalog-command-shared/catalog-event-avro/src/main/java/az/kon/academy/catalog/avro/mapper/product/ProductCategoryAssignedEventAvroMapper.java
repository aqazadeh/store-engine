package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductCategoryAssignedAvroModel;
import az.kon.academy.catalog.event.product.ProductCategoryAssignedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryAssignedEventAvroMapper {

    ProductCategoryAssignedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryAssignedEventAvroMapper.class);

    ProductCategoryAssignedAvroModel toAvro(ProductCategoryAssignedEvent event);

    ProductCategoryAssignedEvent toEvent(ProductCategoryAssignedAvroModel avroModel);
}
