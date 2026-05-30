package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryImageChangedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryImageChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryImageChangedEventAvroMapper {

    ProductCategoryImageChangedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryImageChangedEventAvroMapper.class);

    ProductCategoryImageChangedAvroModel toAvro(ProductCategoryImageChangedEvent event);

    ProductCategoryImageChangedEvent toEvent(ProductCategoryImageChangedAvroModel avroModel);
}
