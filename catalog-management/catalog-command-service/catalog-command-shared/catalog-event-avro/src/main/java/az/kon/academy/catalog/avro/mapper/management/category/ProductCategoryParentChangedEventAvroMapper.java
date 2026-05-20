package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryParentChangedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryParentChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryParentChangedEventAvroMapper {

    ProductCategoryParentChangedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryParentChangedEventAvroMapper.class);

    ProductCategoryParentChangedAvroModel toAvro(ProductCategoryParentChangedEvent event);

    ProductCategoryParentChangedEvent toEvent(ProductCategoryParentChangedAvroModel avroModel);
}
