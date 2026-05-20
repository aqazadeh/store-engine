package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryCreatedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryCreatedEventAvroMapper {

    ProductCategoryCreatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryCreatedEventAvroMapper.class);

    ProductCategoryCreatedAvroModel toAvro(ProductCategoryCreatedEvent event);

    ProductCategoryCreatedEvent toEvent(ProductCategoryCreatedAvroModel avroModel);
}
