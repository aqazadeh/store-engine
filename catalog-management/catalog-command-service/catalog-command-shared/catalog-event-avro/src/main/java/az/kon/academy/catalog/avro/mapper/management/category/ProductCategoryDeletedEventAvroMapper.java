package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryDeletedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryDeletedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryDeletedEventAvroMapper {

    ProductCategoryDeletedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryDeletedEventAvroMapper.class);

    ProductCategoryDeletedAvroModel toAvro(ProductCategoryDeletedEvent event);

    ProductCategoryDeletedEvent toEvent(ProductCategoryDeletedAvroModel avroModel);
}
