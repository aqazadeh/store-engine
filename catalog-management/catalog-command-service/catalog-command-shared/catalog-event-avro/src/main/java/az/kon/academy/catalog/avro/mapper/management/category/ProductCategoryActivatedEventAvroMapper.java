package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryActivatedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryActivatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryActivatedEventAvroMapper {

    ProductCategoryActivatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryActivatedEventAvroMapper.class);

    ProductCategoryActivatedAvroModel toAvro(ProductCategoryActivatedEvent event);

    ProductCategoryActivatedEvent toEvent(ProductCategoryActivatedAvroModel avroModel);
}
