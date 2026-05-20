package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryParentRemovedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryParentRemovedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryParentRemovedEventAvroMapper {

    ProductCategoryParentRemovedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryParentRemovedEventAvroMapper.class);

    ProductCategoryParentRemovedAvroModel toAvro(ProductCategoryParentRemovedEvent event);

    ProductCategoryParentRemovedEvent toEvent(ProductCategoryParentRemovedAvroModel avroModel);
}
