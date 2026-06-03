package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryArchivedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryArchivedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryArchivedEventAvroMapper {

    ProductCategoryArchivedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryArchivedEventAvroMapper.class);

    ProductCategoryArchivedAvroModel toAvro(ProductCategoryArchivedEvent event);

    ProductCategoryArchivedEvent toEvent(ProductCategoryArchivedAvroModel avroModel);
}
